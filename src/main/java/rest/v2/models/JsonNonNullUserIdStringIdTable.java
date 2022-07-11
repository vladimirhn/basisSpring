package rest.v2.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import kpersistence.v2.tables.UserIdStringIdTable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class JsonNonNullUserIdStringIdTable extends UserIdStringIdTable {
}
