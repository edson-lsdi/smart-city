package lsdi.hf.smartCity.AccessControl.Contracts;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import lsdi.hf.smartCity.AccessControl.Assets.AttributeAsset;
import lsdi.hf.smartCity.AccessControl.Converter.InstantConverter;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Contract(
    name = "attribute",
    info = @Info(
        title = "attribute",
        description = "store attributes for intercity security",
        version = "0.0.1-SNAPSHOT",
        contact = @Contact(
            email = "edson.sampaio@lsdi.ufma.br",
            name = "Edson Carlos"
    )))
public class AttributeContract implements ContractInterface {
    private final static String ASSET_TYPE = "ATTRIBUTE_";

    private final Genson genson = new GensonBuilder()
            .withConverter(new InstantConverter(), Instant.class)
            .create();

    private enum AssetTransferErrors {
        ASSET_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean AssetExists(final Context ctx, final String assetID) {
        String assetJSON = ctx.getStub().getStringState(assetID);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void addAttribute(final Context ctx, final String attributeJSON) {
        AttributeAsset attributeAsset = genson.deserialize(attributeJSON, AttributeAsset.class);
        String attributeKey = ASSET_TYPE + attributeAsset.getAttributeID();

        if (AssetExists(ctx, attributeKey)) {
            String errorMessage = String.format("Asset %s already exists", attributeAsset.getAttributeID());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        ctx.getStub().putStringState(attributeKey, genson.serialize(attributeAsset));
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listAttributes(final Context ctx) {
        List<AttributeAsset> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = ctx.getStub().getStateByRange("", "");

        for (KeyValue result: results) {
            AttributeAsset attributeAsset = genson.deserialize(result.getStringValue(), AttributeAsset.class);
            System.out.println(attributeAsset);
            queryResults.add(attributeAsset);
        }

        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listAttributesByEntityManager(final Context ctx, final String entityManagerID) {
        String queryString = String.format("{\"selector\":{\"entityManagerID\":\"%s\"}}", entityManagerID);

        QueryResultsIterator<KeyValue> results = ctx.getStub().getQueryResult(queryString);

        List<AttributeAsset> queryResults = new ArrayList<>();

        for (KeyValue result : results) {
            if (result.getKey().startsWith(ASSET_TYPE)) {
                AttributeAsset attributeAsset = genson.deserialize(result.getStringValue(), AttributeAsset.class);
                queryResults.add(attributeAsset);
            }
        }

        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listAttributesByPublicAgent(final Context ctx, final String publicAgentID) {
        String queryString = String.format("{\"selector\":{\"publicAgentID\":\"%s\"}}", publicAgentID);

        QueryResultsIterator<KeyValue> results = ctx.getStub().getQueryResult(queryString);

        List<AttributeAsset> queryResults = new ArrayList<>();

        for (KeyValue result : results) {
            if (result.getKey().startsWith(ASSET_TYPE)) {
                AttributeAsset attributeAsset = genson.deserialize(result.getStringValue(), AttributeAsset.class);
                queryResults.add(attributeAsset);
            }
        }

        return genson.serialize(queryResults);
    }
}
