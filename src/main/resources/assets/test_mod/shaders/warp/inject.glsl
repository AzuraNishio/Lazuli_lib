//Will automatically add missing uniforms
uniform vec3 epicenter;

//XfragColor means inject at X before the last instance of fragColor
//will always wrap X betwen newlines
//wrapping inside a fuction or just brackets {} wont change anything but allows for glsl sintax highlighting

//WARP_FRAGMENT(XfragColor =)
void waves(vec3 vertPos, vec3 epicenter){
    color.r *= 1.0 + (0.6 * sin( 2.0 * length(epicenter - vertPos)));
}//BREAK  <= inclide a break comment at the end

//WARP_VERTEX(vec3 pos = Position + ChunkOffset;X)
void waves(vec3 vertPos, vec3 epicenter){
    vertPos = pos;
}//BREAK

//this effectivelly injects after the first line, usefull for adding ins/outs
//WARP_FRAGMENT(#version 150X)
void uniformAndInputsFragment(vec3 pos){
    in vec3 vertPos;
}//BREAK

//WARP_VERTEX(#version 150X)
void uniformAndInputsVertex(vec3 color){
    out vec3 vertPos;
}//BREAK


//=========================== Sodium Warp ===========================

//WARP_FRAGMENT(XfragColor =<SODIUM>)
void waves(vec3 vertPos, vec3 epicenter){
    diffuseColor.r *= 1.0 + (0.6 * sin( 2.0 * length(epicenter - vertPos)));
    //diffuseColor.b = 1.0;
}//BREAK

//WARP_VERTEX(vec3 position = _vert_position + translation;X<SODIUM>)
void waves(vec3 vertPos, vec3 epicenter){
    vertPos = position;
}//BREAK


//this effectivelly injects after the first line, usefull for adding ins/outs
//WARP_FRAGMENT(#version 330 coreX<SODIUM>)
void uniformAndInputsFragment(vec3 pos){
    in vec3 vertPos;
}//BREAK

//WARP_VERTEX(#version 330 coreX<SODIUM>)
void uniformAndInputsVertex(vec3 color){
    out vec3 vertPos;
}//BREAK