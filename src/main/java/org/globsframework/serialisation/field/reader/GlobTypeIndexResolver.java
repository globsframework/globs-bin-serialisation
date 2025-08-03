package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.model.Glob;
import org.globsframework.core.utils.collections.IntHashMap;
import org.globsframework.serialisation.model.GlobTypeNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

public interface GlobTypeIndexResolver {
    Logger log = LoggerFactory.getLogger(GlobTypeIndexResolver.class);

    GlobType fromIndex(int index);

    static GlobTypeIndexResolver from(GlobType... globType) {
        return from(Arrays.asList(globType));
    }

    static GlobTypeIndexResolver from(Collection<GlobType> globType) {
        IntHashMap<GlobType> types = new IntHashMap<>();
        for (GlobType type : globType) {
            final Glob annotation = type.findAnnotation(GlobTypeNumber.KEY);
            if (annotation != null) {
                types.put(annotation.get(GlobTypeNumber.globTypeNumber), type);
            }else {
                log.info("type {} is not serializable : missing GlobTypeNumber annotations", type.getName());
            }
        }
        return new GlobTypeIndexResolverImpl(types);
    }

}
