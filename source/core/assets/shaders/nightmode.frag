#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;   // Texture coordinates from the vertex shader
uniform sampler2D u_texture;  // The texture being drawn

// Uniforms to control darkness and color tint for night mode
uniform float u_darkness;    // Darkness level (0.0 to 1.0)
uniform vec3 u_nightColor;   // RGB color tint for night mode (bluish tint for night)

void main() {
    vec4 color = texture2D(u_texture, v_texCoord);  // Get the texture color

    // If the element has high alpha (e.g., UI elements like buttons or text)
    // or is very bright (such as text), don't apply the night mode effect.
    if (color.a > 0.9 || (color.r > 0.8 && color.g > 0.8 && color.b > 0.8)) {
        // Don't apply the night mode effect, render as is
        gl_FragColor = color;  // Keep the original color for buttons and text
    } else {
        // Apply night mode darkening to all other elements
        color.rgb = mix(color.rgb * u_darkness, u_nightColor, 0.5);  // Adjust the darkness and tint
        gl_FragColor = color;  // Output the final color
    }
}
