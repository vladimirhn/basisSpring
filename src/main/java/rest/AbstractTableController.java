package rest;

import repository.tables.StringIdTable;
import rest.dictionary.DictionaryService;
import service.AbstractTableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rest.response.TableDataResponse;

public abstract class AbstractTableController<T extends StringIdTable> {

    protected abstract AbstractTableService<T> getService();
    protected abstract DictionaryService getDictionaryService();

    @GetMapping("/get_all")
    public TableDataResponse<T> getAll() {
        return new TableDataResponse<>(getService().selectAll(), getDictionaryService());
    }

    @PostMapping("/add")
    public void add(@RequestBody T data) {
        getService().insert(data);
    }
    @PostMapping("/update")
    public void update(@RequestBody T data) {
        getService().update(data);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable(value = "id") String id) {
        getService().delete(id);
    }
}
