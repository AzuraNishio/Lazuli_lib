# LazuliVertex

`LazuliVertex` is a small utility class that provides a fluent API for building vertex data. It stores position, texture coordinates, color, light, overlay, and normal information which is later consumed by `LazuliBufferBuilder`.

```java
LazuliVertex vertex = new LazuliVertex()
        .pos(x, y, z)
        .uv(u, v)
        .color(r, g, b, a)
        .light(packedLight)
        .overlay(packedOverlay)
        .normal(nx, ny, nz);
```

Use `copy()` to duplicate a vertex or `getPos()`/`getNormal()` to retrieve the stored vectors.
