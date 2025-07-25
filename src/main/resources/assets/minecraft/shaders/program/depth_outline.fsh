#version 150

#define NEAR_PLANE      0.1
#define FAR_PLANE       100.0
#define EDGE_THRESHOLD  0.7

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2  texCoord;
out vec4 fragColor;

float LinearizeDepth(float d) {
	float z_n = d * 2.0 - 1.0;
	return 0.6 * ((2.0 * NEAR_PLANE * FAR_PLANE)
	/ (FAR_PLANE + NEAR_PLANE - z_n * (FAR_PLANE - NEAR_PLANE)));
}

void main() {
	// compute one-pixel offset
	ivec2 res = textureSize(DepthSampler, 0);
	vec2  texel = 1.0 / vec2(res);

	// sample linear depths
	float d0 = LinearizeDepth(texture(DepthSampler, texCoord               ).r);
	float dL = LinearizeDepth(texture(DepthSampler, texCoord + vec2(-texel.x, 0.0)).r);
	float dR = LinearizeDepth(texture(DepthSampler, texCoord + vec2( texel.x, 0.0)).r);
	float dU = LinearizeDepth(texture(DepthSampler, texCoord + vec2(0.0,  texel.y)).r);
	float dD = LinearizeDepth(texture(DepthSampler, texCoord + vec2(0.0, -texel.y)).r);

	// max difference with neighbors
	float diff = max(
	max(abs(d0 - dL), abs(d0 - dR)),
	max(abs(d0 - dU), abs(d0 - dD))
	);

	// edge mask
	float edge = step(EDGE_THRESHOLD, diff);

	fragColor = vec4(vec3(edge), 1.0);
}
