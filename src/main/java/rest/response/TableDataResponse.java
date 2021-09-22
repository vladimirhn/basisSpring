package rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kpersistence.mapping.annotations.Column;
import kutils.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import rest.dictionary.DictionaryService;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableDataResponse<T> {

    DictionaryService dictionaryService;

    List<T> data;
    List<String> properties = new LinkedList<>();
    List<String> trans = new LinkedList<>();

    public TableDataResponse(List<T> data, DictionaryService dictionaryService) {

        this.dictionaryService = dictionaryService;

        if (data == null) data = new LinkedList<>();
        this.data = data.stream().filter(Objects::nonNull).collect(Collectors.toList());

        definePropertiesAndTranslations();
    }

    private void definePropertiesAndTranslations() {
        if (!data.isEmpty() && data.stream().anyMatch(Objects::nonNull)) {
            Class<?> type = data.get(0).getClass();
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
}
