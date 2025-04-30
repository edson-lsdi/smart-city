package lsdi.hf.smartCity.AccessControl.Contracts;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import lsdi.hf.smartCity.AccessControl.Assets.AccessPolicyAsset;
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
    name = "policy",
    info = @Info(
        title = "Access policy",
        description = "store policies for intercity security",
        version = "0.0.1-SNAPSHOT",
        contact = @Contact(
            email = "lucas.oliveira@lsdi.ufma.br",
            name = "Lucas Oliveira"
    )))
public class AccessPolicyContract implements ContractInterface {
    private final static String ASSET_TYPE = "AcessPolicy_";

    private final Genson genson = new GensonBuilder()
            .withConverter(new InstantConverter(), Instant.class)
            .create();

    private enum AssetTransferErrors {
        ASSET_ALREADY_EXISTS,
        ASSET_NOT_FOUND
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean AssetExists(final Context ctx, final String policyID) {
        String assetJSON = ctx.getStub().getStringState(policyID);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void addPolicy(final Context ctx, final String policyJSON){
        AccessPolicyAsset policyAsset = genson.deserialize(policyJSON, AccessPolicyAsset.class);
        String policyKey = ASSET_TYPE + policyAsset.getPolicyID();

        if (AssetExists(ctx, policyKey)) {
            String errorMessage = String.format("Asset %s already exists", policyAsset.getPolicyID());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        ctx.getStub().putStringState(policyKey, genson.serialize(policyAsset));
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listPolicys(final Context ctx) {
        List<AccessPolicyAsset> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = ctx.getStub().getStateByRange("", "");

        for (KeyValue result: results) {
            AccessPolicyAsset AccessPolicyAsset = genson.deserialize(result.getStringValue(), AccessPolicyAsset.class);
            System.out.println(AccessPolicyAsset);
            queryResults.add(AccessPolicyAsset);
        }

        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listPolicysByEntityManager(final Context ctx,final String entityManagerID) {
        String queryString = String.format("{\"selector\":{\"entityManagerID\":\"%s\"}}", entityManagerID);

        QueryResultsIterator<KeyValue> results = ctx.getStub().getQueryResult(queryString);

        List<AccessPolicyAsset> queryResults = new ArrayList<>();

        for (KeyValue result : results) {
            if (result.getKey().startsWith(ASSET_TYPE)) {
                AccessPolicyAsset policyAsset = genson.deserialize(result.getStringValue(), AccessPolicyAsset.class);
                queryResults.add(policyAsset);
            }
        }

        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listPolicysByPublicAgent(final Context ctx,final String publicAgentID) {
        String queryString = String.format("{\"selector\":{\"publicAgentID\":\"%s\"}}", publicAgentID);

        QueryResultsIterator<KeyValue> results = ctx.getStub().getQueryResult(queryString);

        List<AccessPolicyAsset> queryResults = new ArrayList<>();

        for (KeyValue result : results) {
            if (result.getKey().startsWith(ASSET_TYPE)) {
                AccessPolicyAsset policyAsset = genson.deserialize(result.getStringValue(), AccessPolicyAsset.class);
                queryResults.add(policyAsset);
            }
        }

        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void EditPolicy(final Context ctx, final String policyJSON) {
        AccessPolicyAsset policyAsset = genson.deserialize(policyJSON, AccessPolicyAsset.class);
        String policyKey = ASSET_TYPE + policyAsset.getPolicyID();

        if (!AssetExists(ctx, policyKey)) {
            String errorMessage = String.format("Asset %s does not exist", policyKey);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        ctx.getStub().putStringState(policyKey, genson.serialize(policyAsset));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void revokePolicy(final Context ctx, final String policyID) {
        if (!AssetExists(ctx, policyID)) {
            String errorMessage = String.format("Asset %s does not exist", policyID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }
        ctx.getStub().delState(policyID);
    }



}