#version 330

/*--------------------
    Description:
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
/*--------------------*/


/* UNIFORMS
/*--------------------*/
uniform mat4 projectionModelMatrix;
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
    fragmentTextureCoordinate = vertexTextureCoordinate;
    gl_Position = projectionModelMatrix * vertexPosition;
}
/*--------------------*/