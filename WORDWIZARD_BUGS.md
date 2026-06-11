# App Bugs

## Minor Inconsistencies

### Fixed but not yet included in Docker image

- Fixed statistics logic and output (`avgDefinitionsPerWord` is now a `long`).
- Fixed word count in export log message (previously counted duplicate words across themes).

### Not Fixed

- **Delayed file appearance after export** — the exported file may take a few seconds to appear in the output directory.
