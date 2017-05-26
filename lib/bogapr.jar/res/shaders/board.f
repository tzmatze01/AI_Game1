varying vec3 uv;

void main (void) {  
	if ((uv.x < 0.142 || uv.x > 0.858) && (uv.y < 0.142 || uv.y > 0.858) && (uv.z < 0.142 || uv.z > 0.858)) {
		discard;
	}

	vec4 color;
	if (mod(floor(uv.x * 7.) + floor(uv.y * 7.) + floor(uv.z * 7.), 2.) == 1.) {
		color = vec4(1., 0.8, 0.6, 0.6);
	} else {
		color = vec4(0.5, 0.8, 1., 0.6);
	}

	gl_FragColor = color;
}
