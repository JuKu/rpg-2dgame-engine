#version 330

//input variable
layout (location=0) in vec3 pos;

void main()
{
    //convert 3D vector to 4D vector, gl_Position is an output variable
    gl_Position = vec4(pos, 1.0);
}