package wordwizard.businesslogic.export.exporters;

import wordwizard.repository.entities.Word;

import java.util.List;

public interface Exporter {
    byte[] export(List<Word> words);

    String getFormat();
}
