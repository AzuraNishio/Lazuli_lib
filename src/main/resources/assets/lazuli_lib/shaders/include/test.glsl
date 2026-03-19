#version 150

float linearDepth(float d) {
    float near = 0.05;
    float far = 1000.0;
    return (2.0 * near * far) / (far + near - (d * 2.0 - 1.0) * (far - near));
}