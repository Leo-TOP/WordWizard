# Codebase Index
> 2026-06-04 · 53 files · ~13.0k tokens total
>
> **How to use:** Read this file first. Navigate to the exact file you need,
> then read only that file. Do not read entire directories.

## Source

**src\main\java\wordwizard/**
- `App.java` — App, main

**src\main\java\wordwizard\businesslogic/**
- `WordWizardService.java` — WordWizardService, getWord, getWords, filterWords, getAllThemes, findByDefinition, findSimilar, addWord +1

**src\main\java\wordwizard\businesslogic\dtomapping/**
- `DtoMapper.java` — DtoMapper, mapToWord

**src\main\java\wordwizard\businesslogic\embeddingapplication/**
- `EmbeddingService.java` — EmbeddingService

**src\main\java\wordwizard\businesslogic\export/**
- `ExportService.java` — ExportService

**src\main\java\wordwizard\businesslogic\export\exporters/**
- `DocxExporter.java` — DocxExporter, export
- `Exporter.java` — Exporter, export
- `MarkdownExporter.java` — MarkdownExporter, export

**src\main\java\wordwizard\businesslogic\outputprocessing/**
- `OutputFileProcessor.java` — OutputFileProcessor, process
- `Processor.java` — Processor, process
- `WordProcessor.java` — WordProcessor, process

**src\main\java\wordwizard\businesslogic\themes/**
- `ThemesService.java` — ThemesService

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

**src\main\java\wordwizard\repository\ai/**
- `AIQueryFormer.java` — AIQueryFormer
- `GeminiChatClient.java` — GeminiChatClient, generateContent

**src\main\java\wordwizard\repository\createembeddings/**
- `EmbeddingFactory.java` — EmbeddingFactory

**src\main\java\wordwizard\repository\database/**
- `DatabaseManager.java`
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
- `FileUtil.java` — FileUtil, readFromFile, writeToFile

**src\test\java\wordwizard/**
- `AppTest.java` — AppTest

## Config
- `docker-compose.yml`
- `src\main\resources\application.yaml`

## Docs
- `CODEBASE_INDEX.md`
- `README.md`

---
*Index: ~1.0k tokens · Full codebase: ~13.0k tokens · Saves ~92%*
