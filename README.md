# paint3d-visitor-fsm-parser

## Overview

This repository contains a JavaFX paint application built around a classic **model-view-controller** structure, plus coursework artifacts for an assignment focused on drawing tools, file persistence, and parser design. The application lets a user draw multiple shape types on a canvas and includes the beginnings of a parser-driven load workflow for a custom text-based paint save format.

Although the repository name references a visitor/FSM parser theme, the code currently centers on:

- **MVC separation** for UI, input handling, and drawing state
- **Observer-based repainting** between drawing commands and the canvas
- **Strategy + factory selection** for shape-specific mouse behavior
- **File save/load infrastructure** for a custom Paint Save File format
- **JUnit parser tests** that describe the intended parser behavior

## Project Status

This project appears to be an academic assignment snapshot rather than a fully productized application.

Current state of the codebase:

- The drawing UI is present and wired for **Circle**, **Rectangle**, **Squiggle**, and **Polyline**
- Saving support exists through `PaintFileSaver`
- Loading support is scaffolded through `PaintFileParser`
- The parser implementation is **incomplete** and still contains assignment placeholders such as `ADD CODE` and `ADD MORE`
- Some serialization code does not yet fully match the documented save-file specification
- Maven is configured for **Java 22**

## Main Features

### Drawing tools

The UI exposes four drawing modes:

- **Circle** — click and drag to set center and radius
- **Rectangle** — click and drag between two corners
- **Squiggle** — press and drag to sketch freehand strokes
- **Polyline** — left-click to add segments, right-click to finish

### File operations

The File menu supports:

- **New** — create a fresh `PaintModel`
- **Open** — choose a file and attempt to parse it into a model
- **Save** — write the current model to disk
- **Exit** — close the application

### Parser-oriented assignment work

The repository includes:

- a formal save-file specification
- example save files
- parser tests covering valid and invalid cases
- scrum and backlog notes from the assignment workflow

## Technology Stack

- **Language:** Java
- **UI:** JavaFX
- **Build tool:** Maven Wrapper (`./mvnw`)
- **Testing:** JUnit 5
- **Module system:** Java Platform Module System (`module-info.java`)

## Architecture

### 1. Entry point

- `a3/src/main/java/ca/utoronto/utm/paint/Paint.java`

Launches the JavaFX application and creates a new `View` backed by a fresh `PaintModel`.

### 2. Model layer

Located under `a3/src/main/java/ca/utoronto/utm/paint/model`.

Key classes:

- `PaintModel` — stores the list of drawing commands and notifies observers when state changes
- `PaintCommand` — abstract base class for drawable commands
- `CircleCommand`
- `RectangleCommand`
- `SquiggleCommand`
- `PolylineCommand`
- `Point`

Responsibilities:

- maintain shape state
- render shapes onto a JavaFX `GraphicsContext`
- notify the UI when command data changes
- convert commands into a text representation for saving

### 3. View layer

Located under `a3/src/main/java/ca/utoronto/utm/paint/view`.

Key classes:

- `View` — assembles the main window
- `PaintPanel` — canvas container that repaints when the model changes
- `ShapeChooserPanel` — button panel for selecting the active drawing tool

Responsibilities:

- construct the JavaFX scene graph
- host the menu bar and shape selector
- repaint the canvas when the model updates

### 4. Controller layer

Located under `a3/src/main/java/ca/utoronto/utm/paint/controller`.

Key classes:

- `MenuBarController` — handles File menu commands
- `PaintPanelController` — forwards mouse events to the active strategy
- `ShapeChooserPanelController` — swaps drawing strategies based on button clicks
- `ShapeManipulatorFactory` — creates tool-specific strategies
- `CircleManipulatorStrategy`
- `RectangleManipulatorStrategy`
- `SquiggleManipulatorStrategy`
- `PolylineManipulatorStrategy`

Responsibilities:

- map UI events to domain actions
- isolate drawing behavior per shape type
- coordinate tool changes without overloading the view

### 5. Persistence layer

Located under `a3/src/main/java/ca/utoronto/utm/paint/persistence`.

Key classes:

- `PaintFileSaver` — writes the model to disk
- `PaintFileParser` — intended to parse the custom save format using regexes and a finite-state-machine style approach

Supporting docs:

- `a3/src/main/resources/paintSaveFileFormat.txt`
- `a3/src/main/resources/paintSaveFileExample.txt`

## Design Patterns Used

The repository demonstrates several common OO patterns:

- **Model-View-Controller (MVC)** — clean separation between rendering, state, and event handling
- **Observer** — `PaintModel` and `PaintCommand` notify the UI about updates
- **Strategy** — each shape has its own mouse interaction behavior
- **Factory** — the active drawing strategy is created through `ShapeManipulatorFactory`
- **FSM-style parsing** — the parser is structured around parser states and regex matching

## Repository Layout

```text
.
├── README.md
└── a3
    ├── pom.xml
    ├── mvnw / mvnw.cmd
    ├── scrum
    │   ├── productBacklog.txt
    │   ├── sprintBacklog.txt
    │   ├── sprint2Backlog.txt
    │   ├── sprint3Backlog.txt
    │   └── dailyScrumMeeting.txt
    └── src
        ├── main
        │   ├── java
        │   │   ├── ca/utoronto/utm/paint
        │   │   ├── ca/utoronto/utm/paint/controller
        │   │   ├── ca/utoronto/utm/paint/model
        │   │   ├── ca/utoronto/utm/paint/persistence
        │   │   └── ca/utoronto/utm/paint/view
        │   └── resources
        └── test
            ├── java/ca/utoronto/utm/paint/PaintFileParserTest.java
            └── resources/samplefiles
```

## Getting Started

### Prerequisites

- **JDK 22**
- a system capable of running **JavaFX**

### Build

From the project module directory:

```bash
cd /tmp/workspace/mufferio/paint3d-visitor-fsm-parser/a3
./mvnw compile
```

### Test

```bash
cd /tmp/workspace/mufferio/paint3d-visitor-fsm-parser/a3
./mvnw test
```

### Run

The application entry point is:

- `ca.utoronto.utm.paint.Paint`

In practice, the most reliable way to run the app in its current state is from an IDE configured for JavaFX and Java 22.

## Validation Notes

During documentation work, the existing Maven validation commands were executed in the repository environment:

- `./mvnw -q -DskipTests compile`
- `./mvnw test`

Both failed in this environment with:

```text
error: invalid target release: 22
```

That indicates the local environment did not provide Java 22, not necessarily that the repository is misconfigured for its intended setup.

## Paint Save File Format

The project uses a custom plain-text format for persistence.

High-level rules from the included specification:

- file starts with `PaintSaveFileVersion1.0`
- file ends with `EndPaintSaveFile`
- blank lines and whitespace are intended to be ignored by the parser
- shapes may appear in any order
- supported shapes are:
  - `Circle`
  - `Rectangle`
  - `Squiggle`
  - `Polyline`

Each shape has a structured block with required properties in a fixed order.

Examples:

- Circle stores `color`, `filled`, `center`, and `radius`
- Rectangle stores `color`, `filled`, `p1`, and `p2`
- Squiggle and Polyline store `color`, `filled`, and a `points` block containing repeated `point:(x,y)` lines

See:

- `/tmp/workspace/mufferio/paint3d-visitor-fsm-parser/a3/src/main/resources/paintSaveFileFormat.txt`
- `/tmp/workspace/mufferio/paint3d-visitor-fsm-parser/a3/src/main/resources/paintSaveFileExample.txt`

## Tests

The automated tests focus on the parser:

- `PaintFileParserTest` exercises valid and invalid save files
- sample input files live under `a3/src/test/resources/samplefiles`
- test cases cover:
  - minimal valid files
  - spacing tolerance
  - single and multiple circles
  - rectangles
  - squiggles
  - multiple mixed shapes
  - malformed ordering and missing terminators

The tests are especially useful because they effectively define the expected parser contract, even though the parser implementation is not finished.

## Known Gaps and Limitations

- `PaintFileParser` is incomplete and still contains assignment placeholder comments
- The save/load pipeline is not fully aligned end to end
- `PaintModel#getPaintFileString()` currently emits a header/footer format that differs from the formal parser specification
- The JavaFX Maven plugin configuration references `HelloApplication`, which does not exist in the current source tree
- No top-level license file is present in the repository

## Assignment / Coursework Artifacts

The `a3/scrum` directory contains planning and progress notes, including:

- product backlog
- sprint backlogs
- daily scrum notes

These files indicate the repository was developed as part of a structured assignment process covering:

- environment setup
- polyline support
- save support
- load/parser work
- architectural improvements

## Recommended Next Steps

If this repository is going to be continued beyond its current assignment state, the most important follow-up items are:

1. finish the finite-state parser implementation
2. align serialization output with the formal save-file specification
3. fix the JavaFX Maven plugin `mainClass` configuration
4. confirm Java 22 setup in contributor documentation
5. add screenshots or a demo GIF for the drawing workflow
6. add a license if the project is intended for wider reuse

## Summary

`paint3d-visitor-fsm-parser` is a JavaFX paint application and parser assignment repository that combines interactive drawing, MVC-based UI design, command-style shape models, and a custom text serialization format. Its drawing layer is present and understandable, its test suite documents the intended parser behavior, and its current gaps are mainly in persistence completeness and final project polish.
