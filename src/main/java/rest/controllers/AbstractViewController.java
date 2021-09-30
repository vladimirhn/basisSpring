package rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import repository.tables.AbstractView;
import rest.dictionary.DictionaryService;
import rest.response.TableDataResponse;
import service.AbstractViewService;

public abstract class AbstractViewController<T extends AbstractView> {

    protected abstract AbstractViewService<T> getService();
    protected abstract DictionaryService getDictionaryService();

    @GetMapping("/get_all")
    public TableDataResponse<T> getAll() {
        return new TableDataResponse<>(getService().selectAll(), getDictionaryService());
    }
}
