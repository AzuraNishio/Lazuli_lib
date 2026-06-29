#version 150

uniform sampler2D InputColor;
uniform vec2 InSize;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    float size = 2.0;
    vec3 pix = vec3(1.0 / InSize, 0.0);
    vec4 color1 = texture(InputColor, texCoord);
    vec4 color2 = texture(InputColor, texCoord + size * pix.xz);
    vec4 color3 = texture(InputColor, texCoord + size * pix.zy);

    vec4 diff = pow(color2 - color1, vec4(2.0)) + pow(color3 - color1, vec4(2.0));




    fragColor = vec4(1.0, 1.0, 0.0, 2.0) / (diff.x + diff.y + diff.z);
}
