// Draws texture using uv texture as a lookup table
#version 100

precision highp float;

uniform sampler2D u_Texture;
uniform sampler2D u_UvTexture;
varying vec2 v_TexCoord;

void main() {
    vec2 uv = texture2D(u_UvTexture, v_TexCoord).xy;
    gl_FragColor = texture2D(u_Texture, uv);
}
