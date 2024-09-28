// Vertex Shader (nightmode.vert)
attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;  // The projection and transformation matrix

varying vec2 v_texCoord;   // Varying variable to pass texture coordinates to fragment shader

void main() {
    v_texCoord = a_texCoord0;  // Pass the texture coordinates
    gl_Position = u_projTrans * a_position;  // Calculate the final position of the vertex
}
