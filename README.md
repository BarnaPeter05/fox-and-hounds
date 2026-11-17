# Fox and Hounds

Parancssoros róka–kutyák táblajáték (Java 21, Maven). A build- és kódstílus-beállítások a 10. heti Sudoku mintából
származnak, a csomagszerkezet saját.

## Indítás

```
mvn clean verify
java -jar target/fox-and-hounds-1.0-SNAPSHOT-jar-with-dependencies.jar
```

`mvn clean verify` lefuttatja a teszteket, a JaCoCo 80%-os sorlefedettségi küszöbét és a Checkstyle-t
(utóbbi egyelőre csak figyelmeztet).

## Szerkezet (`src/main/java`)

| Csomag | Tartalom | Mikor készül |
|---|---|---|
| (gyökér) `Main` | belépési pont | folyamatosan |
| `model` | `Position`, `Direction`, `Board` (immutable + Builder), `Figure`, `Fox`, `Hound`, `Move`, később `GamePhase`, `GameState` | 1–4. nap |
| `config` | `GameConfig` (Singleton) | kész |
| `event`, `event.listener` | Observer: `GameEventPublisher`, listenerek | kész |
| `businesslogic` | `BoardService`: szabályok, lépések, győzelem | 5–8. nap |
| `command` | `Command`, `CommandRegistry`, `GameEngine` és a parancsok | 9–14. nap |
| `persistence` | `DbInit`, `GameRepository` | 15. nap |

A JaCoCo kizárásai a `pom.xml`-ben: `Main`, `model`, `config`, `event`. A 80%-os küszöböt tehát a `businesslogic`,
a `command` és a `persistence` tesztjeinek kell hozniuk.
