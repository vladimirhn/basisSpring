package rest.v2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import kpersistence.v2.tables.StringIdTable;
import rest.v2.response.tables.TableDataResponse;
import service.v2.AbstractStringIdTableService;
import service.v2.ModelServiceMap;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractStringIdTableController<T extends StringIdTable> {

    Class<T> model;
    AbstractStringIdTableService<T> service;

    public AbstractStringIdTableController() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private AbstractStringIdTableService<T> service() {
        if (service == null) {
            service = (AbstractStringIdTableService<T>) ModelServiceMap.data.get(model);
        }
        return service;
    }

    @GetMapping("/select_all")
    public TableDataResponse<T> selectAll() {
        TableDataResponse<T> result = new TableDataResponse<>(service().selectAll());
        return result;
    }

    @PostMapping("/select_filtered")
    public TableDataResponse<T> selectFiltered(@RequestBody T data) {
        TableDataResponse<T> result = new TableDataResponse<>(service().selectFiltered(data));
        return result;
    }

    @PostMapping("/insert")
    public void insert(@RequestBody T data) {
        service().insert(data);
    }

    @PostMapping("/update")
    public void update(@RequestBody T data) {
        service().update(data);
    }

    @PostMapping("/delete")
    public String update(@RequestBody String id) {
        return service().delete(id);
    }
}
