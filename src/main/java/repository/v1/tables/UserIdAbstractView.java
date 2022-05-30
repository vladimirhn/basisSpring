package repository.v1.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kpersistence.v1.mapping.annotations.CurrentUserId;

public abstract class UserIdAbstractView extends AbstractView {

    @CurrentUserId
    @JsonIgnore
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
