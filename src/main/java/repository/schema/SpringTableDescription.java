package repository.schema;

import kpersistence.modelsMaster.schema.ColumnDescription;
import kpersistence.modelsMaster.schema.TableDescription;
import kutils.ClassUtils;
import rest.schema.ModelsEndpointsSchema;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpringTableDescription {

    Class<?> klass;
    String endpoint;
    Map<String, SpringColumnDescription> desc = new LinkedHashMap<>();

    public SpringTableDescription(TableDescription tableDescription) {

        klass = tableDescription.getKlass();
        endpoint = ModelsEndpointsSchema.getSchema("rest").get(klass);

        for (Map.Entry<String, ColumnDescription> entry : tableDescription.getColumnDescriptions().entrySet()) {
            desc.put(entry.getKey(), ClassUtils.createInstanceByExample(entry.getValue(), SpringColumnDescription.class));
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Map<String, SpringColumnDescription> getDesc() {
        return desc;
    }
}
