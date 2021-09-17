package repository.tables;

import java.util.ArrayList;
import java.util.List;

public interface MainTable {

    List<? extends SubTable> getSubTableData();

    default List<? extends SubTable> getSafeSubTableData() {
        if (getSubTableData() == null) return new ArrayList<>();
        return getSubTableData();
    }
}
