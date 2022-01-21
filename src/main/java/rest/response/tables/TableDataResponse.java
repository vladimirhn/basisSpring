package rest.response.tables;

import com.fasterxml.jackson.annotation.JsonProperty;
import kpersistence.mapping.annotations.Column;
import kpersistence.mapping.annotations.Filter;
import kpersistence.mapping.annotations.FilterType;
import kutils.ClassUtils;
import rest.dictionary.DictionaryService;
import rest.response.JsonTableResponse;
import rest.response.KResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class TableDataResponse<T> extends KResponse {

    DictionaryService dictionaryService;

    Class<?> type;

    List<T> data;
    List<String> properties = new LinkedList<>();
    List<String> trans = new LinkedList<>();
    Map<String, String> filters = new HashMap<>();

    public TableDataResponse(Set<T> data, DictionaryService dictionaryService) {
        this(new ArrayList<>(data), dictionaryService);
    }

    public TableDataResponse(List<T> data, DictionaryService dictionaryService) {

        this.dictionaryService = dictionaryService;

        if (data == null) data = new LinkedList<>();
        this.data = data.stream().filter(Objects::nonNull).collect(Collectors.toList());

        if (!data.isEmpty() && data.stream().anyMatch(Objects::nonNull)) {
            type = data.get(0).getClass();

            definePropertiesAndTranslations();
            defineFilters();
        }
    }

    private void definePropertiesAndTranslations() {

        for (Field field : ClassUtils.getFieldsUpToObject(type)) {
            boolean isWriteOnly =
                    field.isAnnotationPresent(JsonProperty.class)
                            && JsonProperty.Access.WRITE_ONLY.equals(field.getAnnotation(JsonProperty.class).access());

            boolean addToProperties =
                    !field.isAnnotationPresent(JsonTableResponse.class)
                            || field.getAnnotation(JsonTableResponse.class).addToProperties();

            if (!isWriteOnly && addToProperties) {

                properties.add(field.getName());

                String translation = null;

                if (field.isAnnotationPresent(Column.class)) {
                    Column columnData = field.getAnnotation(Column.class);
                    translation = columnData.rus();
                    if (translation.equals("")) {
                        translation = dictionaryService.russian(field.getName());
                    }
                } else {
                    translation = dictionaryService.russian(field.getName());
                }
                trans.add(translation == null ? field.getName() : translation);
            }
        }

    }

    private void defineFilters() {
        for (Field field : ClassUtils.getFieldsUpToObject(type)) {
            if (field.isAnnotationPresent(Filter.class)) {
                Filter filterAnnotation = field.getAnnotation(Filter.class);

                if (FilterType.DATE.equals(filterAnnotation.type())) {
                    filters.put(field.getName(), filterAnnotation.type().toString());
                } else {
                    filters.put(field.getName(), filterAnnotation.endPoint());
                }
            }
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public List<String> getTrans() {
        return trans;
    }

    public void setTrans(List<String> trans) {
        this.trans = trans;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }
}
