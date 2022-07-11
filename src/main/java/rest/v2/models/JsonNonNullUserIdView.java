package rest.v2.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import kpersistence.v2.tables.UserIdView;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class JsonNonNullUserIdView extends UserIdView {
}
