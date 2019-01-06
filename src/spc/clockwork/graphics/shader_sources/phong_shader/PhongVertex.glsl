#version 330 core

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
layout (location = 2) in vec3 vertexNormal;
/*--------------------*/


/* UNIFORMS
/*--------------------*/
uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
/*--------------------*/


/* OUTPUT
/*--------------------*/
out vec2 fragmentTextureCoordinate;
out vec3 fragmentPosition;
out vec3 fragmentNormal;
/*--------------------*/


/* FUNCTIONS
/*--------------------*/
/*--------------------*/


/* MAIN
/*--------------------*/
void main() {
    //Local variable
    vec4 modelViewPosition = modelViewMatrix * vertexPosition;

    //Stuff we pass to fragment shader
    fragmentTextureCoordinate = vertexTextureCoordinate;
    fragmentPosition = modelViewPosition.xyz;
    fragmentNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;


    //The vertex position
    gl_Position = projectionMatrix * modelViewPosition;
}
/*--------------------*/
