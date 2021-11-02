package repository.tables;

import kpersistence.mapping.annotations.CurrentUserId;

public abstract class UserIdStringIdTable extends StringIdTable {

    @CurrentUserId
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
