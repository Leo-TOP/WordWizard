# Codebase Index
> 2026-06-09 · 89 files · ~27.1k tokens total
>
> **How to use:** Read this file first. Navigate to the exact file you need,
> then read only that file. Do not read entire directories.

## Source

**src\main\java\wordwizard/**
- `App.java` — App, main

**src\main\java\wordwizard\cli/**
- `CliEngine.java` — CliEngine, run

**src\main\java\wordwizard\cli\commands/**
- `Command.java` — Command, execute
- `ExportCommand.java` — ExportCommand, execute
- `GetFromDictionaryCommand.java` — GetFromDictionaryCommand, execute
- `GetSimilarByDefinitionsCommand.java` — GetSimilarByDefinitionsCommand, execute
- `GetSimilarByWordCommand.java` — GetSimilarByWordCommand, execute
- `GetStatisticsCommand.java` — GetStatisticsCommand, execute
- `GetWordCommand.java` — GetWordCommand, execute
- `GetWordsCommand.java` — GetWordsCommand, execute
- `SaveCommand.java` — SaveCommand, execute

**src\main\java\wordwizard\cli\exceptionhandling/**
- `ExceptionHandler.java` — ExceptionHandler

**src\main\java\wordwizard\config/**
- `AppConfig.java` — AppConfig, geminiGenerationConfig, geminiClient, restTemplate, gson, wordImportExecutor
- `DotenvConfig.java` — DotenvConfig

**src\main\java\wordwizard\exceptions/**
- `FileException.java` — FileException
- `InvalidDefinitionException.java` — InvalidDefinitionException
- `InvalidFileExtensionException.java` — InvalidFileExtensionException
- `InvalidFormatException.java` — InvalidFormatException
- `InvalidStartsWithException.java` — InvalidStartsWithException
- `InvalidWordException.java` — InvalidWordException
- `PermissionDeniedException.java` — PermissionDeniedException
- `WordNotFoundException.java` — WordNotFoundException

**src\main\java\wordwizard\infrastructure/**
- `GeminiChatClient.java` — GeminiChatClient, generateContent

**src\main\java\wordwizard\models/**
- `Definition.java` — Definition, create, createWithoutId, createWithoutExampleAndId
- `SimilarWord.java` — SimilarWord
- `Theme.java` — Theme
- `VocabularyStats.java` — VocabularyStats
- `Word.java` — Word, createWordWithoutId

**src\main\java\wordwizard\repository\database/**
- `DatabaseManager.java` — DatabaseManager, findAllThemes, getOrCreateTheme, assignThemeToDefinition, findByWord, saveWord, updateDefinitionEmbedding, getSimilarTo +6

**src\main\java\wordwizard\repository\database\helpers/**
- `CastHelper.java` — CastHelper, toLong, toLocalDateTime, toPGvector

**src\main\java\wordwizard\repository\database\helpers\accumulaterows/**
- `AccumulateHelper.java` — addDefinition, build
- `AccumulateUtil.java` — AccumulateUtil, accumulateRows, accumulateGroupedRows

**src\main\java\wordwizard\repository\database\repos/**
- `JdbcDefinitionRepository.java` — JdbcDefinitionRepository, save, updateEmbedding, findByWordId, findByEmbedding, findSimilarTo, findSimilarDefinition
- `JdbcStatisticsRepository.java` — JdbcStatisticsRepository, getStatistics
- `JdbcThemeRepository.java` — JdbcThemeRepository, findAll, findByName, getOrCreate, assignToDefinition
- `JdbcWordRepository.java` — JdbcWordRepository, findByName, findGroupedByTheme, save, findById, updateWordUpdatedAt

**src\main\java\wordwizard\repository\externaldictionaries/**
- `DictionaryApiClient.java` — DictionaryApiClient, fetchWord

**src\main\java\wordwizard\repository\externaldictionaries\dto/**
- `ApiDefinition.java` — ApiDefinition
- `ApiDtoMapper.java` — ApiDtoMapper, mapToWord
- `DictionaryApiResponse.java` — DictionaryApiResponse
- `Meaning.java` — Meaning

**src\main\java\wordwizard\service/**
- `CentralService.java` — CentralService, getWord, getWords, getWordsFromUserDictionary, getAllThemes, findByDefinition, findSimilar, addUserWords +2

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
- `DictionaryQueryService.java` — DictionaryQueryService, getWords, getAllThemes, getStatistics

**src\main\java\wordwizard\service\dictrequesting\dto/**
- `FilterRequest.java` — FilterRequest, isEmpty

**src\main\java\wordwizard\service\embedding/**
- `EmbeddingService.java` — EmbeddingService, generateAndSaveEmbeddingsForWord, generateAndSaveEmbeddingForDefinition

**src\main\java\wordwizard\service\export/**
- `ExportService.java` — ExportService, export

**src\main\java\wordwizard\service\export\dto/**
- `ExportRequest.java` — ExportRequest

**src\main\java\wordwizard\service\export\exporters/**
- `DocxExporter.java` — DocxExporter, getFormat
- `ExcelExporter.java` — ExcelExporter, getFormat
- `Exporter.java` — Exporter
- `ExporterFactory.java` — ExporterFactory, getExporter
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
- `TextValidator.java` — TextValidator, validateWords, validateDefinitions, validateStart

**src\main\java\wordwizard\service\validation\pos/**
- `PartOfSpeech.java` — PartOfSpeech, fromString
- `PosValidator.java` — PosValidator, validatePos

**src\main\java\wordwizard\service\wordsfetching/**
- `WordsImportingService.java` — WordsImportingService, fetchAndSaveWord, fetchAndSaveWords

**src\main\java\wordwizard\service\wordsfetching\dto/**
- `WordRequest.java` — WordRequest

**src\main\java\wordwizard\util/**
- `FileUtil.java` — FileUtil, readResource, writeBinaryToFile

**src\test\java\wordwizard/**
- `AppTest.java` — AppTest

## Config
- `docker\docker-compose.test.yml`
- `docker\docker-compose.yml`
- `src\main\resources\application.yaml`

## Docs
- `CODEBASE_INDEX.md`
- `README.md`

---
*Index: ~2.0k tokens · Full codebase: ~27.1k tokens · Saves ~93%*
