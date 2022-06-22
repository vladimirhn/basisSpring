package rest.v2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import kpersistence.v2.tables.StringIdTable;
import rest.v2.response.tables.TableDataResponse;
import service.v2.AbstractStringIdTableService;
import service.v2.AbstractStringIdTableServiceIf;
import service.v2.ModelServiceMap;
import service.v2.ServiceInvocationHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.List;

public abstract class AbstractStringIdTableController<T extends StringIdTable> {

    Class<T> model;
    AbstractStringIdTableServiceIf<T> service;

    public AbstractStringIdTableController() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private AbstractStringIdTableServiceIf<T> service() {
        if (service == null) {
            service = (AbstractStringIdTableService<T>) ModelServiceMap.data.get(model);

            if (service == null) {
                AbstractStringIdTableServiceIf proxyInstance = (AbstractStringIdTableServiceIf) Proxy.newProxyInstance(
                        getClass().getClassLoader(),
                        new Class[] { AbstractStringIdTableServiceIf.class },
                        new ServiceInvocationHandler(new AbstractStringIdTableService<>(model)));
                service = proxyInstance;
            }
        }
        return service;
    }

    @GetMapping("/get_all/{orderByFieldName}/{direction}")
    public TableDataResponse<T> selectAll(
            @PathVariable(value = "orderByFieldName") String orderByFieldName,
            @PathVariable(value = "direction") String direction) {
        TableDataResponse<T> result = new TableDataResponse<>(service().selectAll(orderByFieldName, direction));
        return result;
    }

    @GetMapping("/get_all_labels/{orderByFieldName}/{direction}")
    public TableDataResponse<T> selectAllMinimum(
            @PathVariable(value = "orderByFieldName") String orderByFieldName,
            @PathVariable(value = "direction") String direction) {
        TableDataResponse<T> result = new TableDataResponse<>(service().selectAllLabels(orderByFieldName, direction));
        return result;
    }

    @GetMapping("/get_all")
    public TableDataResponse<T> selectAll() {
        return selectAll(null, null);
    }

    @GetMapping("/get_all_labels")
    public TableDataResponse<T> selectAllMinimum() {
        return selectAllMinimum(null, null);
    }

    @PostMapping("/get_filtered")
    public TableDataResponse<T> selectFiltered(@RequestBody T data) {
        TableDataResponse<T> result = new TableDataResponse<>(service().selectFiltered(data));
        return result;
    }
    @PostMapping("/get_filtered_labels")
    public TableDataResponse<T> selectFilteredLabels(@RequestBody T data) {
        TableDataResponse<T> result = new TableDataResponse<>(service().selectFilteredLabels(data));
        return result;
    }

    @PostMapping("/insert")
    public void insert(@RequestBody T data) {
        service().insert(data);
    }

    @PostMapping("/insert_all")
    public void insertAll(@RequestBody List<T> data) {
        for (T datum : data) {
            service().insert(datum);
        }
    }

    @PostMapping("/update")
    public void update(@RequestBody T data) {
        service().update(data);
    }

    @GetMapping("/delete/{id}")
    public String update(@PathVariable(value = "id") String id) {
        return service().delete(id);
    }
}
