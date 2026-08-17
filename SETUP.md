# Setup Guide

Step-by-step environment setup for building and running the project locally.

## Prerequisites

- **Java 25** (the project targets this LTS release - see `pom.xml`)
- **Maven 3.8+**
- **Git**

## 1. Install Java 25

Check what you already have first:

```
java -version
```

If that's missing or shows something older than 25:

**macOS (Homebrew):**
```
brew install --cask temurin
```
(Run `brew search temurin` first if you want a version-pinned formula like
`temurin@25` instead, e.g. to keep an older JDK around for other projects.)

**Windows/Linux:** download a JDK 25 build from
[Adoptium](https://adoptium.net) or your package manager
(`apt install openjdk-25-jdk` on Debian/Ubuntu).

If you have multiple JDKs installed and the wrong one is active:
```
/usr/libexec/java_home -V      # macOS: list installed JDKs
export JAVA_HOME=$(/usr/libexec/java_home -v 25)
```

## 2. Install Maven

```
brew install maven          # macOS
```
or see [maven.apache.org/install.html](https://maven.apache.org/install.html)
for other platforms. Verify with:
```
mvn -version
```
It should report both a recent Maven version and Java 25 in the output.

## 3. Get the code

```
git clone https://github.com/<your-username>/library-management-system.git
cd library-management-system
```

## 4. Build

```
mvn clean package
```

This compiles the code and runs the test suite; the runnable jar ends up
at `target/library-management-system.jar`.

## 5. Run it

```
java -jar target/library-management-system.jar
```

This starts the console menu (see `README.md` for what each option does).

## 6. Run the tests on their own

```
mvn test
```

## Configuration / environment variables

None needed. There's no database, no external API, and no secrets - the
whole app is in-memory for the life of one run, so there's nothing to put
in a `.env` file or set as an environment variable.

## Troubleshooting

- **`zsh: command not found: mvn`** - Maven isn't installed; see step 2.
- **`release version 25 not supported`** - your active `java` is older than
  25. Run `java -version` to check, then see the `JAVA_HOME` fix in step 1.
- **IDE import** - open the folder in IntelliJ or Eclipse and import as an
  existing Maven project; both will pick up `pom.xml` automatically.
