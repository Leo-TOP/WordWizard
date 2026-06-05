# Codebase Index
> 2026-06-05 · 71 files · ~24.4k tokens total
>
> **How to use:** Read this file first. Navigate to the exact file you need,
> then read only that file. Do not read entire directories.

## Source

**src\main\java\wordwizard/**
- `App.java` — App, main

**src\main\java\wordwizard\businesslogic/**
- `CentralService.java` — CentralService, getWord, getWords, filterWords, getAllThemes, findByDefinition, findSimilar, addWord +1

**src\main\java\wordwizard\businesslogic\ai\promptbuilding/**
- `PromptBuilder.java` — PromptBuilder, buildThemeAssignmentPrompt, buildSingleWordPrompt
- `PromptObjectFormatter.java` — formatDefinition, PromptObjectFormatter

**src\main\java\wordwizard\businesslogic\ai\responseprocessing/**
- `AIResponseProcessor.java` — AIResponseProcessor, parseBatchResponse, parseSingleResponse

**src\main\java\wordwizard\businesslogic\ai\responseprocessing\dto/**
- `BatchThemeResponse.java` — BatchThemeResponse
- `SingleThemeResponse.java` — SingleThemeResponse
- `ThemeAssignment.java` — ThemeAssignment

**src\main\java\wordwizard\businesslogic\clioutput/**
- `OutputFormatter.java` — OutputFormatter

**src\main\java\wordwizard\businesslogic\dtomapping/**
- `DtoMapper.java` — DtoMapper, mapToWord

**src\main\java\wordwizard\businesslogic\dtomapping\aimapping/**
- `AIResponseDtoMapper.java` — AIResponseDtoMapper, toAssignments, toAssignment
- `AssignmentInput.java` — AssignmentInput

**src\main\java\wordwizard\businesslogic\embeddingapplication/**
- `EmbeddingService.java` — EmbeddingService

**src\main\java\wordwizard\businesslogic\export/**
- `ExportService.java` — ExportService, export

**src\main\java\wordwizard\businesslogic\export\dto/**
- `ExportRequest.java` — ExportRequest

**src\main\java\wordwizard\businesslogic\export\exporters/**
- `DocxExporter.java` — DocxExporter, getFormat
- `ExcelExporter.java` — ExcelExporter, getFormat
- `Exporter.java` — Exporter
- `ExporterFactory.java` — ExporterFactory, getExporter
- `MarkdownExporter.java` — MarkdownExporter, getFormat
- `TextExporter.java` — TextExporter, getFormat

**src\main\java\wordwizard\businesslogic\outputprocessing/**
- `OutputFileProcessor.java` — OutputFileProcessor, process
- `OutputProcessor.java` — OutputProcessor, process
- `WordProcessor.java` — WordProcessor, process

**src\main\java\wordwizard\businesslogic\themesprocessing/**
- `ThemesService.java` — ThemesService, assignThemesToWords, assignThemeToDefinition

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
- `WordNotFoundException.java` — WordNotFoundException

**src\main\java\wordwizard\repository\aiclients/**
- `GeminiChatClient.java` — GeminiChatClient, generateContent

**src\main\java\wordwizard\repository\createembeddings/**
- `EmbeddingFactory.java` — EmbeddingFactory

**src\main\java\wordwizard\repository\database/**
- `DatabaseManager.java` — DatabaseManager, findAllThemes, getAllWords, getOrCreateTheme, assignThemeToDefinition, findWordsForExport

**src\main\java\wordwizard\repository\database\repos/**
- `DefinitionRepository.java` — DefinitionRepository
- `JdbcDefinitionRepository.java` — JdbcDefinitionRepository, save, updateEmbedding, findByWordId, findByEmbedding, findSimilarTo
- `JdbcThemeRepository.java` — JdbcThemeRepository, findAll, findByName, getOrCreate, assignToDefinition
- `JdbcWordRepository.java` — JdbcWordRepository, findByName, findByNames, findFiltered, findAll, exists, save, delete
- `ThemeRepository.java` — ThemeRepository
- `WordRepository.java` — WordRepository

**src\main\java\wordwizard\repository\entities/**
- `Definition.java` — Definition, create, withEmbedding
- `SimilarWord.java` — SimilarWord
- `Theme.java` — Theme, create
- `Word.java` — Word, create

**src\main\java\wordwizard\repository\externalsourcefetching/**
- `DictionaryApiClient.java` — DictionaryApiClient, fetchWord

**src\main\java\wordwizard\repository\externalsourcefetching\dto/**
- `ApiDefinition.java` — ApiDefinition
- `DictionaryApiResponse.java` — DictionaryApiResponse
- `Meaning.java` — Meaning

**src\main\java\wordwizard\util/**
- `FileUtil.java` — FileUtil, readResource, writeBinaryToFile

**src\test\java\wordwizard/**
- `AppTest.java` — AppTest

## Config
- `.claude\settings.json`
- `docker\docker-compose.text.yml`
- `docker\docker-compose.yml`
- `src\main\resources\application.yaml`

## Docs
- `CLAUDE.md`
- `CODEBASE_INDEX.md`
- `README.md`

---
*Index: ~1.4k tokens · Full codebase: ~24.4k tokens · Saves ~94%*
