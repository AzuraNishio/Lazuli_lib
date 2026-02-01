# Installation

How to include LazuliLib in your mod?

---

## 1. Add the JitPack Repository

**Groovy** (`build.gradle`):

```groovy
repositories {
  maven { url = 'https://jitpack.io' }
}
```

**Kotlin** (`build.gradle.kts`):

```kotlin
repositories {
  maven("https://jitpack.io")
}
```

---

## 2. Add LazuliLib as a Dependency

Replace `VERSION` with a release tag, commit hash, or branch name:

**Groovy**
```groovy
dependencies {
  modImplementation 'com.github.AzuraNishio:LazuliLib:VERSION'
}
```
**Kotlin**
```kotlin
dependencies {
  modImplementation("com.github.AzuraNishio:LazuliLib:VERSION")
}
```

> **Tip:**  
> • Use `1.0+` to pick the latest `1.0.x` release (e.g., `1.0.2`).  
> • Use `master-SNAPSHOT` (or a full commit SHA) to grab the bleeding-edge build.  
> • For 1.20 use v1.1.2-1.20.

---

## 3. Enable Runtime Shader Generation

To allow LazuliLib to generate shader files at runtime, add the `lazuli_gen` folder as a resource directory:

**Groovy** (`build.gradle`):

```groovy
sourceSets {
    main {
        resources {
            srcDirs += ['src/main/lazuli_gen']
        }
    }
}
```

**Kotlin** (`build.gradle.kts`):

```kotlin
sourceSets {
    main {
        resources {
            srcDirs("src/main/lazuli_gen")
        }
    }
}
```

> **Note:** This step is required for LazuliLib's automatic shader generation feature. The `lazuli_gen` folder will be created automatically when you run your game in development mode.

---

## 4. Refresh Your Build

After saving, refresh and rebuild your project:

```bash
./gradlew --refresh-dependencies build
```

---

## You're All Set! 

LazuliLib is now installed and configured. You can start creating shaders directly in code with zero manual file setup!

Check out the [documentation](https://github.com/AzuraNishio/LazuliLib) for usage examples and API reference.