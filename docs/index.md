# Lazuli Library

Welcome to the Lazuli Library documentation.

This page will guide you through installing the library via **JitPack** so you can use it in your mod.

LazuliLib is currently compatible with fabric mods on 1.20 or above.

## Using JitPack

1. Add the JitPack repository to the `repositories` block in your `build.gradle`:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

2. Include Lazuli Library as a dependency. Replace `VERSION` with the desired release tag or commit hash:

```gradle
dependencies {
    modImplementation 'com.github.AzuraNishio:Lazuli_lib:VERSION'
}
```


After adding those entrys, rebuild the gradle.
