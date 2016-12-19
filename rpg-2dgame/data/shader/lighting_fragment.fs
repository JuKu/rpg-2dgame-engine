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

uniform sampler2D u_lightmap;//light map

void main()
{
    if (colour.a <= 0) {
        discard;
    }

    if ( hasTexture == 1 )
    {
        vec2 lightCoord = (gl_FragCoord.xy / resolution.xy);
        vec4 light = texture(u_lightmap, lightCoord);

        vec4 diffuseColor = texture(texture_sampler, outTexCoord);

        //calculate ambient color
        vec3 ambient = ambientColor.rgb * ambientColor.a;
        vec3 intensity = ambient + light.rgb;

        vec3 final = diffuseColor.rgb * ambient;

        //fragColor = vec4(colour) * texture(texture_sampler, outTexCoord);
        fragColor = colour * vec4(final, diffuseColor.a);

        /*vec4 diffuseColor = texture(texture_sampler, outTexCoord);
        vec2 lightCoord = (gl_FragCoord.xy / resolution.xy);
        vec4 light = texture(u_lightmap, lightCoord);

        vec3 ambient = ambientColor.rgb * ambientColor.a;
        vec3 intensity = ambient + light.rgb;
        vec3 finalColor = diffuseColor.rgb * intensity;

        fragColor = colour * vec4(finalColor, diffuseColor.a);*/
    }
    else
    {
        fragColor = vec4(colour);
    }
}