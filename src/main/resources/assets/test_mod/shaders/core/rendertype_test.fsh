#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    float r = 1500.0;
    vec2 pix = floor(texCoord0 * r) / r;

    vec4 color = texture(Sampler0, pix);
    vec4 color2 = texture(Sampler0, pix + vec2(1.0/ r, 0));
    vec4 color3 = texture(Sampler0, pix + vec2(0, 1.0/ r));

    float a = color.r - color2.r;
    float b = color.r - color3.r;

    vec3 norm = normalize(vec3(a, 0.6, b));

    float v = color.r * color.r * color.r * 50.0;

    vec3 tint = min(2.0, v) * vec3(0.5, 0.4, 0.0);

    tint *= vec3(dot(norm, normalize(vec3(1, 1, 1))));

    fragColor.rgb = tint;
    fragColor.a = 1.0;
}
