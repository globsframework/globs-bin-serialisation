package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.field.writer.FieldWriterVisitorCreator;
import org.globsframework.serialisation.field.writer.NullFieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.model.FieldNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultGlobTypeFieldWritersFactory implements GlobTypeFieldWritersFactory {

    public DefaultGlobTypeFieldWritersFactory() {
    }

    public GlobTypeFieldWriters create(GlobType type) {
        Field[] fields = type.getFields();
        List<FieldWriter> fieldWriters = new ArrayList<>();

        for (Field field : fields) {
            field.findOptAnnotation(FieldNumber.KEY)
                    .map(FieldNumber.fieldNumber)
                    .ifPresent(fieldNumber -> field.safeAccept(
                            new FieldWriterVisitorCreator(fieldNumber, fieldWriters)
                    ));
        }

        int maxLen = fieldWriters.stream()
                .map(FieldWriter::getFieldNumber)
                .max(Integer::compareTo)
                .orElse(0);

        FieldWriter[] realFieldWriters = new FieldWriter[maxLen + 1];
        Arrays.fill(realFieldWriters, NullFieldWriter.INSTANCE);

        for (FieldWriter fieldWriter : fieldWriters) {
            realFieldWriters[fieldWriter.getFieldNumber()] = fieldWriter;
        }

        return new GlobTypeFieldWriters(realFieldWriters);
    }

}
