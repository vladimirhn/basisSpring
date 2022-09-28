package rest.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import kpersistence.tables.UserIdStringIdTable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class JsonNonNullUserIdStringIdTable extends UserIdStringIdTable {
}
