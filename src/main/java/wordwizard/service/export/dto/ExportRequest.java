package wordwizard.service.export.dto;

import wordwizard.service.dictrequesting.dto.FilterRequest;

public record ExportRequest(
        FilterRequest filterRequest,
        String format,
        String outputPath
) {}