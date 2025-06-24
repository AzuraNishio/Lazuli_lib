#version 150

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

in vec3 Position;
in vec3 Normal;
in vec2 UV0;

out vec2 screenCoord;
out vec2 UV;
out float dist;
out vec3 that;
out vec3 normal;
out vec3 lightDirection;

void main() {
    vec4 pos = ProjMat * ModelViewMat * vec4(Position, 1.0);
    gl_Position = pos;

    // Pass along the projected XY coordinates as screen coordinates.
    screenCoord = pos.xy;

    // Forward the vertex normal to the fragment shader.
    normal = Normal;

    // Assume a simple directional light coming from the vertex position.
    lightDirection = normalize(Position);

    // Calculate 'that' as the difference between the normal and a normalized version of position.
    that = normal - normalize(Position);

    // Pass texture coordinates unchanged.
    UV = UV0;

    // Compute a distance-like value based on the angular difference.
    float d = length(that);
    normal = Normal;

    // Manipulate distance to generate a useful value for later (in the fragment shader).
    dist = 2.0 * cos(3.1415 - (2.0 * acos(d / 2.0)));
}