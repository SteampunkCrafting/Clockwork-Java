#version 330

/*--------------------
    Description:
    This fragment shader gets the uniform, which specifies the color of this mesh
    and colors each fragment in this color, having 1 as the alpha chanel
--------------------*/


/* STRUCTS
/*--------------------*/
/*--------------------*/


/* CONSTANTS
/*--------------------*/
/*--------------------*/


/* INPUT
/*--------------------*/
in vec2 fragmentTextureCoordinate;
/*--------------------*/


/* UNIFORMS
/*--------------------*/
uniform vec3 meshColor; // Specifies the color of the mesh
uniform sampler2D textureSampler;
uniform bool hasTexture;
/*--------------------*/


/* OUTPUT
/*--------------------*/
out vec4 fragmentColor;
/*--------------------*/


/* FUNCTIONS
/*--------------------*/
/*--------------------*/


/* MAIN
/*--------------------*/
void main() {
    if(hasTexture) {
        fragmentColor = texture(textureSampler, fragmentTextureCoordinate);
    } else {
        fragmentColor = vec4(meshColor, 1.0);
    }

}
/*--------------------*/
