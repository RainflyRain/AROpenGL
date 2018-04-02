attribute vec3 vPosition;
attribute vec3 vColor;
uniform mat4 vMatrix;

varying vec3 vFragColor;

void main(){
    gl_Position = vMatrix*vec4(vPosition,1.0);
    vFragColor = vColor;
}