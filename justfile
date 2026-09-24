# HoloEasy — common tasks
# https://github.com/casey/just

set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

# Prefer JAVA_HOME if it has javac; otherwise pick a JDK from ~/.jdks or JetBrains JBR.
# System java-*-openjdk on Fedora is often a JRE-only install (no compiler).
export JAVA_HOME := `bash -c '
if [ -n "${JAVA_HOME:-}" ] && [ -x "${JAVA_HOME}/bin/javac" ]; then
  echo "$JAVA_HOME"
elif [ -x "$HOME/.jdks/ms-25.0.4.1-1/bin/javac" ]; then
  echo "$HOME/.jdks/ms-25.0.4.1-1"
elif jdk=$(ls -d "$HOME"/.jdks/ms-25* "$HOME"/.jdks/graalvm-ce-25* 2>/dev/null | head -1) && [ -x "$jdk/bin/javac" ]; then
  echo "$jdk"
elif [ -x "$HOME/.local/share/JetBrains/Toolbox/apps/intellij-idea/jbr/bin/javac" ]; then
  echo "$HOME/.local/share/JetBrains/Toolbox/apps/intellij-idea/jbr"
else
  dirname "$(dirname "$(readlink -f "$(command -v java)")")"
fi
'`

gradle := "./gradlew"

default:
    @just --list

# Show which JDK will be used for Gradle
java:
    @echo "JAVA_HOME=$JAVA_HOME"
    @"$JAVA_HOME/bin/java" -version
    @"$JAVA_HOME/bin/javac" -version

# Build the shaded Java example plugin jar (drop into plugins/)
example: example-java

# Build holoeasy-example-java shadow jar
example-java:
    {{gradle}} :holoeasy-example-java:shadowJar
    @echo ""
    @echo "Jar:"
    @ls -1 holoeasy-example-java/build/libs/*-shadow.jar

# Build holoeasy-example-kotlin shadow jar
example-kotlin:
    {{gradle}} :holoeasy-example-kotlin:shadowJar
    @echo ""
    @echo "Jar:"
    @ls -1 holoeasy-example-kotlin/build/libs/*-shadow.jar

# Compile the core library only
core:
    {{gradle}} :holoeasy-core:build

# Clean all Gradle outputs
clean:
    {{gradle}} clean

# Install docs deps and start VitePress locally
docs-dev:
    npm install
    npm run docs:dev

# Build the static docs site
docs-build:
    npm install
    npm run docs:build
