# Folder Structure

Standard Maven layout, package-by-feature under `com.library`. Rationale
for each top-level piece:

```
library-management-system/
├── pom.xml
├── README.md
├── docs/
│   ├── ARCHITECTURE.md
│   └── FOLDER_STRUCTURE.md
└── src/
    ├── main/java/com/library/
    │   ├── book/            # Book/BookCopy catalog model, repository, service
    │   ├── patron/          # Patron model, repository, service
    │   ├── lending/         # Checkout/return transaction model, repository, service
    │   ├── search/          # SearchStrategy implementations + search service
    │   ├── notification/    # LendingEventListener implementations (Observer)
    │   ├── common/          # Domain exceptions shared across packages
    │   └── app/             # Main.java — runnable demo entry point
    └── test/java/com/library/
        ├── book/
        ├── patron/
        ├── lending/
        └── search/
```

- **`book/`, `patron/`, `lending/`** — one package per core entity from the
  brief, each holding its model, repository interface + in-memory
  implementation, and service interface + implementation. Keeping
  repository/service pairs together (rather than splitting into
  `model/`, `repository/`, `service/` top-level packages) means opening one
  package tells you everything about that entity's behavior — relevant for
  a reviewer skimming the repo.
- **`search/`** — separate from `book/` because it's a cross-cutting
  concern (search *of* books, not book domain logic itself) and holds the
  Strategy pattern implementations.
- **`notification/`** — the Observer pattern implementations, separate from
  `lending/` because listeners are meant to be swappable/extensible without
  touching the lending domain logic that raises the events.
- **`common/`** — domain exceptions only. Not a dumping ground for
  unrelated utilities.
- **`app/`** — the one package allowed to depend on all the others; keeps
  the demo/wiring code out of the domain packages.
- **`src/test/java`** mirrors `src/main/java` package-for-package, standard
  Maven convention.

No `resources/`, `config/`, or `util/` package — nothing in this project
needs them, and adding them speculatively would be exactly the kind of
premature structure `docs/ARCHITECTURE.md` argues against.
