#version 100

attribute vec4 a_Position;
attribute vec2 a_TexCoord;
varying vec2 v_TexCoord;

void main() {
    gl_Position = a_Position;
    v_TexCoord = vec2(a_TexCoord.x, a_TexCoord.y);
}
