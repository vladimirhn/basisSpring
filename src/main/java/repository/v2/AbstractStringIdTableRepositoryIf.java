package repository.v2;

import kcollections.KList;
import kpersistence.v2.tables.StringIdTable;

public interface AbstractStringIdTableRepositoryIf<T extends StringIdTable> {

    KList<T> selectAll(String orderByFieldName, String direction);

    KList<T> selectAllLabels(String orderByFieldName, String direction);

    KList<T> selectFiltered(T data);

    KList<T> selectFilteredLabels(T data);

    String insert(T data);

    void update(T data);

    String delete(String id);
}
