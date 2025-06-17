package com.ventasWeb.cl.ventasWeb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.repository.ProductoRepository;


// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    // Creamos un mock del repositorio para simular su comportamiento.
    @Mock
    private ProductoRepository pr;

    // Creamos una instancia de ProductoService y le inyectamos los mocks que creamos.
    @InjectMocks
    private ProductoService ps;


    // Preparamos las variables que vamos a usar en los tests.
    // Declaramos las variables.
    private Producto p1;
    private Producto p2;
    // Generamos una acción que se hace antes de cada test, esta acción es inicializar las variables.
    @BeforeEach
    void setUp() {
        p1 = new Producto();
        p2 = new Producto();
        p1.setId_producto(Long.valueOf(1));
        p1.setNombre("bolsas papel");
        p2.setId_producto(Long.valueOf(2));
        p2.setNombre("frutos secos");
    }


    // Métodos.
    // Probando .buscarTodos() cuando hay productos.
    @Test
    public void probarBuscarTodosBien(){
        // Definimos el comportamiento del Mock repositorio.
        // Hacemos una lista y le agregamos los productos.
        List<Producto> productos = Arrays.asList(p1,p2);
        when(pr.findAll()).thenReturn(productos);
        
        // LLamamos al método .buscarTodos() del ProductoService.
        List<Producto> productos2 = ps.buscarTodos();

        // Verificaciones.
        // Verificamos la longitud de la lista.
        assertEquals(2, productos2.size());
        // Verificamos los nombres de los productos en la lista.
        assertEquals("bolsas papel", productos2.get(0).getNombre());
        assertEquals("frutos secos", productos2.get(1).getNombre());
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).findAll();
    }

    // Probando .buscarTodos() cuando no hay productos.
    @Test
    void probarBuscarTodosMal() {
        // Definimos el comportamiento del mock repositorio.
        when(pr.findAll()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarTodos() del ProductoService.
        List<Producto> productos2 = ps.buscarTodos();

        // Verificaciones.
        // Verificamos que la lista esté vacía.
        assertTrue(productos2.isEmpty());
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).findAll();
    }

    // Probando .buscarPorId() cuando encuentra el producto.
    @Test
    void probarBuscarPorIdBien() {
        // Definimos el comportamiento del mock repositorio.
        when(pr.findById(Long.valueOf(1))).thenReturn(Optional.of(p1));

        // LLamamos al método .buscarPorId() del productoService.
        Producto p3 = ps.buscarPorId(p1.getId_producto());

        // Verificaciones.
        // Verificamos que p3 no sea nulo.
        assertNotNull(p3);
        // Verificamos que el nombre de p3 sea igual al de p1.
        assertEquals(p3.getNombre(), p1.getNombre());
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).findById(Long.valueOf(1));
    }

    // Probando .buscarPorId() cuando no encuentra el producto.
    @Test
    void probarBuscarPorIdMal() {

    }

    // Probamos .guardar()
    @Test
    void probarGuardar() {
        // Definimos el comportamiento del mock repositorio.
        when(pr.save(any(Producto.class))).thenReturn(p1);

        // LLamamos al método .guardar() del productoService.
        Producto p3 = ps.guardar(p1);

        // Verificaciones.
        // Verificamos que p3 no sea nulo.
        assertNotNull(p3);
        // Verificamos que el nombre de p3 sea igual al nombre de p1.
        assertEquals(p3.getNombre(), p1.getNombre());
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).save(p1);
    }

    // Provamos .borrar()
    @Test
    void probarBorrar() {
        // Definimos el comportamiento del mock repositorio.
        doNothing().when(pr).deleteById(p1.getId_producto());

        // LLamamos al método .borrar() del productoService.
        ps.borrar(p1.getId_producto());

        // Verificaciones.
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).deleteById(p1.getId_producto());
    }

    // Probamos .buscarPorNombre() cuando encuentra el producto.
    @Test
    void probarBuscarPorNombreBien() {
        // Definimos el comportamiento del mock repositorio.
        List<Producto> productos = Arrays.asList(p1, p2);
        when(pr.findAll()).thenReturn(productos);

        // LLamamos al método .buscarPorNombre() del productoService.
        Producto p3 = ps.buscarPorNombre("bolsas papel");

        // Verificaciones.
        // Verificamos que p3 no sea nulo.
        assertNotNull(p3);
        // Verificamos que el nombre de p3 sea igual al de p1.
        assertEquals(p1.getNombre(), p3.getNombre());
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).findAll();
    }

    // Probamos .buscarPorNombre() cuando no encuentra el producto.
    @Test
    void probarBuscarPorNombreMal() {
        // Definimos el comportamiento del mock repositorio.
        List<Producto> productos = Arrays.asList(p1, p2);
        when(pr.findAll()).thenReturn(productos);

        // LLamamos al método .buscarPorNombre() del productoService (con un nombre que no existe).
        Producto p3 = ps.buscarPorNombre("lolsas papel");

        // Verificaciones.
        // Verificamos que p3 no sea nulo.
        assertNotNull(p3);
        // Verificamos que los valores de p3 sean nulos.
        assertNull(p3.getNombre());
        assertNull(p3.getId_producto());
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).findAll();
    }

    // Probamos .buscarTodosIds() cuando hay productos.
    @Test
    void probarBuscarTodosIdsBien() {
        // Definimos el comportamiento del mock repositorio.
        List<Producto> productos = Arrays.asList(p1, p2);
        when(pr.findAll()).thenReturn(productos);

        // LLamamos al método .buscarTodosIds() del productoService.
        List<Long> ids = ps.buscarTodosIds();

        // Verificaciones.
        // Verificamos la longitud de la lista.
        assertEquals(2, ids.size());
        // Verificamos que la lista contenga los ids.
        assertTrue(ids.contains(p1.getId_producto()));
        assertTrue(ids.contains(p2.getId_producto()));
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).findAll();
    }

    // Probamos .buscarTodosIds cuando no hay productos.
    @Test
    void probarBuscarTodosIdsMal() {
        // Definimos el comportamiento del mock repositorio.
        when(pr.findAll()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarTodosIds() del productoService.
        List<Long> ids = ps.buscarTodosIds();

        // Verificaciones.
        // Verificamos que la lista esá vacía.
        assertTrue(ids.isEmpty());
        // Verificamos que el método fue llamado una vez.
        verify(pr, times(1)).findAll();
    }
}
