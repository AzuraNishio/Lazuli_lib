# LazuliShaderRegistry

`LazuliShaderRegistry` manages shaders and post‑processing effects used by Lazuli. Use it to register core shaders or to defer creation of post processors until the client is ready.

## Registering a core shader

```java
LazuliShaderRegistry.registerShader(
    "my_shader",
    "examplemod",
    VertexFormats.POSITION_COLOR
);
```

Registered shaders can later be retrieved with `getShader("my_shader")`.

## Registering a post‑processing shader

```java
LazuliShaderRegistry.registerPostProcessingShader("bloom", "examplemod");
```

Call `LazuliShaderRegistry.register()` once during client initialization to hook resize handling and finish setup.
