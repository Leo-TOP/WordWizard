# Codebase Index
> 2026-06-10 · 137 files · ~63.7k tokens total
>
> **How to use:** Read this file first. Navigate to the exact file you need,
> then read only that file. Do not read entire directories.

## Source

**src\main\java\wordwizard/**
- `App.java` — App, main

**src\main\java\wordwizard\cli/**
- `CliEngine.java` — CliEngine, run

**src\main\java\wordwizard\cli\commands/**
- `Command.java` — Command
- `CommandRegistry.java` — CommandRegistry, getCommand

**src\main\java\wordwizard\cli\commands\dto/**
- `CliDtoMapper.java` — CliDtoMapper, toWordRequest, toFilterRequest, toUserWordRequests, toExportRequest, toSimilarByWordRequest, toSimilarByDefinitionRequest
- `CommandContext.java` — CommandContext

**src\main\java\wordwizard\cli\commands\servicerelated\nooption/**
- `GetAllThemesCommand.java` — GetAllThemesCommand, commandName, execute
- `GetStatisticsCommand.java` — GetStatisticsCommand, commandName, execute
- `GetWordsCommand.java` — GetWordsCommand, commandName, execute

**src\main\java\wordwizard\cli\commands\servicerelated\withoptions/**
- `ExportCommand.java` — ExportCommand, commandName, execute
- `FilterCommand.java` — FilterCommand, commandName, execute
- `GetSimilarByDefinitionCommand.java` — GetSimilarByDefinitionCommand, commandName, execute
- `GetSimilarByWordCommand.java` — GetSimilarByWordCommand, commandName, execute
- `GetWordCommand.java` — GetWordCommand, commandName, execute
- `SaveCommand.java` — SaveCommand, commandName, execute

**src\main\java\wordwizard\cli\commands\system/**
- `ExitCommand.java` — ExitCommand, commandName, execute
- `HelpCommand.java` — HelpCommand, commandName, execute

**src\main\java\wordwizard\cli\commands\validation/**
- `ParameterValidator.java` — ParameterValidator, requireArgCount, requireMinArgs, requirePairedArgs, parseAndValidateLimit, requireOption

**src\main\java\wordwizard\cli\parsing/**
- `InputParser.java` — InputParser, parse

**src\main\java\wordwizard\cli\parsing\dto/**
- `ParsedInput.java` — ParsedInput

**src\main\java\wordwizard\config/**
- `AppConfig.java` — AppConfig, geminiGenerationConfig, geminiClient, restTemplate, gson, wordImportExecutor
- `DotenvConfig.java` — DotenvConfig

**src\main\java\wordwizard\exceptions/**
- `AiResponseParseException.java` — AiResponseParseException
- `BatchImportException.java` — BatchImportException
- `CommandNotFoundException.java` — CommandNotFoundException
- `CommandValidationException.java` — CommandValidationException
- `DataMappingException.java` — DataMappingException
- `DictionaryApiException.java` — DictionaryApiException
- `EmbeddingException.java` — EmbeddingException
- `ExportException.java` — ExportException
- `FileException.java` — FileException
- `GeminiApiException.java` — GeminiApiException
- `InvalidDefinitionException.java` — InvalidDefinitionException
- `InvalidFileExtensionException.java` — InvalidFileExtensionException
- `InvalidFormatException.java` — InvalidFormatException
- `InvalidRequestException.java` — InvalidRequestException
- `InvalidStartsWithException.java` — InvalidStartsWithException
- `InvalidThemeException.java` — InvalidThemeException
- `InvalidWordException.java` — InvalidWordException
- `PermissionDeniedException.java` — PermissionDeniedException
- `ThemeAssignmentException.java` — ThemeAssignmentException
- `WordNotFoundException.java` — WordNotFoundException

**src\main\java\wordwizard\exceptions\exceptionhandling/**
- `GlobalExceptionHandler.java` — GlobalExceptionHandler, handle

**src\main\java\wordwizard\infrastructure/**
- `GeminiChatClient.java` — GeminiChatClient, generateContent

**src\main\java\wordwizard\models/**
- `Definition.java` — Definition, create, createWithoutId, createWithOnlyText
- `SimilarWord.java` — SimilarWord
- `Theme.java` — Theme
- `VocabularyStats.java` — VocabularyStats
- `Word.java` — Word, createWordWithoutId

**src\main\java\wordwizard\repository\database/**
- `DatabaseManager.java` — DatabaseManager, findAllThemes, getOrCreateTheme, assignThemeToDefinition, findByWord, saveWord, updateDefinitionEmbedding, getSimilarTo +2

**src\main\java\wordwizard\repository\database\helpers/**
- `CastHelper.java` — CastHelper, toLong, toLocalDateTime, toPGvector

**src\main\java\wordwizard\repository\database\helpers\accumulaterows/**
- `AccumulateHelper.java` — addDefinition, build
- `AccumulateUtil.java` — AccumulateUtil, accumulateRows, accumulateGroupedRows

**src\main\java\wordwizard\repository\database\repos/**
- `JdbcDefinitionRepository.java` — JdbcDefinitionRepository, save, updateEmbedding, findByWordId, findByEmbedding, findSimilarTo, findSimilarDefinition
- `JdbcStatisticsRepository.java` — JdbcStatisticsRepository, getStatistics
- `JdbcThemeRepository.java` — JdbcThemeRepository, findAll, findByName, getOrCreate, assignToDefinition
- `JdbcWordRepository.java` — JdbcWordRepository, findByName, findFilteredGroupedByTheme, save, findById, updateWordUpdatedAt

**src\main\java\wordwizard\repository\externaldictionaries/**
- `DictionaryApiClient.java` — DictionaryApiClient, fetchWord

**src\main\java\wordwizard\repository\externaldictionaries\dto/**
- `ApiDefinition.java` — ApiDefinition
- `ApiDtoMapper.java` — ApiDtoMapper, mapToWord
- `DictionaryApiResponse.java` — DictionaryApiResponse
- `Meaning.java` — Meaning

**src\main\java\wordwizard\service/**
- `CentralService.java` — CentralService, getWord, getWords, filter, getAllThemes, findByDefinition, findSimilar, addUserWords +2

**src\main\java\wordwizard\service\ai\mapping/**
- `AIResponseDtoMapper.java` — AIResponseDtoMapper, toAssignments
- `AssignmentInput.java` — AssignmentInput

**src\main\java\wordwizard\service\ai\prompt/**
- `PromptBuilder.java` — PromptBuilder, buildThemeAssignmentPrompt
- `PromptObjectFormatter.java` — PromptObjectFormatter

**src\main\java\wordwizard\service\ai\response/**
- `AIResponseProcessor.java` — AIResponseProcessor, parseBatchResponse

**src\main\java\wordwizard\service\ai\response\dto/**
- `BatchThemeResponse.java` — BatchThemeResponse
- `ThemeAssignment.java` — ThemeAssignment

**src\main\java\wordwizard\service\dictrequesting/**
- `DictionaryQueryService.java` — DictionaryQueryService, getFilteredWords, getAllThemes, getStatistics

**src\main\java\wordwizard\service\dictrequesting\dto/**
- `FilterRequest.java` — FilterRequest

**src\main\java\wordwizard\service\embedding/**
- `EmbeddingService.java` — EmbeddingService, generateAndSaveEmbeddingsForWord, generateAndSaveEmbeddingForDefinition

**src\main\java\wordwizard\service\export/**
- `ExporterFactory.java` — ExporterFactory, getExporter
- `ExportService.java` — ExportService, export

**src\main\java\wordwizard\service\export\dto/**
- `ExportRequest.java` — ExportRequest

**src\main\java\wordwizard\service\export\exporters/**
- `DocxExporter.java` — DocxExporter, getFormat
- `ExcelExporter.java` — ExcelExporter, getFormat
- `Exporter.java` — Exporter
- `MarkdownExporter.java` — MarkdownExporter, getFormat
- `TextExporter.java` — TextExporter, getFormat

**src\main\java\wordwizard\service\export\fileprocessing/**
- `ExportFileProcessor.java` — ExportFileProcessor, process
- `ExportFormat.java` — ExportFormat, supportsExtension, fromString

**src\main\java\wordwizard\service\save/**
- `SaveService.java` — SaveService, saveWords

**src\main\java\wordwizard\service\save\dto/**
- `SaveDtoMapping.java` — SaveDtoMapping, mapToWord, mapToDefinition
- `UserWordRequest.java` — UserWordRequest

**src\main\java\wordwizard\service\save\message/**
- `SaveResult.java` — SaveResult
- `SaveStatus.java` — SaveStatus

**src\main\java\wordwizard\service\similarword/**
- `SimilarityService.java` — SimilarityService, findSimilarToWord, findByDescription, isDuplicateDefinition

**src\main\java\wordwizard\service\similarword\dto/**
- `SimilarWordRequestForDefinition.java` — SimilarWordRequestForDefinition
- `SimilarWordRequestForWord.java` — SimilarWordRequestForWord

**src\main\java\wordwizard\service\themes/**
- `ThemesService.java` — ThemesService, assignThemesToWords

**src\main\java\wordwizard\service\validation/**
- `TextValidator.java` — TextValidator, validateWords, validateDefinitions, validateTheme, validateStart

**src\main\java\wordwizard\service\validation\pos/**
- `PartOfSpeech.java` — PartOfSpeech, fromString
- `PosValidator.java` — PosValidator, validatePos

**src\main\java\wordwizard\service\wordsfetching/**
- `WordsImportingService.java` — WordsImportingService, fetchAndSaveWord, fetchAndSaveWords

**src\main\java\wordwizard\service\wordsfetching\dto/**
- `WordRequest.java` — WordRequest

**src\main\java\wordwizard\util/**
- `FileUtil.java` — FileUtil, readResource, writeBinaryToFile

**src\test\java\wordwizard\cli/**
- `CliEngineTest.java` — CliEngineTest

**src\test\java\wordwizard\cli\commands/**
- `CommandRegistryTest.java` — CommandRegistryTest
- `CommandsTest.java` — CommandsTest

**src\test\java\wordwizard\cli\commands\dto/**
- `CliDtoMapperTest.java` — CliDtoMapperTest

**src\test\java\wordwizard\cli\commands\validation/**
- `ParameterValidatorTest.java` — ParameterValidatorTest

**src\test\java\wordwizard\cli\parsing/**
- `InputParserTest.java` — InputParserTest

**src\test\java\wordwizard\exceptions\exceptionhandling/**
- `GlobalExceptionHandlerTest.java` — GlobalExceptionHandlerTest

**src\test\java\wordwizard\integration/**
- `AbstractIntegrationTest.java` — AbstractIntegrationTest
- `ExportIntegrationTest.java` — ExportIntegrationTest
- `SaveWordsIntegrationTest.java` — SaveWordsIntegrationTest
- `SimilarityIntegrationTest.java` — SimilarityIntegrationTest
- `TestVectors.java` — TestVectors
- `ThemesAndStatsIntegrationTest.java` — ThemesAndStatsIntegrationTest
- `WordImportIntegrationTest.java` — WordImportIntegrationTest

**src\test\java\wordwizard\models/**
- `ModelsTest.java` — ModelsTest

**src\test\java\wordwizard\repository\database\helpers/**
- `CastHelperTest.java` — CastHelperTest

**src\test\java\wordwizard\repository\database\helpers\accumulaterows/**
- `AccumulateUtilTest.java` — AccumulateUtilTest

**src\test\java\wordwizard\repository\externaldictionaries\dto/**
- `ApiDtoMapperTest.java` — ApiDtoMapperTest

**src\test\java\wordwizard\service\ai\mapping/**
- `AIResponseDtoMapperTest.java` — AIResponseDtoMapperTest

**src\test\java\wordwizard\service\ai\prompt/**
- `PromptBuilderTest.java` — PromptBuilderTest

**src\test\java\wordwizard\service\ai\response/**
- `AIResponseProcessorTest.java` — AIResponseProcessorTest

**src\test\java\wordwizard\service\embedding/**
- `EmbeddingServiceTest.java` — EmbeddingServiceTest

**src\test\java\wordwizard\service\export/**
- `ExportUnitTest.java` — ExportUnitTest

**src\test\java\wordwizard\service\export\exporters/**
- `ExportersTest.java` — ExportersTest

**src\test\java\wordwizard\service\save\dto/**
- `SaveDtoMappingTest.java` — SaveDtoMappingTest

**src\test\java\wordwizard\service\validation/**
- `ValidatorsTest.java` — ValidatorsTest

**src\test\java\wordwizard\util/**
- `FileUtilTest.java` — FileUtilTest

## Config
- `docker-compose.test.yml`
- `docker-compose.yml`
- `src\main\resources\application.yaml`

## Docs
- `CODEBASE_INDEX.md`
- `README.md`

---
*Index: ~3.2k tokens · Full codebase: ~63.7k tokens · Saves ~95%*
