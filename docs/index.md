# Lazuli Library

Welcome to the Lazuli Library documentation.

This page will guide you through installing the library via **JitPack** so you can use it in your Gradle project.

## Using JitPack

1. Add the JitPack repository to the `repositories` block in your `build.gradle`:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

2. Include Lazuli Library as a dependency. Replace `USER` with the GitHub username that hosts this repository and `VERSION` with the desired release tag or commit hash:

```gradle
dependencies {
    modImplementation 'com.github.USER:Lazuli_lib:VERSION'
}
```

The library is published as a Fabric mod, so `modImplementation` is typically used, but `implementation` also works for non‑mod projects.

With these entries added, Gradle will fetch Lazuli Library from JitPack the next time you build your project.
