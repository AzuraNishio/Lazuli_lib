#version 150

uniform sampler2D InputColor;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    fragColor = texture(InputColor, texCoord) * vec4(1.2, 0.1, 0.1, 1.0);
}
