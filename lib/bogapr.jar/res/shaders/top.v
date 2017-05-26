varying vec3 vertex;
varying vec2 uv;
varying vec4 color;

void main(void) {
	vec4 v = gl_ModelViewMatrix * gl_Vertex;
	vertex = v.xyz;
	
	uv = vec2((gl_Vertex.x / 0.36 + 1.)/2., (gl_Vertex.y / 0.36 + 1.)/2.);
	color = gl_Color;

	gl_Position = gl_ProjectionMatrix * v;  
}
