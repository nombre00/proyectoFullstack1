package com.ventasWeb.cl.ventasWeb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.repository.DetalleCarritoRepository;


// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
public class DetalleCarritoServiceTest {

    // Creamos el mock del repositorio para simular su comportamiento.
    @Mock
    private DetalleCarritoRepository dcr;

    // Creamos una instancia de DetalleCarritoService y le inyectamos los mocks que creamos.
    @InjectMocks
    private DetalleCarritoService dcs;

    // Preparamos las variables que vamos a usar en los tests.
    // Declaramos las variables.
    private Producto p1;
    private DetalleCarrito dc1;
    private DetalleCarrito dc2;

    // Generamos una acción que se hace antes de cada test, esta acción es inicializar las variables.
    @BeforeEach
    void setUp(){
        // Primero inicializamos el producto que los detalleCarrito van a contener.
        p1 = new Producto();
        p1.setId_producto(Long.valueOf(1));
        p1.setNombre("frutos secos");
        p1.setCantidad(50);
        p1.setPrecio(800);
        // Luego inicializamos los detalleCarritos.
        dc1 = new DetalleCarrito();
        dc2 = new DetalleCarrito();
        dc1.setId_detalleCarrito(Long.valueOf(1));
        dc2.setId_detalleCarrito(Long.valueOf(2));
        dc1.setProducto(p1);
        dc2.setProducto(p1);
        dc1.setCantidadSolicitada(3);
        dc2.setCantidadSolicitada(5);
    }

    // Métodos.
    // Probando .buscarTodos() cuando hay detallesCarrito.
    @Test
    void probarBuscarTodosBien() {
        // Definimos el comportamiento del mock repositorio.
        // Hacemos una lista y le agregamos los detalleCarritos.
        List<DetalleCarrito> detallesCarrito = Arrays.asList(dc1,dc2);
        when(dcr.findAll()).thenReturn(detallesCarrito);

        // LLamamos al método .buscarTodos() de detalleCarritoService.
        List<DetalleCarrito> detallesCarrito2 = dcs.buscarTodos();

        // Verificaciones.
        // Verificamos la longitud de la lista.
        assertEquals(2, detallesCarrito2.size());
        // Verificamos datos de la lista.
        assertEquals(dc1.getId_detalleCarrito(), detallesCarrito2.get(0).getId_detalleCarrito());
        assertEquals(dc2.getId_detalleCarrito(), detallesCarrito2.get(1).getId_detalleCarrito());
        assertEquals(dc1.getProducto().getNombre(), detallesCarrito2.get(0).getProducto().getNombre());
        assertEquals(dc2.getProducto().getNombre(), detallesCarrito2.get(1).getProducto().getNombre());
        // Verificamos que el método haya sido llamado una vez.
        verify(dcr, times(1)).findAll();
    }

    // Probando .buscarTodosMal().
    @Test
    void probarBuscarTodosMal() {
        // Definimos el comportamiento del mock repositorio.
        when(dcr.findAll()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarTodos() del detalleCarritoService.
        List<DetalleCarrito> detallesCarrito2 = dcs.buscarTodos();

        // Verificaciones.
        // Verificamos que la lista está vacía.
        assertTrue(detallesCarrito2.isEmpty());
        // Verificamos que el método fue llamado una vez.
        verify(dcr, times(1)).findAll();
    }

    // Probando .buscarPorId().
    @Test
    void probarBuscarPorIdBien() {
        // Definimos el comportamiento del mock repositorio.
        when(dcr.findById(Long.valueOf(1))).thenReturn(Optional.of(dc1));

        // LLamamos al método .buscarPorId() del detalleCarritoService.
        DetalleCarrito dc3 = dcs.buscarPorId(Long.valueOf(1));

        // Verificaciones.
        // Verificamos que dc3 no sea nulo.
        assertNotNull(dc3);
        // Verificamos que el id de dc3 sea igual al de dc1.
    }
}
