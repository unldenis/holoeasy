# Example plugins

The HoloEasy repository ships two ready-to-run example plugins. Use them as a reference while you learn.

## Where to look

| Module | Language | What it shows |
|---|---|---|
| `holoeasy-example-java` | Java | PacketEvents setup, pool, clickable Display + Text lines |
| `holoeasy-example-kotlin` | Kotlin | Same ideas in Kotlin |

Key files in the Java example:

- `ExamplePlugin.java` — lifecycle, pool, command, click listener
- `MyCounterHolo.java` — hologram with Display text, per-player text, interaction line

## What the counter example does

1. On enable, starts an interactive pool
2. `/hologram` (or the registered command) spawns a `MyCounterHolo` at your feet into the pool
3. Clicking the interaction line increments counters
4. Global Display text updates for everyone; the armor-stand line updates for the clicker

![Counter hologram preview](/preview/MyCounterHolo.gif)

Other previews from the project:

![Classic hologram demo](/preview/gif.gif)

![Block line and animations](/preview/blockline-and-animations.gif)

## How to run an example

From the repo root:

```bash
just example          # Java shaded plugin jar
just example-kotlin   # Kotlin shaded plugin jar
```

Or with Gradle directly: `./gradlew :holoeasy-example-java:shadowJar`

Then put the `*-shadow.jar` from `holoeasy-example-*/build/libs/` on a test Paper/Spigot server and try the command registered in `plugin.yml`.

Reading the example source after finishing [Your first hologram](./first-hologram) is the fastest way to connect the concepts.
