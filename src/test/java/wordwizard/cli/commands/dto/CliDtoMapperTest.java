package wordwizard.cli.commands.dto;

import org.junit.jupiter.api.Test;
import wordwizard.exceptions.CommandValidationException;
import wordwizard.service.export.dto.ExportRequest;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.service.similarword.dto.SimilarWordRequestForDefinition;
import wordwizard.service.similarword.dto.SimilarWordRequestForWord;
import wordwizard.service.wordsfetching.dto.WordRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CliDtoMapperTest {

    private final CliDtoMapper mapper = new CliDtoMapper();

    @Test
    void toWordRequestMapsArgAndOptions() {
        WordRequest request = mapper.toWordRequest(new CommandContext(
                List.of("cat"), Map.of("limit", "3", "pos", "noun")));

        assertEquals("cat", request.word());
        assertEquals(3, request.limit());
        assertEquals("noun", request.partOfSpeech());
    }

    @Test
    void toWordRequestWithoutArgThrows() {
        assertThrows(CommandValidationException.class,
                () -> mapper.toWordRequest(new CommandContext(List.of(), Map.of())));
    }

    @Test
    void toFilterRequestReadsAllOptions() {
        FilterRequest request = mapper.toFilterRequest(new CommandContext(
                List.of(), Map.of("theme", "animals", "pos", "noun", "starts-with", "ca")));

        assertEquals("animals", request.theme());
        assertEquals("noun", request.partOfSpeech());
        assertEquals("ca", request.startsWith());
    }

    @Test
    void toUserWordRequestsBuildsPairs() {
        List<UserWordRequest> requests = mapper.toUserWordRequests(new CommandContext(
                List.of("cat", "a small animal", "dog", "a loyal animal"), Map.of()));

        assertEquals(2, requests.size());
        assertEquals(new UserWordRequest("cat", "a small animal"), requests.getFirst());
    }

    @Test
    void toUserWordRequestsRejectsOddArgCount() {
        CommandValidationException e = assertThrows(CommandValidationException.class,
                () -> mapper.toUserWordRequests(new CommandContext(
                        List.of("cat", "a small animal", "dog"), Map.of())));
        assertTrue(e.getMessage().contains("odd"));
    }

    @Test
    void toExportRequestDefaultsToMarkdown() {
        ExportRequest request = mapper.toExportRequest(new CommandContext(
                List.of(), Map.of("output", "out.md")));

        assertEquals("markdown", request.format());
        assertEquals("out.md", request.outputPath());
    }

    @Test
    void toSimilarRequestsMapArguments() {
        SimilarWordRequestForWord byWord = mapper.toSimilarByWordRequest(new CommandContext(
                List.of("cat"), Map.of("limit", "7")));
        assertEquals("cat", byWord.word());
        assertEquals(7, byWord.limit());

        SimilarWordRequestForDefinition byDef = mapper.toSimilarByDefinitionRequest(new CommandContext(
                List.of("a", "small", "animal"), Map.of()));
        assertEquals("a small animal", byDef.definition());
        assertNull(byDef.limit());
    }
}
