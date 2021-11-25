package repository.tables;

import com.fasterxml.jackson.annotation.JsonProperty;
import kpersistence.mapping.annotations.CurrentUserId;

public abstract class UserIdStringIdTable extends StringIdTable {

    @CurrentUserId
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
