package com.ventasWeb.cl.ventasWeb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.repository.DetalleCarritoRepository;


// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
public class DetalleCarritoServiceTest {

    // Creamos el mock de los repositorios para simular su comportamiento.
    @Mock
    private DetalleCarritoRepository dcr;
    @Mock
    private ProductoService ps;

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

    // Probando .buscarPorId() cuando encuentra un detalleCarrito.
    @Test
    void probarBuscarPorIdBien() {
        // Definimos el comportamiento del mock repositorio.
        when(dcr.findById(Long.valueOf(1))).thenReturn(Optional.of(dc1));

        // LLamamos al método .buscarPorId() del detalleCarritoService.
        DetalleCarrito dc3 = dcs.buscarPorId(Long.valueOf(1));

        // Verificaciones.
        // Verificamos que dc3 no sea nulo.
        assertNotNull(dc3);
        // Verificamos que el nombre del producto dentro de dc3 sea el nombre de p1.
        assertEquals(p1.getNombre(), dc3.getProducto().getNombre());
        // Verificamos que el método fue llamado una vez.
        verify(dcr, times(1)).findById(Long.valueOf(1));
    }

    // Probando .guardar()
    @Test
    void probarGuardar() {
        //Definimos el comportamiento del mock repositorio.
        when(dcr.save(any(DetalleCarrito.class))).thenReturn(dc1);

        // LLamamos al método .guardar del detalleCarritoService.
        DetalleCarrito dc3 = dcs.guardar(dc1);

        // Verificaciones.
        // Verificamos que dc3 no sea nulo.
        assertNotNull(dc3);
        // Verificamos que el nombre del producto de dc3 sea el de p1.
        assertEquals(p1.getNombre(), dc3.getProducto().getNombre());
        // Verificamos que el método fue llamado una vez.
        verify(dcr, times(1)).save(dc1);
    }

    // Probamos .borrar()
    @Test
    void probarBorrar() {
        // Definimos el comportamiento del mock repositorio.
        doNothing().when(dcr).deleteById(Long.valueOf(1));

        // LLamamos al método .borar() de detalleCarritoService.
        dcs.borrar(1);

        // Verificaciones.
        // Verificamos que el método fue llamado una vez.
        verify(dcr, times(1)).deleteById(Long.valueOf(1));
    }

    // Probamos .valor()
    @Test
    void probarValor() {
        // Definimos el comportamiento del mock repositorio.
        when(dcr.findById(Long.valueOf(1))).thenReturn(Optional.of(dc1));
        when(ps.buscarPorId(Long.valueOf(1))).thenReturn(p1);

        // LLamamos al método .valor() del DetalleCarritoService()
        int precio = dcs.valor(Long.valueOf(1));

        // Verificaciones.
        // Verificamos que el precio sea el que corresponde.
        int precio2 = p1.getPrecio() * dc1.getCantidadSolicitada();
        assertEquals(precio2, precio);
        // Verificamos que los métodos fueron llamados cada uno una vez.
        verify(dcr, times(1)).findById(Long.valueOf(1));
        verify(ps, times(1)).buscarPorId(Long.valueOf(1));
    }

    // Probamos .agregarProducto()
    @Test
    void probarAgregarProducto() {
        // Definimos el comportamiento del mock repositorio.
        when(ps.buscarPorId(Long.valueOf(1))).thenReturn(p1);
        // Hacemos esto para imitar el retornar un objeto que se crea dentro del método en vez de hardcodearlo.
        // Acá usamos .thenAnswer() que permite retornar un valor dinámico, a diferencia de .thenReturn que devuevle un valor fijo.
        // Acá estamos retornando el argumento dado y además le estamos asignando un id, imitando el comportamiento de la base de datos.
        // invocation -> es parte de la funcion lambda que estamos usando y sirve para retornar el valor dado.
        // invocation -> es como se redacta esta función lambda en particular y lo que está dentro de {} es el cuerpo.
        when(dcr.save(any(DetalleCarrito.class))).thenAnswer(invocation -> {
            // invocation.getArgument(0) obtiene el argumento dado (el DetalleCarrito creado dentro del método .agregarProducto()).
            DetalleCarrito dc = invocation.getArgument(0);
            // Le asignamos un id, imitando la base de datos.
            dc.setId_detalleCarrito(3L); // Simula la generación del ID
            // Retornamos el DetalleCarrito modificado.
            return dc;
        });

        // LLamamos al método .agregarProducto()
        DetalleCarrito dc3 = dcs.agregarProducto(1, 3);

        // Verificaciones.
        // Verificamos el nombre del producto dentro de dc3.
        assertEquals("frutos secos", dc3.getProducto().getNombre());
        // Verificamos la cantidad.
        assertEquals(3, dc3.getCantidadSolicitada());
        // Verificamos que los métodos fueron llamados cada uno una vez.
        verify(ps, times(1)).buscarPorId(1L);
        verify(dcr, times(1)).save(any(DetalleCarrito.class));
    }

    // Probamos .cambiarCantidad()
    @Test
    void probarCambiarCantidad() {
        // Definimos las variables que vamos a ocupar.
        int idDetalleCarrito = 1;
        int cantidad = 7;
        // Definimos el comportamiento del mock repositorio.
        when(dcr.findById(1L)).thenReturn(Optional.of(dc1));
        when(dcr.save(dc1)).thenReturn(dc1);

        // LLamamos el método .cambiarCantidad() de detalleCarritoService()
        DetalleCarrito dc3 = dcs.cambiarCantidad(idDetalleCarrito, cantidad);

        // Verificaciones.
        // Verificamos que la cantidad cambió.
        assertEquals(cantidad, dc3.getCantidadSolicitada());
        // Verificamos que cada método fue llamado una vez.
        verify(dcr, times(1)).findById(1L);
        verify(dcr, times(1)).save(dc1);
    }
}
