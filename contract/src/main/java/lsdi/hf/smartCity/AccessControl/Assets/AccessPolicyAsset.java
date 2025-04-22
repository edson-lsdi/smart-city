package lsdi.hf.smartCity.AccessControl.Assets;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

import java.time.Instant;

@DataType
public class AccessPolicyAsset {
    @Property
    private final String policyID;
    @Property
    private final String policyDescription;
    @Property
    private final String policy;
    @Property
    private final boolean enforce;
    @Property
    private final String cityResourceType;   // InterSCity: resource_type (TAG)
    @Property
    private final String dataList;   // InterScity: capabilities list
    @Property
    private final String entityManagerID;   // identificador do asset (componente de software) no trabalho André (id)
    @Property
    private final String publicAgentID;   // identificador do asset (cidadão) no trabalho Danilo (full_name)
    @Property
    private final Instant timestampEvent;

    public AccessPolicyAsset(
        @JsonProperty("policyID") String policyID,
        @JsonProperty("policyDescription") String policyDescription,
        @JsonProperty("policy") String policy,
        @JsonProperty("enforce") boolean enforce,
        @JsonProperty("cityResourceType") String cityResourceType,
        @JsonProperty("dataList") String dataList,
        @JsonProperty("entityManagerID") String entityManagerID,
        @JsonProperty("publicAgentID") String publicAgentID,
        @JsonProperty("timestampEvent") Instant timestampEvent
    ) {
        this.policyID = policyID;
        this.policyDescription = policyDescription;
        this.policy = policy;
        this.enforce = enforce;
        this.cityResourceType = cityResourceType;
        this.dataList = dataList;
        this.entityManagerID = entityManagerID;
        this.publicAgentID = publicAgentID;
        this.timestampEvent = timestampEvent;
    }

    public String getPolicyID() {
        return policyID;
    }

    public String getPolicyDescription() {
        return policyDescription;
    }

    public String getPolicy() {
        return policy;
    }

    public String getCityResourceType() {
        return cityResourceType;
    }

    public boolean isEnforce() {
        return enforce;
    }

    public String getDataList() {
        return dataList;
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
