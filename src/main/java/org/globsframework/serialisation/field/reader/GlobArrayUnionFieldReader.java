package org.globsframework.serialisation.field.reader;

import org.globsframework.core.metamodel.GlobTypeResolver;
import org.globsframework.core.metamodel.fields.GlobArrayUnionField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.serialisation.WireConstants;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.stream.CodedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GlobArrayUnionFieldReader implements FieldReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobArrayUnionFieldReader.class);
    private final Integer fieldNumber;
    private final GlobArrayUnionField field;
    private final GlobTypeIndexResolver resolver;

    public GlobArrayUnionFieldReader(Integer fieldNumber, GlobArrayUnionField field) {
        this.fieldNumber = fieldNumber;
        this.field = field;
        resolver = GlobTypeIndexResolver.from(field.getTargetTypes());
    }

    public void read(MutableGlob data, int tag, int tagWireType, CodedInputStream inputStream) {
        switch (tagWireType) {
            case WireConstants.Type.NULL:
                data.set(field, null);
                break;
            case WireConstants.Type.GLOB_UNION_ARRAY:
                int size = inputStream.readInt();
                Glob[] globs = new Glob[size];
                for (int index = 0; index < size; index++) {
                    final Glob glob = inputStream.readGlob(resolver);
                    globs[index] = glob;
                }
                data.set(field, globs);
                break;
            default:
                String message = "For " + field.getName() + " unexpected type " + tagWireType;
                LOGGER.error(message);
                inputStream.skipField(tag);
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}
