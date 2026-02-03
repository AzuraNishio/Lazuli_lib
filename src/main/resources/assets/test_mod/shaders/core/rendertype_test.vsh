#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV2;
uniform sampler2D Sampler0;


uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 vertexColor;
out vec2 texCoord0;
out vec2 texCoord2;

void main() {
    vertexColor = Color;
    texCoord0 = UV0;
    texCoord2 = UV2;

    vec4 color = texture(Sampler0, texCoord0);

    float v = color.r * 0.2;

    gl_Position = ProjMat * ModelViewMat * vec4(Position + vec3(0,v, 0), 1.0);




}
