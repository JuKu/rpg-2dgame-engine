#version 330

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 colour;
uniform int hasTexture;

//additional parameters for the lighting shader
uniform LOWP vec4 ambientColor;

//resolution of screen
uniform vec2 resolution;

void main()
{
    if (colour.a <= 0) {
        discard;
    }

    if ( hasTexture == 1 )
    {
        vec2 lighCoord = (gl_FragCoord.xy / resolution.xy);

        vec4 diffuseColor = texture(texture_sampler, outTexCoord);
        vec3 ambient = ambientColor.rgb * ambientColor.a;

        vec3 final = colour * diffuseColor.rgb * ambient;

        //fragColor = vec4(colour) * texture(texture_sampler, outTexCoord);
        fragColor = vec4(final, diffuseColor.a);
    }
    else
    {
        fragColor = vec4(colour);
    }
}