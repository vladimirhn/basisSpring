package rest.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import kcollections.KList;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntryTransferData {

    String id;
    String name;
    BigDecimal qty;

    Integer intData0;

    Double realData0;

    String stringData0;

    LocalDate dateData0;

    KList<EntryTransferData> subList0;

    EntryTransferData() {}

    public EntryTransferData(String id) {
        this.id = id;
    }

    public EntryTransferData(String id, String name) {
        this(id);
        this.name = name;
    }

    public EntryTransferData(String id, String name, BigDecimal qty) {
        this(id, name);
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getIntData0() {
        return intData0;
    }

    public void setIntData0(Integer intData0) {
        this.intData0 = intData0;
    }

    public Double getRealData0() {
        return realData0;
    }

    public void setRealData0(Double realData0) {
        this.realData0 = realData0;
    }

    public String getStringData0() {
        return stringData0;
    }

    public void setStringData0(String stringData0) {
        this.stringData0 = stringData0;
    }

    public LocalDate getDateData0() {
        return dateData0;
    }

    public void setDateData0(LocalDate dateData0) {
        this.dateData0 = dateData0;
    }

    public KList<EntryTransferData> getSubList0() {
        return subList0;
    }

    public void setSubList0(KList<EntryTransferData> subList0) {
        this.subList0 = subList0;
    }
}
