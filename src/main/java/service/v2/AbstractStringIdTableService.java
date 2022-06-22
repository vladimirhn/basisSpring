package service.v2;

import kcollections.KList;
import kpersistence.v2.tables.StringIdTable;
import repository.v2.AbstractStringIdTableRepository;
import repository.v2.AbstractStringIdTableRepositoryIf;
import repository.v2.ModelRepositoryMap;
import repository.v2.RepositoryInvocationHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

public class AbstractStringIdTableService <T extends StringIdTable> implements AbstractStringIdTableServiceIf<T> {

    protected Class<T> model;
    private AbstractStringIdTableRepositoryIf<T> repository;

    public AbstractStringIdTableService() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelServiceMap.data.put(model, this);
    }

    public AbstractStringIdTableService(Class<T> model) {
        this.model = model;
    }

    protected AbstractStringIdTableRepositoryIf<T> repository() {
        if (repository == null) {
            repository = (AbstractStringIdTableRepository<T>) ModelRepositoryMap.data.get(model);

            if (repository == null) {
                AbstractStringIdTableRepositoryIf proxyInstance = (AbstractStringIdTableRepositoryIf) Proxy.newProxyInstance(
                        getClass().getClassLoader(),
                        new Class[] { AbstractStringIdTableRepositoryIf.class },
                        new RepositoryInvocationHandler(new AbstractStringIdTableRepository<>(model)));
                repository = proxyInstance;
            }
        }
        return repository;
    }

    public KList<T> selectAll(String orderByFieldName, String direction) {
        return repository().selectAll(orderByFieldName, direction);
    }

    public KList<T> selectAllLabels(String orderByFieldName, String direction) {
        return repository().selectAllLabels(orderByFieldName, direction);
    }

    public KList<T> selectFiltered(T data) {
        return repository().selectFiltered(data);
    }

    public KList<T> selectFilteredLabels(T data) {
        return repository().selectFilteredLabels(data);
    }

    public String insert(T data) {
        return repository().insert(data);
    }

    public void update(T data) {
        repository().update(data);
    }

    public String delete(String id) {
        return repository().delete(id);
    }
}
