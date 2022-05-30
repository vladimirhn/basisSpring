package rest.v2.response.tables;

import rest.v2.response.KResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TableDataResponse<T> extends KResponse {

    List<T> data;

    public TableDataResponse(Set<T> data) {
        this(new ArrayList<>(data));
    }

    public TableDataResponse(List<T> data) {

        if (data == null) data = new ArrayList<>();
        this.data = data.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
