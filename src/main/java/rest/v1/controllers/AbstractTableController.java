package rest.v1.controllers;

import kmodels.IdLabelWithParentList;
import kpersistence.v1.domain.Tables;
import kpersistence.v1.query.KFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import kpersistence.v2.tables.StringIdTable;
import rest.v2.response.tables.TableDataResponse;
import service.v1.AbstractTableService;

import java.util.List;
import java.util.Map;

public abstract class AbstractTableController<T extends StringIdTable, F extends KFilter> {

    protected abstract AbstractTableService<T> getService();

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
        return new TableDataResponse<>(data);
    }

    @PostMapping("/insert")
    public void insert(@RequestBody T data) {
        getService().insert(data);
    }

    @PostMapping("/insert_all")
    public void insertAll(@RequestBody List<T> data) {
        for (T datum : data) {
            getService().insert(datum);
        }
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
