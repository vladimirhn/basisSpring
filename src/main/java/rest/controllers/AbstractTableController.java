package rest.controllers;

import kjson.OneStringJson;
import kmodels.IdLabelWithParentList;
import kpersistence.domain.Tables;
import kpersistence.query.KFilter;
import repository.tables.StringIdTable;
import rest.dictionary.DictionaryService;
import service.AbstractTableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rest.response.tables.TableDataResponse;

import java.util.List;
import java.util.Map;

public abstract class AbstractTableController<T extends StringIdTable, F extends KFilter> {

    protected abstract AbstractTableService<T> getService();
    protected abstract DictionaryService getDictionaryService();

    @GetMapping("/get_all")
    public TableDataResponse<T> getAll() {
        TableDataResponse<T> result = getAllTranslatedResponse(getService().selectAll());
        return result;
    }

    @PostMapping("/get_filtered")
    public TableDataResponse<T> getFiltered(@RequestBody Map<String, String> filters) {
        TableDataResponse<T> result = getAllTranslatedResponse(getService().selectFiltered(filters));
        return result;
    }

    protected TableDataResponse<T> getAllTranslatedResponse(List<T> data) {
        return new TableDataResponse<>(data, getDictionaryService());
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

    @GetMapping("/get_labels")
    public Map<String, Object> getLabels() {
        Map<String, Object> result = getService().selectIdToLabelsMap();
        return result;
    }
    @GetMapping("/get_hierarchical_labels")
    public IdLabelWithParentList getHierarchicalLabels() {
        IdLabelWithParentList result = getService().getHierarchicalLabels(null);
        return result;
    }
    @PostMapping("/get_filtered_hierarchical_labels")
    public IdLabelWithParentList getFilteredHierarchicalLabels(@RequestBody OneStringJson tableName) {
        IdLabelWithParentList result = getService().getHierarchicalLabels(Tables.getModelClassByName(tableName.getValue()));
        return result;
    }
}
