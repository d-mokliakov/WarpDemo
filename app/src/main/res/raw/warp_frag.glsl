#version 100

precision highp float;

uniform sampler2D u_Texture;
uniform vec4 u_Gesture;// x, y, dx, dy
varying vec2 v_TexCoord;

uniform float u_Size;
uniform float u_Pressure;

void main() {
    float distance = abs(distance(v_TexCoord, u_Gesture.xy));
    if (distance < u_Size) {
        float posX = v_TexCoord.x + u_Gesture.z * (1.0 - distance / u_Size) * u_Pressure;
        float posY = v_TexCoord.y + u_Gesture.w * (1.0 - distance / u_Size) * u_Pressure;
        vec4 texColor = texture2D(u_Texture, vec2(posX, posY));
        if (posX <= 0.0 || posX >= 1.0 || posY <= 0.0 || posY >= 1.0 || texColor.r > 1.0 || texColor.g > 1.0) {
            gl_FragColor = texture2D(u_Texture, v_TexCoord);
        } else {
            gl_FragColor = texColor;
        }
    } else {
        gl_FragColor = texture2D(u_Texture, v_TexCoord);
    }
}
