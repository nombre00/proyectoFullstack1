package com.ventasWeb.cl.ventasWeb.service;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.ventasWeb.cl.ventasWeb.repository.CarritoRepository;

// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {

    // Creamos el mock del repositorio para simular su comportamiento.
    @Mock
    private CarritoRepository cr;

    // Creamos una istancia de CarritoService y le inyectamos los mocks que creamos.
    @InjectMocks
    private CarritoService cs;

    // Preparamos las variables que vamos a usar en los tests.
    // Declaramos las variables.
    private Carrito c1;
    private Carrito c2;

    // Generamos una acción que se hace antes de cada test, esta acción es inicializar las variables.
    @BeforeEach
    void setUp(){
        c1 = new Carrito();
        c2 = new Carrito();
        c1.setId_carrito(Long.valueOf(1));
        c2.setId_carrito(Long.valueOf(2));
        c1.setDetallesCarrito(new ArrayList<DetalleCarrito>());
        c2.setDetallesCarrito(new ArrayList<DetalleCarrito>());
        c1.setPagado(false);
        c2.setPagado(false);
    }

    // Métodos.
    // Probando .buscarTodos() cuando hay carritos.
    @Test
    void probarBuscarTodosBien(){
        // Definimos el comportamiento del Mock repositorio.
        // Hacemos una lista y le agregamos los carritos.
        List<Carrito> carritos = Arrays.asList(c1,c2);
        when(cr.findAll()).thenReturn(carritos);

        // LLamamos el método .buscarTodos()
        List<Carrito> carritos2 = cs.buscarTodos();

        // Verificaciones.
        // Verificamos la longitud de la lista.
        assertEquals(2, carritos2.size());
        // Verificamos datos de la lista.
        assertEquals(1, carritos2.get(0).getId_carrito());
        assertEquals(2, carritos2.get(1).getId_carrito());
        assertFalse(carritos2.get(0).isPagado());
        assertFalse(carritos2.get(1).isPagado());
        // Verificamos que el método haya sido llamado una vez.
        verify(cr, times(1)).findAll();
    }

    // Probando .buscarTodos() cuando no hay productos.
    @Test
    void probarBuscarTodosMal() {
        // Definimos el comportamiento del mock repositorio.
        when(cr.findAll()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarTodos() del carritoService.
        List<Carrito> carritos2 = cs.buscarTodos();

        // Verificaciones.
        // Verificamos que la lista esté vacía.
        assertTrue(carritos2.isEmpty());
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).findAll();
    }

    // Probando .buscarPorId() cuando encuentra un carrito.
    @Test
    void probarBuscarPorIdBien() {
        // Definimos el comportamiento del mock repositorio.
        when(cr.findById(Long.valueOf(1))).thenReturn(Optional.of(c1));

        // LLamamos al método .buscarPorId() del carritoService.
        Carrito c3 = cs.buscarPorId(c1.getId_carrito());

        // Verificaciones.
        // Verificamos que c3 no sea nulo.
        assertNotNull(c3);
        // Verificamos que el id de c3 sea igual al de c1.
        assertEquals(c1.getId_carrito(), c3.getId_carrito());
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).findById(Long.valueOf(1));
    }

    // Probamos .guardar.
    @Test
    void probarGuardar() {
        // Definimos el comportamiento del mock repositorio.
        when(cr.save(any(Carrito.class))).thenReturn(c1);

        // LLamamos al método .guardar() de carritoService.
        Carrito c3 = cs.guardar(c1);

        // Verificaciones.
        // Verificamos que c3 no sea nulo.
        assertNotNull(c3);
        // Verificamos que el id de c3 sea igual al de c1.
        assertEquals(c1.getId_carrito(), c3.getId_carrito());
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).save(c1);
    }

    // Probamos .borrar().
    @Test
    void probarBorrar() {
        // Definimos el comportamiento del mock repositorio.
        doNothing().when(cr).deleteById(c1.getId_carrito());

        // LLamamos al método .borrar() de carritoService.
        cs.borrar(c1.getId_carrito());

        // Verificaciones.
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).deleteById(c1.getId_carrito());
    }

    // Probamos calcularValorCarrito().
    @Test
    void probarValor() {

    }

}
