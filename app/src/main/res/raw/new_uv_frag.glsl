// Generates a new UV texture lookup based on the original UV coordinates.
#version 100

precision highp float;

varying vec2 v_TexCoord;

void main() {
    gl_FragColor = vec4(v_TexCoord.x, 1.0 - v_TexCoord.y, 0.0, 1.0);
}
