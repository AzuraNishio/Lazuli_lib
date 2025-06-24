#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

uniform vec4 ColorModulator;

// New uniforms for bloom parameters.
uniform float bloomThreshold;  // brightness threshold for bloom effect
uniform float bloomIntensity;  // strength of bloom contribution
uniform vec3 bloomColor;       // color tint for the bloom

in vec2 texCoord0;
in vec3 normal;
in vec2 screenCoord;
in float dist;
in vec3 that;
in vec3 lightDirection;

out vec4 fragColor;

// A colored bloom function: if a pixel's brightness exceeds a threshold,
// a bloom contribution colored by bloomColor is added.
vec3 computeBloom(vec3 color, float threshold, float intensity, vec3 tint) {
    // Calculate perceived luminance using a standard weighted sum.
    float brightness = dot(color, vec3(0.2126, 0.7152, 0.0722));
    // If above threshold, mix the bloom tint and add an intensity boost.
    if (brightness > threshold) {
        return color * intensity * tint;
    } else {
        return vec3(0.0);
    }
}

void main() {
    // Convert screen coordinates (normally in [0,1] space) to a -1 to 1 range.
    vec2 uv = (screenCoord - 0.5) * 2.0;

    fragColor = vec4((0.5 * normal) + 0.5, 1.0);
}