package wordwizard.service.export.exporters;

import wordwizard.models.Word;

import java.util.List;

public interface Exporter {
    byte[] export(List<Word> words);

    String getFormat();
}
