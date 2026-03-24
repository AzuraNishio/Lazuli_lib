#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

mat3 hueShiftMatrix(float angle) {
    float c = cos(angle);
    float s = sin(angle);

    vec3 w = vec3(0.299, 0.587, 0.114); // luminance weights

    return mat3(
    w.x + c * (1.0 - w.x) + s * (-w.x),
    w.y + c * (-w.y)      + s * (-w.y),
    w.z + c * (-w.z)      + s * (1.0 - w.z),

    w.x + c * (-w.x)      + s * (0.143),
    w.y + c * (1.0 - w.y) + s * (0.140),
    w.z + c * (-w.z)      + s * (-0.283),

    w.x + c * (-w.x)      + s * (-(1.0 - w.x)),
    w.y + c * (-w.y)      + s * (w.y),
    w.z + c * (1.0 - w.z) + s * (w.z)
    );
}


void main() {
    vec4 color = texture(Sampler0, texCoord0);

    float v = color.r - color.g;
    float s1 = step(0.08, abs(v));
    float s2 = step(0.05, abs(v));
    float s3 = step(0.03, abs(v));

    vec3 tint = vec3((s1 * 0.5) + (s1 * 0.3) + (s2 * 0.3)) * vec3(3.0, 0.8, 0.2);

    tint.rg += vec2(color.b) * vec2(0.6, 0.4);

    fragColor.rgb = tint * hueShiftMatrix((6.0 * (texCoord0.x + texCoord0.y)) + ((0.8 - color.r) * 3.0));
    fragColor.a = tint.r;
}
