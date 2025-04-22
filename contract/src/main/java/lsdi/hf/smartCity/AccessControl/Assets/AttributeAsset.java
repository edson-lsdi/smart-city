package lsdi.hf.smartCity.AccessControl.Assets;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

import java.time.Instant;

@DataType
public class AttributeAsset {
    @Property
    private final String attributeID;
    @Property
    private final String descriptionAttribute;
    @Property
    private final String entityManagerID;
    @Property
    private final String publicAgentID;
    @Property
    private final Instant timestampEvent;

    public AttributeAsset(
        @JsonProperty("attributeID") String attributeID,
        @JsonProperty("descriptionAttribute") String descriptionAttribute,
        @JsonProperty("publicAgentID") String publicAgentID,
        @JsonProperty("entityManagerID") String entityManagerID, 
        @JsonProperty("timestampEvent") Instant timestampEvent
    ) {
        this.attributeID = attributeID;
        this.descriptionAttribute = descriptionAttribute;
        this.publicAgentID = publicAgentID;
        this.entityManagerID = entityManagerID;
        this.timestampEvent = timestampEvent;
    }

    public String getAttributeID() {
        return attributeID;
    }

    public String getDescriptionAttribute() {
        return descriptionAttribute;
    }

    public String getPublicAgentID() {
        return publicAgentID;
    }

    public String getEntityManagerID() {
        return entityManagerID;
    }

    public Instant getTimestampEvent() {
        return timestampEvent;
    }
}
