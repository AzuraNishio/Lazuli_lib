//Will automatically add missing uniforms
uniform vec3 epicenter;

//XfragColor means inject at X before the last instance of fragColor
//will always wrap X betwen newlines
//wrapping inside a fuction or just brackets {} wont change anything but allows for glsl sintax highlighting

//WARP_FRAGMENT(XfragColor =)
void waves(vec3 pos, vec3 epicenter){
    color.rgb += vec3(0.5, 0.0, 0.0);
}//BREAK  <= inclide a break comment at the end

//this effectivelly injects after the first line, usefull for adding ins/outs
//WARP_FRAGMENT(#version 150X)
void uniformAndInputsFragment(vec3 pos){
    in vec3 vertPos;
}//BREAK

//WARP_VERTEX(#version 150X)
void uniformAndInputsVertex(vec3 color){
    out vec3 vertPos;
}//BREAK