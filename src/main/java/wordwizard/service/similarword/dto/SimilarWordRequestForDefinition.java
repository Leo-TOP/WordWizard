package wordwizard.service.similarword.dto;

import javax.validation.constraints.Min;

public record SimilarWordRequestForDefinition(String definition,
                                              @Min(1) Integer limit) {}
