uniform float zNormal;

varying vec3 N;
varying vec3 vertex;
varying vec4 color;

void main(void) {
	N = normalize(gl_NormalMatrix * vec3(gl_Vertex.xy, zNormal));

	vec4 v = gl_ModelViewMatrix * gl_Vertex;
	vertex = v.xyz;
	
	color = gl_Color;

	gl_Position = gl_ProjectionMatrix * v;  
}
