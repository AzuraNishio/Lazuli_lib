#version 150

uniform sampler2D InputColor;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    fragColor = texture(InputColor, texCoord);
    fragColor.g = 0.0;
    fragColor.g = 0.0;
}
