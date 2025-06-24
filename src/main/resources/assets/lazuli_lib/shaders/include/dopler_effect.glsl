#ifndef DOPPLER_EFFECT_INCLUDED
#define DOPPLER_EFFECT_INCLUDED

// Define a velocidade da luz em unidades arbitrárias (por exemplo, 1.0 = velocidade da luz)
const float SPEED_OF_LIGHT = 1.0;

// Aplica o efeito Doppler relativístico na cor
vec4 doppler_effect(vec4 color, vec3 velocity) {
    // Consideramos apenas a componente Z (direção do observador)
    float v = velocity.z;

    // Limita v para não ultrapassar a velocidade da luz
    v = clamp(v, -0.99 * SPEED_OF_LIGHT, 0.99 * SPEED_OF_LIGHT);

    // Fator Doppler relativístico (frequência percebida / original)
    float doppler = sqrt((1.0 - v / SPEED_OF_LIGHT) / (1.0 + v / SPEED_OF_LIGHT));

    // Frequência aumenta → cor puxa pro azul (mais energética)
    // Frequência diminui → cor puxa pro vermelho (menos energética)

    vec3 shifted_color = vec3(
    clamp(color.r * doppler, 0.0, 1.0),
    clamp(color.g * doppler, 0.0, 1.0),
    clamp(color.b * doppler, 0.0, 1.0)
    );

    return vec4(shifted_color, color.a);
}

#endif
