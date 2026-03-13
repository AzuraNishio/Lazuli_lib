//Will automatically add missing uniforms
uniform vec3 epicenter2;

//XfragColor means inject at X before the last instance of fragColor
//will always wrap X betwen newlines
//wrapping inside a fuction or just brackets {} wont change anything but allows for glsl sintax highlighting

//WARP_FRAGMENT(XfragColor =)
void waves(vec3 vertPos2, vec3 epicenter2){
    color.g *= 1.0 + (0.6 * sin( 6.0 * length(epicenter2 - vertPos2)));
}//BREAK  <= inclide a break comment at the end

//WARP_VERTEX(vec3 pos = Position + ChunkOffset;X)
void waves(vec3 vertPos, vec3 epicenter2){
    vertPos2 = pos;
}//BREAK  <= inclide a break comment at the end

//this effectivelly injects after the first line, usefull for adding ins/outs
//WARP_FRAGMENT(#version 150X)
void uniformAndInputsFragment(vec3 pos){
    in vec3 vertPos2;
}//BREAK

//WARP_VERTEX(#version 150X)
void uniformAndInputsVertex(vec3 color){
    out vec3 vertPos2;
}//BREAK