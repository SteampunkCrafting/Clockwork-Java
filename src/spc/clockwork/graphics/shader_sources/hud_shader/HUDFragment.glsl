#version 330

/*--------------------
    Description:
--------------------*/


/* STRUCTS
/*--------------------*/
struct Material {
    vec3 ambientColor;
    vec3 diffuseColor;
    vec3 specularColor;
    float specularPower;
    float reflectance;
    sampler2D textureSampler;
    bool hasTexture;
};
/*--------------------*/


/* CONSTANTS
/*--------------------*/
/*--------------------*/


/* INPUT
/*--------------------*/
// in vec3 fragmentPosition;
in vec2 fragmentTextureCoordinate;
/*--------------------*/


/* UNIFORMS
/*--------------------*/
uniform Material entityMaterial;
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
    if (entityMaterial.hasTexture)
        fragmentColor = vec4(entityMaterial.ambientColor, 1.0) *
                texture(entityMaterial.textureSampler, fragmentTextureCoordinate);
    else
        fragmentColor = vec4(entityMaterial.ambientColor, 1.0);
}
/*--------------------*/