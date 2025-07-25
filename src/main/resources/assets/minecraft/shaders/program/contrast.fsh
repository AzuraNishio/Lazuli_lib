#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform float Time;

out vec4 fragColor;

vec2 barrelDistortion(vec2 coord) {
	vec2 cent = coord - 0.5;
	float dist = length(cent);

	float tanhStrength = 1.4;
	float logStrength = 1.4;
	vec2  radial = normalize(cent) * tanh(dist * tanhStrength);
	vec2  curved = sign(radial) * log(1.0 + abs(radial) * logStrength * 0.5);

	return curved + 0.5;
}

float hash(vec2 p) {
	p = fract(p * vec2(123.33197, 456.312471));
	p += dot(p, p + 78.212312893);
	return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
}


void main() {
	float offset = 0.003;
	float pixel_size = 0.003;
	float pixel_strenght = 0.7;
	vec3 color_multiplier = vec3(1.1, 0.85, 0.85);

	vec2 fTexCoord = barrelDistortion(texCoord);

	vec2 pixCoord = pixel_size * round(fTexCoord / pixel_size);

	vec3 col;
	col.r = texture(DiffuseSampler, fTexCoord + vec2(offset, 0.0)).r;
	col.g = texture(DiffuseSampler, fTexCoord).g;
	col.b = texture(DiffuseSampler, fTexCoord - vec2(offset, 0.0)).b;

	vec3 pixelated;
	pixelated.r = texture(DiffuseSampler, pixCoord + vec2(offset, 0.0)).r;
	pixelated.g = texture(DiffuseSampler, pixCoord).g;
	pixelated.b = texture(DiffuseSampler, pixCoord - vec2(offset, 0.0)).b;

	pixelated *= color_multiplier;

	col = (col + (pixelated * pixel_strenght)) / (1.0 + pixel_strenght);

	vec3 line_color = vec3(0.06, 0.03, 0.03);
	float v = sin(fTexCoord.y * 60.0 - (Time * 0.06));

	v += sin(fTexCoord.y * 90.0 - (Time * 0.11)) * 0.2;

	col += (1.0 - abs(v)) * line_color;

	col *= color_multiplier;

	col += 0.02 * hash(vec2(0.02 * Time, pixCoord.x + (pixCoord.y * 200.0)));

	fragColor = vec4(col, 1.0);
}




































































































