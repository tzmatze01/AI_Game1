uniform vec3 lightPos;

varying vec3 N;
varying vec3 vertex;
varying vec4 color;

void main (void) {  
	vec3 L = normalize(lightPos - vertex);

	gl_FragColor = vec4(color.rgb * max(dot(L,N), 0.2), color.a);
}
