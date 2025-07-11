# LazuliRenderingRegistry

`LazuliRenderingRegistry` is the main entry point for hooking render callbacks. It exposes methods for registering custom rendering logic and post‑render hooks.

## Registering callbacks

```java
LazuliRenderingRegistry.registerRenderCallback((context, viewProj, tickDelta) -> {
    // draw your geometry here
});

LazuliRenderingRegistry.registerPostCallback((context, viewProj, tickDelta) -> {
    // post processing or overlay rendering
});
```

Callbacks are executed each frame during `WorldRenderEvents.LAST`, with convenience access to the camera matrix and tick delta.
