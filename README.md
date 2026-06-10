# 🧙 WordWizard

> *Your Personal AI‑Powered Dictionary Builder*

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot 3">
  <img src="https://img.shields.io/badge/PostgreSQL-pgvector-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL + pgvector">
  <img src="https://img.shields.io/badge/Gemini-AI-purple?style=for-the-badge&logo=googlegemini" alt="Google Gemini AI">
  <img src="https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker" alt="Docker">
</p>

---

## ✨ What Is This?

**WordWizard** is a terminal‑based dictionary that grows with you. Look up any word, and it fetches real definitions from the internet. But it doesn't stop there – it **understands** your words using AI, automatically organising them into themes like *"programming"*, *"science"*, or *"emotions"*.

Search by meaning, not just by word. Export your personal dictionary to beautiful files. Build a vocabulary that's truly yours.

---

## 🚀 Features

| | |
|---|---|
| 🔍 | **Smart Lookups** – Fetch definitions from the Free Dictionary API and cache them locally. Repeat lookups are instant and work offline. |
| 🧠 | **AI Themes** – Google Gemini automatically groups your words into themes. Each definition is themed exactly once – no duplicate API calls. |
| 🎯 | **Search by Meaning** – Describe a concept and find the matching word using vector embeddings. |
| 🔗 | **Similar Words** – Discover words with similar meanings to any word in your dictionary. |
| ✏️ | **Add Your Own** – Build your vocabulary with custom definitions. Duplicate detection keeps it clean. |
| 📊 | **Statistics** – Word counts, theme distributions, recent activity – know your dictionary. |
| 📁 | **Export** – Markdown, plain text, Excel, or DOCX. Grouped by theme for easy navigation. |
| 🎨 | **Clean CLI** – Friendly terminal interface with built‑in help and graceful error messages. |

---

## 🛠️ Tech Stack

| Tool | Purpose |
|------|---------|
| ☕ **Java 21** | Core language |
| 🌱 **Spring Boot 3** | Application framework & DI |
| 🐘 **PostgreSQL + pgvector** | Database with vector similarity search (HNSW index) |
| 🧠 **Google Gemini 2.5 Flash** | AI theme assignment |
| 🤖 **ONNX (all‑MiniLM‑L6‑v2)** | Local embedding generation – your definitions never leave your machine |
| 📄 **Apache POI** | Excel & DOCX export |
| 🧪 **JUnit 5 + Testcontainers** | Integration tests against a real PostgreSQL |
| 🐳 **Docker Compose** | One‑command setup |

---

## 🏁 Quick Start

### You'll need
- 🐳 Docker & Docker Compose
- 🔑 A [Google Gemini API key](https://aistudio.google.com/apikey) *(free tier works!)*

### Setup

```bash
# 1. Clone the repo
git clone https://github.com/Leo-TOP/WordWizard.git
cd WordWizard

# 2. Create your .env file and add your Gemini API key
cp .env.example .env

# 3. Build and start everything (database + app)
docker compose up -d --build

# 4. Open an interactive session
docker compose run --rm app
```

Type `help` to see all commands. To leave the CLI without stopping it, detach with `Ctrl‑P` then `Ctrl‑Q` (plain `Ctrl‑C` kills the session).

```bash
# Stop everything
docker compose down

# Start it again later (no rebuild needed)
docker compose up -d
```

### Try it out

```text
get-word serendipity        # look up a word and save it
add catnap "a short sleep"  # add your own definition
similar serendipity         # find words with similar meanings
find-by-def "lucky find"    # search by meaning
themes                      # see your AI-assigned themes
stats                       # dictionary statistics
export --output vocab.md    # export your dictionary
```

### Running locally instead (for development)

Start only the database in Docker and run the app from your IDE — no environment
configuration needed, the app reads `.env` by itself:

```bash
docker compose up -d database
./gradlew run
```

> **Note:** the Gemini API is not available in every region. If theme assignment
> fails with *"User location is not supported"*, use a system‑wide VPN. Words are
> still saved without themes and get themed automatically on a later lookup.

---

## 📜 License

Released under the [MIT License](LICENSE.md).
