package lsdi.hf.smartCity.AccessControl.Converter;

import com.owlike.genson.Converter;
import com.owlike.genson.Context;
import com.owlike.genson.stream.ObjectWriter;
import com.owlike.genson.stream.ObjectReader;
import java.time.Instant;

public class InstantConverter implements Converter<Instant> {
    @Override
    public void serialize(Instant instant, ObjectWriter writer, Context ctx) {
        writer.writeString(instant.toString());
    }

    @Override
    public Instant deserialize(ObjectReader reader, Context ctx) {
        return Instant.parse(reader.valueAsString());
    }
}

