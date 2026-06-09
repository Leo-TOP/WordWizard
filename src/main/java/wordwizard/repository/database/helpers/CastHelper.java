package wordwizard.repository.database.helpers;

import com.pgvector.PGvector;

import java.time.LocalDateTime;

public final class CastHelper {
    private CastHelper() {}

    public static Long toLong(Object value) {
        if (value instanceof Long l) return l;
        if (value instanceof Integer i) return i.longValue();
        return null;
    }

    public static LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
        return null;
    }

    public static PGvector toPGvector(float[] vector){
        return new PGvector(vector);
    }
}
