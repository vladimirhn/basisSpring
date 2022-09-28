package rest.v2.controllers;

import kpersistence.tables.StringIdTable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rest.v2.response.tables.TableDataResponse;
import service.v2.AbstractStringIdTableService;
import service.v2.ModelServiceMap;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Transactional
public abstract class AbstractStringIdTableController<T extends StringIdTable> {

    Class<T> model;
    AbstractStringIdTableService<T> service;

    public AbstractStringIdTableController() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected AbstractStringIdTableService<T> service() {
        if (service == null) {
            service = (AbstractStringIdTableService<T>) ModelServiceMap.data.get(model);

            if (service == null) {
                service = new AbstractStringIdTableService<>(model);
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
    public String delete(@PathVariable(value = "id") String id) {
        return service().delete(id);
    }
}
