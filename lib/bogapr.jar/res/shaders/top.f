uniform vec3 lightPos;
uniform sampler2D sampler;

varying vec3 vertex;
varying vec2 uv;
varying vec4 color;

void main (void) {  
	vec3 L = normalize(lightPos - vertex);

	float myHeight = texture2D(sampler, uv).r;
	float heightDifferenceToRight = texture2D(sampler, uv + vec2(0.02, 0.)).r - myHeight;
	float heightDifferenceToTop = texture2D(sampler, uv + vec2(0., 0.02)).r - myHeight;

	vec3 normalFromMap = vec3(-heightDifferenceToRight, -heightDifferenceToTop, 1.);
	vec3 N = normalize(gl_NormalMatrix * normalFromMap);

	gl_FragColor = vec4(color.rgb * max(dot(L,N), 0.2), color.a);
}
