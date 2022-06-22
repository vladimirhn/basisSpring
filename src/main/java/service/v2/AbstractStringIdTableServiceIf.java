package service.v2;

import kcollections.KList;

public interface AbstractStringIdTableServiceIf<T> {

    KList<T> selectAll(String orderByFieldName, String direction);

    KList<T> selectAllLabels(String orderByFieldName, String direction);

    KList<T> selectFiltered(T data);

    KList<T> selectFilteredLabels(T data);

    String insert(T data);

    void update(T data);

    String delete(String id);
}
