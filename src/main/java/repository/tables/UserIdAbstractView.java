package repository.tables;

import kpersistence.mapping.annotations.CurrentUserId;

public abstract class UserIdAbstractView extends AbstractView {

    @CurrentUserId
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
