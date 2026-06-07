# Codebase Index
> 2026-06-07 · 75 files · ~20.3k tokens total
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
- `FilterByCommand.java` — FilterByCommand, execute
- `GetByDefinitionsCommand.java` — GetByDefinitionsCommand, execute
- `GetCommand.java` — GetCommand, execute
- `SaveCommand.java` — SaveCommand, execute

**src\main\java\wordwizard\cli\exceptionhandling/**
- `ExceptionHandler.java` — ExceptionHandler

**src\main\java\wordwizard\config/**
- `AppConfig.java` — AppConfig, geminiClient, restTemplate, gson
- `DotenvConfig.java` — DotenvConfig

**src\main\java\wordwizard\exceptions/**
- `FileException.java` — FileException
- `InvalidFileExtensionException.java` — InvalidFileExtensionException
- `InvalidFormatException.java` — InvalidFormatException
- `InvalidWordRequestException.java` — InvalidWordRequestException
- `PermissionDeniedException.java` — PermissionDeniedException
- `WordNotFoundException.java` — WordNotFoundException

**src\main\java\wordwizard\infrastructure/**
- `GeminiChatClient.java` — GeminiChatClient, generateContent

**src\main\java\wordwizard\models/**
- `Definition.java` — Definition, create
- `SimilarWord.java` — SimilarWord
- `Theme.java` — Theme, create
- `Word.java` — Word, create

**src\main\java\wordwizard\repository\database/**
- `DatabaseManager.java` — DatabaseManager, findAllThemes, getAllWords, getOrCreateTheme, assignThemeToDefinition, findWordsWithFiltering, findByWord, saveWord +2

**src\main\java\wordwizard\repository\database\repos/**
- `JdbcDefinitionRepository.java` — JdbcDefinitionRepository, save, updateEmbedding, findByWordId, findByEmbedding, findSimilarTo
- `JdbcThemeRepository.java` — JdbcThemeRepository, findAll, findByName, getOrCreate, assignToDefinition
- `JdbcWordRepository.java` — JdbcWordRepository, findByName, findByNames, findFiltered, findAll, exists, save

**src\main\java\wordwizard\repository\externaldictionaries/**
- `ApiDtoMapper.java` — ApiDtoMapper, mapToWord
- `DictionaryApiClient.java` — DictionaryApiClient, fetchWord

**src\main\java\wordwizard\repository\externaldictionaries\dto/**
- `ApiDefinition.java` — ApiDefinition
- `DictionaryApiResponse.java` — DictionaryApiResponse
- `Meaning.java` — Meaning

**src\main\java\wordwizard\service/**
- `CentralService.java` — CentralService, getWord, getWords, getWordsFromUserDictionary, getAllThemes, findByDefinition, findSimilar, addWord +1

**src\main\java\wordwizard\service\ai\mapping/**
- `AIResponseDtoMapper.java` — AIResponseDtoMapper, toAssignments, toAssignment
- `AssignmentInput.java` — AssignmentInput

**src\main\java\wordwizard\service\ai\prompt/**
- `PromptBuilder.java` — PromptBuilder, buildThemeAssignmentPrompt, buildSingleWordPrompt
- `PromptObjectFormatter.java` — formatDefinition, PromptObjectFormatter

**src\main\java\wordwizard\service\ai\response/**
- `AIResponseProcessor.java` — AIResponseProcessor, parseBatchResponse, parseSingleResponse

**src\main\java\wordwizard\service\ai\response\dto/**
- `BatchThemeResponse.java` — BatchThemeResponse
- `SingleThemeResponse.java` — SingleThemeResponse
- `ThemeAssignment.java` — ThemeAssignment

**src\main\java\wordwizard\service\dictrequesting/**
- `DictionaryQueryService.java` — DictionaryQueryService, getWords, getAllThemes

**src\main\java\wordwizard\service\dictrequesting\dto/**
- `FilterRequest.java` — FilterRequest, isEmpty

**src\main\java\wordwizard\service\embedding/**
- `EmbeddingService.java` — EmbeddingService, generateAndSaveEmbeddings, getSimilarWords

**src\main\java\wordwizard\service\export/**
- `ExportFileProcessor.java` — ExportFileProcessor, validate, process
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

**src\main\java\wordwizard\service\save/**
- `UserWordRequest.java` — UserWordRequest

**src\main\java\wordwizard\service\similarword/**
- `SimilarWordService.java` — SimilarWordService, getSimilarForWord, getSimilarForDefinition

**src\main\java\wordwizard\service\similarword\dto/**
- `SimilarWordRequestForDefinition.java` — SimilarWordRequestForDefinition
- `SimilarWordRequestForWord.java` — SimilarWordRequestForWord

**src\main\java\wordwizard\service\themes/**
- `ThemesService.java` — ThemesService, assignThemesToWords, assignThemeToDefinition

**src\main\java\wordwizard\service\validation/**
- `PosValidator.java` — PosValidator, validatePos
- `WordValidator.java` — WordValidator, validateWord

**src\main\java\wordwizard\service\wordsfetching/**
- `WordsImportingService.java` — WordsImportingService, fetchAndSave

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
*Index: ~1.6k tokens · Full codebase: ~20.3k tokens · Saves ~92%*
