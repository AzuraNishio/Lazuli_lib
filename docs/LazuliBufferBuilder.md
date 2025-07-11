# LazuliBufferBuilder

`LazuliBufferBuilder` is a high level wrapper around Minecraft's `BufferBuilder`. It simplifies rendering by keeping track of the last used vertex attributes and by automatically applying transforms or camera offsets.

To create one you specify a draw mode, vertex format, transformation matrix and optionally a `Camera`.

```java
LazuliBufferBuilder builder = new LazuliBufferBuilder(
        VertexFormat.DrawMode.QUADS,
        VertexFormats.POSITION_COLOR_TEXTURE,
        matrices.peek().getPositionMatrix(),
        camera
);
```

You can chain calls to set vertex positions and colors:

```java
builder.vertex(x, y, z)
       .color(255, 255, 255, 255)
       .uv(u, v)
       .light(light)
       .normal(nx, ny, nz);
```

Use `draw()` or `drawAndReset()` to render the collected vertices.
