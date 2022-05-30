package rest.v1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import repository.v1.tables.AbstractView;
import rest.v2.response.tables.TableDataResponse;
import service.v1.AbstractViewService;

public abstract class AbstractViewController<T extends AbstractView> {

    protected abstract AbstractViewService<T> getService();

    @GetMapping("/get_all")
    public TableDataResponse<T> getAll() {
        TableDataResponse response =new TableDataResponse<>(getService().selectAll());
        return response;
    }
}