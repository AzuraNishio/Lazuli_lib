#version 150
/*;

// import lazuli_lib:test

// autoSemicolon

// SugarSugar

*/;

uniform sampler2D InputColor;
uniform sampler2D InputDepth;
uniform vec2 InSize;
uniform float Time;
uniform float FOV;
in vec2 texCoord;



float linearDepth(float d) {
    float near = 0.05;
    float far = 1000.0;
    return (2.0 * near * far) / (far + near - (d * 2.0 - 1.0) * (far - near));
}
out vec4 fragColor;



float dd(float x, float y){
    vec2 texel = 1.0 / InSize;
    return linearDepth(texture(InputDepth, texCoord + (vec2(x, y) * texel)).x);
}

void main() {
    vec2 nPix = (texCoord * InSize) - (0.5 * InSize);

    vec4 color = texture(InputColor, texCoord);
    float depth      = dd( 0.0,  0.0);


    color.r += 0.1 / sin(depth);

    fragColor = color;
}