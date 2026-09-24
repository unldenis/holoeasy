# Requirements

Before you install HoloEasy, make sure you have these pieces ready.

## 1. A Spigot / Paper server

HoloEasy runs on **Spigot**, **Paper**, and similar forks.

Supported range (roughly):

- Legacy holograms (armor stands): **1.8+**
- Display-entity lines: **1.19.4+**
- Clickable interaction lines: **1.19.4+**

You develop a **plugin** that loads on that server. HoloEasy is not a standalone plugin you drop in `plugins/` by itself — it is a library your plugin depends on.

## 2. A Java plugin project

You should already have a plugin project that builds with **Gradle** or **Maven**, with a main class extending `JavaPlugin` (or the Kotlin equivalent).

If you do not have a plugin yet, create a basic one first (any Spigot plugin tutorial is fine), then come back here.

## 3. PacketEvents

HoloEasy does **not** ship PacketEvents inside the jar.

[PacketEvents](https://docs.packetevents.com/) is a separate library that sends and receives Minecraft packets. HoloEasy uses it under the hood.

You must:

1. Add PacketEvents as a dependency (or shade it into your plugin)
2. Create and initialize the PacketEvents API in your plugin lifecycle

We show the exact steps in [Installation](./installation).

## 4. Build tools

| Tool | Used for |
|---|---|
| JDK 8+ | Compiling your plugin (HoloEasy itself targets Java 8) |
| Gradle or Maven | Downloading HoloEasy from JitPack |
| A server jar | Testing in-game |

## Checklist

- [ ] Spigot/Paper server to test on
- [ ] Plugin project that already loads on that server
- [ ] Willing to add PacketEvents + HoloEasy as dependencies

Next: [Installation](./installation)
