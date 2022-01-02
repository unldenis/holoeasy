# Hologram-Lib
Asynchronous, high-performance Minecraft Hologram library for 1.8-1.18 servers.
## Requirements
This library can only be used on spigot servers higher or on version 1.8.8. The plugin <a href="https://www.spigotmc.org/resources/protocollib.1997/">ProtocolLib</a> is required on your server.
## How to use
Add the repository and dependency to your plugin:
Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
<dependency>
    <groupId>com.github.unldenis</groupId>
    <artifactId>Hologram-Lib</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
Gradle
```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
dependencies {
    implementation 'com.github.unldenis:Hologram-Lib:master-SNAPSHOT'
}
```
