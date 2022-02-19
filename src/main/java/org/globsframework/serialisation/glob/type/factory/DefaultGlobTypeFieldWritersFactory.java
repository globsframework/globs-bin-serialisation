package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.BinWriter;
import org.globsframework.serialisation.field.FieldWriter;
import org.globsframework.serialisation.field.writer.FieldWriterVisitorCreator;
import org.globsframework.serialisation.field.writer.NullFieldWriter;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.model.FieldNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultGlobTypeFieldWritersFactory implements GlobTypeFieldWritersFactory {
    private final BinWriter binWriter;

    public DefaultGlobTypeFieldWritersFactory(BinWriter binWriter) {
        this.binWriter = binWriter;
    }

    public GlobTypeFieldWriters create(GlobType type) {
        Field[] fields = type.getFields();
        List<FieldWriter> fieldWriters = new ArrayList<>();

        for (Field field : fields) {
            field.findOptAnnotation(FieldNumber.key)
                    .map(FieldNumber.fieldNumber)
                    .ifPresent(fieldNumber -> field.safeVisit(
                            new FieldWriterVisitorCreator(binWriter, fieldNumber, fieldWriters)
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
