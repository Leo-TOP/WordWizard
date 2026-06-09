package wordwizard.service.export.exporters;

import wordwizard.models.Word;

import java.util.List;
import java.util.Map;

public interface Exporter {
    byte[] export(Map<String, List<Word>> themedWords);

    String getFormat();
}
