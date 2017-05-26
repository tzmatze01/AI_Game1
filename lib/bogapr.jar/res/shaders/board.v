varying vec3 uv;

void main(void) {
	uv = vec3(gl_Vertex.x < -0.9 ? 1 : 0, gl_Vertex.y < -0.9 ? 1 : 0, gl_Vertex.x > 0.9 ? 1 : 0);

	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
