#version 150

uniform sampler2D InputColor;
uniform sampler2D Last;
uniform vec4 Tint;
in vec2 texCoord;


out vec4 fragColor;

void main() {
    fragColor = ((texture(InputColor, texCoord) * Tint) * Tint.a) + (texture(Last, texCoord) * (1.0 - Tint.a));
}
