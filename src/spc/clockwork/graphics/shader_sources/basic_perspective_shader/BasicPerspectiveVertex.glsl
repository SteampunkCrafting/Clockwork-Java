#version 330

/*--------------------
    Description:
    This vertex shader gets 2 matrices as the uniforms.
    Those matrices are:
        * Projection matrix - a matrix that projects 3d space on a 2d screen
        * Model view matrix - a matrix that changes the position, scale and rotation of a mesh
    The final vertex position is computed by multiplication of the projection matrix by the
    product of model view matrix and the initial vertex position
--------------------*/


/* STRUCTS
/*--------------------*/
/*--------------------*/


/* CONSTANTS
/*--------------------*/
/*--------------------*/


/* INPUT
/*--------------------*/
layout (location = 0) in vec4 vertexPosition;
layout (location = 1) in vec2 vertexTextureCoordinate;
layout (location = 2) in vec4 vertexNormal;
/*--------------------*/


/* UNIFORMS
/*--------------------*/
uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
/*--------------------*/


/* OUTPUT
/*--------------------*/
out vec2 fragmentTextureCoordinate;
/*--------------------*/


/* FUNCTIONS
/*--------------------*/
/*--------------------*/


/* MAIN
/*--------------------*/
void main() {
    gl_Position = projectionMatrix * modelViewMatrix * vertexPosition;
    fragmentTextureCoordinate = vertexTextureCoordinate;
}
/*--------------------*/