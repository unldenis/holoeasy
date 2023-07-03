# Hologram-Lib
[![](https://jitpack.io/v/unldenis/Hologram-Lib.svg)](https://jitpack.io/#unldenis/Hologram-Lib) <br>
Asynchronous, high-performance Minecraft Hologram library for 1.8-1.20.1 servers.
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
    <version>2.4.0</version>
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
Add ProtocolLib as dependency to your plugin.yml. It could look like this:
```yml
name: Hub
version: 1.0-SNAPSHOT
api-version: "1.13"
depend: [ProtocolLib]
author: unldenis
```
## Line solutions in HologramLib
|                       	| Animation 	| Hologram Integration 	| Clickable  	|
|-----------------------	|-----------	|----------------------	|------------	|
| [Line]()              	|           	|                      	|            	|
| [TextLine]()          	|           	| ✅                    	| ✅(w/ Pool) 	|
| [TextALine]()         	| ✅         	| ✅                    	|            	|
| [ClickableTextLine]() 	|           	| ✅                    	| ✅          	|
| [ItemLine]()          	|           	| ✅                    	|            	|
| [ItemALine]()         	| ✅         	| ✅                    	|            	|

These are the lines provided by the library, thanks to the composition of various parts. The library is structured so that you too can create your own custom hologram. 

To be integrated into a Hologram composed of multiple lines you need to implement ILine.
## Support
The libraries that integrate Hologram-Lib
- <a href="https://github.com/unldenis/NPC-Lib/tree/hologramlib-integration">unldenis/NPC-Lib</a>
## Example usage
```java
  public Hologram firstExample(Location loc) {
    // create new line structure (armorstand)
    Line line = new Line(plugin);
    // compose an TextLine hologram
    TextLine textLine = new TextLine(line, "Hello", placeholders, true);

    // create new line structure (armorstand)
    Line line2 = new Line(plugin);
    // compose this second TextLine hologram
    TextLine textLine2 = new TextLine(line2, "%%player%%", placeholders, true);

    // append to hologram that will make all the hard work for you
    // the TextSequentialLoader loader will load lines(text only) one after the other. It is an experimental function.
    Hologram hologram = new Hologram(plugin, loc, new TextSequentialLoader());
    // remember to call this method or hologram will not be visible
    hologram.load(textLine, textLine2);

    // add hologram to pool
    pool.takeCareOf(hologram);

    return hologram;
    }
```
Click [here](https://github.com/unldenis/Hologram-Lib/blob/master/src/test/java/com/github/unldenis/hologram/test/HologramLibExample.java) if you want to see the full guided example.
## Preview
https://user-images.githubusercontent.com/80055679/147889286-6d4006a0-677b-4066-a285-08e79d3fad9e.mp4
#### Placeholder Preview
![2022-01-03_22 11 34](https://user-images.githubusercontent.com/80055679/147980899-fa7b8172-b0d8-4ab6-9eab-d33e9323fb63.png)
#### Interact Preview
![2022-06-12_18 39 23](https://user-images.githubusercontent.com/80055679/173243893-0f5568d4-c667-4311-b5ab-35d19ccc18e4.png)
