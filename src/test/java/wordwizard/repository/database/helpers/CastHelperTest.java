package wordwizard.repository.database.helpers;

import org.junit.jupiter.api.Test;
import wordwizard.exceptions.DataMappingException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CastHelperTest {

    @Test
    void toLongHandlesNumbersAndNull() {
        assertEquals(5L, CastHelper.toLong(5L));
        assertEquals(5L, CastHelper.toLong(5));
        assertEquals(5L, CastHelper.toLong(new BigDecimal("5")));
        assertNull(CastHelper.toLong(null));
    }

    @Test
    void toLongRejectsNonNumericTypes() {
        DataMappingException e = assertThrows(DataMappingException.class,
                () -> CastHelper.toLong("5"));
        assertTrue(e.getMessage().contains("String"));
    }

    @Test
    void toLocalDateTimeHandlesTimestampAndNull() {
        LocalDateTime time = LocalDateTime.of(2026, 6, 10, 9, 30);
        assertEquals(time, CastHelper.toLocalDateTime(Timestamp.valueOf(time)));
        assertNull(CastHelper.toLocalDateTime(null));
    }

    @Test
    void toLocalDateTimeRejectsOtherTypes() {
        assertThrows(DataMappingException.class,
                () -> CastHelper.toLocalDateTime("2026-06-10"));
    }

    @Test
    void toPGvectorWrapsArray() {
        assertNotNull(CastHelper.toPGvector(new float[]{0.1f, 0.2f}));
    }
}
