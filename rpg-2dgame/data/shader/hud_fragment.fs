#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 colour;
uniform int hasTexture;

void main()
{
    if(colour.a <= 0) {
        discard;
    }

    if ( hasTexture == 1 )
    {
        fragColor = vec4(colour) * texture(texture_sampler, outTexCoord);
    }
    else
    {
        fragColor = vec4(colour);
    }
}