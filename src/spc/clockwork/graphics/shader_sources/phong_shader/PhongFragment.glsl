#version 330 core

/*--------------------
    Description:
--------------------*/

#define MAX_DIR_LIGHTS 4
#define MAX_POINT_LIGHTS 64
#define MAX_SPOT_LIGHTS 64
//#define LIGHT_INTENSITY_ARRAY_TERMINATOR -1


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

struct LightAttenuation {
    float constant;
    float linear;
    float exponent;
};

struct DirectionalLight {
    vec3 direction; // a direction in view space
    vec3 color;
    float intensity;
};

struct PointLight {
    vec3 position; // view matrix times the position of the light (preprocessed in ShaderProgram class)
    vec3 color;
    float intensity;
    LightAttenuation attenuation;
};

struct SpotLight {
    vec3 position; // view matrix times the position of the light (preprocessed in ShaderProgram class)
    vec3 coneDirection; // a direction of light
    float cosOfConeAngle; // a cos of cone angle
    vec3 color;
    float intensity;
    LightAttenuation attenuation;
};

/*--------------------*/


/* CONSTANTS
/*--------------------*/
/*--------------------*/


/* INPUT
/*--------------------*/
in vec2 fragmentTextureCoordinate;
in vec3 fragmentPosition;
in vec3 fragmentNormal;
/*--------------------*/


/* UNIFORMS
/*--------------------*/
uniform Material entityMaterial;
uniform DirectionalLight directionalLights[MAX_DIR_LIGHTS];
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform vec3 ambientLightColor;
uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
/*--------------------*/


/* OUTPUT
/*--------------------*/
out vec4 fragmentColor;
/*--------------------*/


/* FUNCTIONS
/*--------------------*/
vec4 computeAmbientComponent(vec3 ambientLightColor, vec4 ambientColor) {
    return vec4(ambientLightColor, 1.0) * ambientColor;
}


vec4 computeDiffuseComponent(vec4 diffuseColor,
                             vec3 fragmentPosition,
                             vec3 fragmentNormal,
                             vec3 lightPosition,
                             vec3 lightColor,
                             float lightIntensity) {
    return vec4(lightColor, 1.0)
           * diffuseColor
           * lightIntensity
           * max(dot(fragmentNormal, normalize(lightPosition - fragmentPosition)), 0.0);
}


vec4 computeSpecularComponent(vec4 specularColor,
                              float specularPower,
                              float reflectance,
                              vec3 fragmentPosition,
                              vec3 fragmentNormal,
                              vec3 lightPosition,
                              vec3 lightColor,
                              float lightIntensity) {
    vec3 toLightSource = normalize(lightPosition - fragmentPosition);
    vec3 reflectedLight = normalize(reflect(-toLightSource, fragmentNormal));
    vec3 cameraPosition = normalize(-fragmentPosition);
    float specularFactor = pow(max(dot(cameraPosition, reflectedLight), 0.0), specularPower);
    return vec4(lightColor, 1.0) * specularColor * lightIntensity * reflectance * specularFactor;
}


float computeAttenuationFactor(vec3 fragmentPosition,
                               vec3 lightPosition,
                               float attConst,
                               float attLin,
                               float attExpt) {
    float distance = length(fragmentPosition - lightPosition);
    return attConst + attLin * distance + attExpt * distance * distance;
}


vec4 computeDirLightInfluence(vec2 fragmentTextureCoordinate,
                              vec3 fragmentNormal,
                              Material entityMaterial,
                              DirectionalLight directionalLight) {
    if (entityMaterial.hasTexture) {
        vec4 textureColor = texture(entityMaterial.textureSampler, fragmentTextureCoordinate);
        return computeDiffuseComponent(textureColor,
                                       vec3(0), //fragment position
                                       fragmentNormal,
                                       -directionalLight.direction, //light position
                                       directionalLight.color,
                                       directionalLight.intensity)
               + computeSpecularComponent(textureColor,
                                          entityMaterial.specularPower,
                                          entityMaterial.reflectance,
                                          vec3(0),
                                          fragmentNormal,
                                          -directionalLight.direction,
                                          directionalLight.color,
                                          directionalLight.intensity);
    } else {
        return computeDiffuseComponent(vec4(entityMaterial.diffuseColor, 1.0),
                                       vec3(0), //fragment position
                                       fragmentNormal,
                                       -directionalLight.direction, //light position
                                       directionalLight.color,
                                       directionalLight.intensity)
               + computeSpecularComponent(vec4(entityMaterial.specularColor, 1.0),
                                          entityMaterial.specularPower,
                                          entityMaterial.reflectance,
                                          vec3(0),
                                          fragmentNormal,
                                          -directionalLight.direction,
                                          directionalLight.color,
                                          directionalLight.intensity);
    }
}


vec4 computePointLightInfluence(vec3 fragmentPosition,
                                vec2 fragmentTextureCoordinate,
                                vec3 fragmentNormal,
                                Material entityMaterial,
                                PointLight pointLight) {
    if (entityMaterial.hasTexture) {
        vec4 textureColor = texture(entityMaterial.textureSampler, fragmentTextureCoordinate);
        return (computeDiffuseComponent(textureColor,
                                         fragmentPosition,
                                         fragmentNormal,
                                         pointLight.position,
                                         pointLight.color,
                                         pointLight.intensity)
                   + computeSpecularComponent(textureColor,
                                              entityMaterial.specularPower,
                                              entityMaterial.reflectance,
                                              fragmentPosition,
                                              fragmentNormal,
                                              pointLight.position,
                                              pointLight.color,
                                              pointLight.intensity))
               / computeAttenuationFactor(fragmentPosition,
                                          pointLight.position,
                                          pointLight.attenuation.constant,
                                          pointLight.attenuation.linear,
                                          pointLight.attenuation.exponent);
    } else {
        return computeAmbientComponent(ambientLightColor, vec4(entityMaterial.ambientColor, 1.0))
               + (computeDiffuseComponent(vec4(entityMaterial.diffuseColor, 1.0),
                                         fragmentPosition,
                                         fragmentNormal,
                                         pointLight.position,
                                         pointLight.color,
                                         pointLight.intensity)
                   + computeSpecularComponent(vec4(entityMaterial.specularColor, 1.0),
                                              entityMaterial.specularPower,
                                              entityMaterial.reflectance,
                                              fragmentPosition,
                                              fragmentNormal,
                                              pointLight.position,
                                              pointLight.color,
                                              pointLight.intensity))
               / computeAttenuationFactor(fragmentPosition,
                                          pointLight.position,
                                          pointLight.attenuation.constant,
                                          pointLight.attenuation.linear,
                                          pointLight.attenuation.exponent);
    }
}

vec4 computeSpotLightInfluence(vec3 fragmentPosition,
                               vec2 fragmentTextureCoordinate,
                               vec3 fragmentNormal,
                               Material entityMaterial,
                               SpotLight spotLight) {
    float cosine = dot(normalize(fragmentPosition - spotLight.position), spotLight.coneDirection);
    if (cosine < spotLight.cosOfConeAngle) {
        return vec4(0);
    } else if (entityMaterial.hasTexture) {
        vec4 textureColor = texture(entityMaterial.textureSampler, fragmentTextureCoordinate);
        return (computeDiffuseComponent(textureColor,
                                        fragmentPosition,
                                        fragmentNormal,
                                        spotLight.position,
                                        spotLight.color,
                                        spotLight.intensity)
                    + computeSpecularComponent(textureColor,
                                               entityMaterial.specularPower,
                                               entityMaterial.reflectance,
                                               fragmentPosition,
                                               fragmentNormal,
                                               spotLight.position,
                                               spotLight.color,
                                               spotLight.intensity))
               / computeAttenuationFactor(fragmentPosition,
                                          spotLight.position,
                                          spotLight.attenuation.constant,
                                          spotLight.attenuation.linear,
                                          spotLight.attenuation.exponent)
               * (1 - (1 - cosine) / (1 - spotLight.cosOfConeAngle));
    } else {
        return (computeDiffuseComponent(vec4(entityMaterial.diffuseColor, 1.0),
                                        fragmentPosition,
                                        fragmentNormal,
                                        spotLight.position,
                                        spotLight.color,
                                        spotLight.intensity)
                    + computeSpecularComponent(vec4(entityMaterial.specularColor, 1.0),
                                               entityMaterial.specularPower,
                                               entityMaterial.reflectance,
                                               fragmentPosition,
                                               fragmentNormal,
                                               spotLight.position,
                                               spotLight.color,
                                               spotLight.intensity))
               / computeAttenuationFactor(fragmentPosition,
                                          spotLight.position,
                                          spotLight.attenuation.constant,
                                          spotLight.attenuation.linear,
                                          spotLight.attenuation.exponent)
               * (1 - (1 - cosine) / (1 - spotLight.cosOfConeAngle));
    }
}


/*--------------------*/


/* MAIN
/*--------------------*/
void main() {

    /* -- COMPUTING AMBIENT COMPONENT OF THIS FRAGMENT -- */
    fragmentColor = entityMaterial.hasTexture
                        ? computeAmbientComponent(ambientLightColor,
                                                  texture(entityMaterial.textureSampler,
                                                        fragmentTextureCoordinate))
                        : computeAmbientComponent(ambientLightColor,
                                                  vec4(entityMaterial.ambientColor, 1.0));


    /* -- ADDING THE INFLUENCE OF THE DIRECTIONAL LIGHTS -- */
    for (int i = 0; i < MAX_DIR_LIGHTS; i++) {
        if (directionalLights[i].intensity <= 0) break;

        fragmentColor += computeDirLightInfluence(fragmentTextureCoordinate,
                                                  fragmentNormal,
                                                  entityMaterial,
                                                  directionalLights[i]);
    }


    /* -- ADDING THE INFLUENCE OF THE POINT LIGHTS -- */
    for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
        if (pointLights[i].intensity <= 0) break;

        fragmentColor += computePointLightInfluence(
                fragmentPosition,
                fragmentTextureCoordinate,
                fragmentNormal,
                entityMaterial,
                pointLights[i]);
    }


    /* -- ADDING THE INFLUENCE OF THE SPOT LIGHTS -- */
    for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
        if (spotLights[i].intensity <= 0) break;

        fragmentColor += computeSpotLightInfluence(
                fragmentPosition,
                fragmentTextureCoordinate,
                fragmentNormal,
                entityMaterial,
                spotLights[i]);
    }
}
/*--------------------*/
