package repository.tables;

import kpersistence.mapping.annotations.Column;
import kpersistence.mapping.annotations.Id;

public abstract class TypoTable {

    @Id
    @Column(name = "ID")
    private String id;

    public abstract void setDefaults();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
