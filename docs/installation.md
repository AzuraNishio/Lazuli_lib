# Instalation

How to include lazuliLib in your mod?

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
> • For 1.20 use v1.1.1-1.20.

---

## 3. Refresh Your Build

After saving, refresh and rebuild your project:

```bash
./gradlew --refresh-dependencies build
```

---



