#version 150

uniform sampler2D InputColor;
uniform sampler2D InputDepth;
uniform vec2 InSize;
uniform float Time;
uniform float FOV;
in vec2 texCoord;
out vec4 fragColor;

float linearDepth(float d) {
    float near = 0.05;
    float far = 1000.0;
    return (2.0 * near * far) / (far + near - (d * 2.0 - 1.0) * (far - near));
}

float dd(float x, float y){
    vec2 texel = 1.0 / InSize;
    return linearDepth(texture(InputDepth, texCoord + (vec2(x, y) * texel)).r);
}



void main() {
    vec2 nPix = (texCoord * InSize) - (0.5 * InSize);

    vec4 color = texture(InputColor, texCoord);
    float depth = dd(0.0, 0.0);
    float depthLeft = dd(1.0, 0.0);
    float depthRight = dd(-1.0, 0.0);
    float depthUp = dd(0.0, 1.0);
    float depthDown = dd(0.0, -1.0);

    vec2 derivative1 = vec2(depth - depthLeft, depth - depthDown);
    vec2 derivative2 = vec2(depthRight - depth, depthUp - depth);

    vec2 smaller = step(abs(derivative1), abs(derivative2));

    vec2 derivative = (smaller * derivative1) + ((1.0 - smaller) * derivative2);

    vec3 normal = normalize(vec3(derivative, 0.001 + depth * tan(FOV * 0.5) / InSize.x));

    float radius = 500.0 * depth;

    float decay = smoothstep(radius, radius * 0.9, (length(nPix) * depth)) / pow(depth, 0.5);

    vec3 lightDir = normalize(vec3(-nPix / InSize.x, 1.0));
    float diffuse = max(0.0, dot(lightDir, normal)) + 0.1;
    color.rgb *= 1.0 + (diffuse * vec3(21.0, 20.0, 30.0) * max(0.0, decay));

    //color.rgb = (normal * 0.5) + 0.5;

    fragColor = color;


}