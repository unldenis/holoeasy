## Requirements

The prerequisites:
- Java 8
- ProtocolLib / experimental PacketEvents

> If you opt for PacketEvents, the dependency is not included by HoloEasy and neither is the instance registration.
More information in the [example](https://github.com/unldenis/holoeasy/tree/master/holoeasy-example-packetevents).

## Installation

*Learn how to install and integrate HoloEasy in your projects.*

### Maven

**1.** Add the JitPack repository to your build file.

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

<br />

**2.** Add the core dependency.

```xml
<dependency>
    <groupId>com.github.unldenis.holoeasy</groupId>
    <artifactId>holoeasy-core</artifactId>
    <version>4.3.2</version>
</dependency>
```

<br />

**3.** For  **java** plugins include also the kotlin stdlib.

```xml
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-stdlib</artifactId>
    <version>1.9.21</version>
</dependency>
```

<br />

**4.** Once you’re done, you may now proceed creating your first hologram.

### Gradle

**1.** Add the JitPack repository to your build file.

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

<br />

**2.** Add the core dependency.

```groovy
implementation 'com.github.unldenis.holoeasy:holoeasy-core:4.3.2'
```

<br />

**3.** For **java** plugins include also the kotlin stdlib.

```groovy
implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.9.21'
```

<br />

**4.** Once you’re done, you may now proceed creating your first hologram.