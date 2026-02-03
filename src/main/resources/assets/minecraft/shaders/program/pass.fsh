#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

uniform vec2 InSize;
uniform vec2 Pos;
uniform float Time;

out vec4 fragColor;

float hash(vec3 p)
{
	p = fract(p * 0.1031);
	p += dot(p, p.yzx + 33.33);
	return fract((p.x + p.y) * p.z);
}



vec4 sampleTex(vec2 uv) {
	return texture(DiffuseSampler, uv);
}

void main() {



	vec2 texel = 1.0 / InSize;

	vec4 c = sampleTex(texCoord);

	float h  = c.r; // current height
	float h0 = c.g; // previous height

	// neighbors (current height)
	float u = sampleTex(texCoord + vec2(0.0,  texel.y)).r;
	float d = sampleTex(texCoord + vec2(0.0, -texel.y)).r;
	float l = sampleTex(texCoord + vec2(-texel.x, 0.0)).r;
	float r = sampleTex(texCoord + vec2( texel.x, 0.0)).r;

	// laplacian
	float lap = (u + d + l + r) - 4.0 * h;

	// wave equation (THIS is the missing piece)
	float newH = (2.0 * h - h0);

	float rand = hash(vec3(texCoord + 10.0, newH) * 0.1);

	newH += lap * (0.25 + (rand * 0.05));

	// damping (kills maxed buffers)
	newH *= 0.993;

	// inject ripple
	float dist = length(texCoord - vec2(Pos.x, Pos.y));

	newH += smoothstep(length(texel) * 16.0, 0.0, dist) * 0.8;


	fragColor = vec4(newH, h, 0.0, 1.0);
}
