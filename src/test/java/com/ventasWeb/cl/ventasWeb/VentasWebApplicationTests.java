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


/* 
Comandos para subir el código a github.

git add .
git commit -m "Agrega configuración de GitHub Actions para CI"
git push origin main 
*/