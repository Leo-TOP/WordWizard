package wordwizard.repository.database.helpers;

import com.pgvector.PGvector;
import wordwizard.exceptions.DataMappingException;

import java.time.LocalDateTime;

public final class CastHelper {
    private CastHelper() {}

    /* Database NULL stays null (e.g. def_id from a LEFT JOIN);
       a non-null value of an unexpected type is corruption and must fail loudly. */
    public static Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.longValue();
        throw new DataMappingException(
                "Cannot map database value of type " + value.getClass().getName() + " to Long: " + value);
    }

    public static LocalDateTime toLocalDateTime(Object value) {
        if (value == null) return null;
        if (value instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
        throw new DataMappingException(
                "Cannot map database value of type " + value.getClass().getName() + " to LocalDateTime: " + value);
    }

    public static PGvector toPGvector(float[] vector){
        return new PGvector(vector);
    }
}
