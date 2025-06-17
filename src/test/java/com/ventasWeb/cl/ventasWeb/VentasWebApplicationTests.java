package com.ventasWeb.cl.ventasWeb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
class VentasWebApplicationTests {

	@Test
	void contextLoads() {
		 // Este test pasa si el contexto de Spring se carga correctamente
	}

}
