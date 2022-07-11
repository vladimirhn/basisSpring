package repository.v2.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import kpersistence.v2.modelsMaster.schema.ColumnDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpringColumnDescription extends ColumnDescription {
}
