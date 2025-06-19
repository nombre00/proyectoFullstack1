package com.ventasWeb.cl.ventasWeb.service;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.repository.ClienteRepository;

// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    // Creamos el mock del repositorio para simular su comportamiento.
    @Mock
    private ClienteRepository cr;

    // Creamos una instancia de ClienteService y le inyectamos los moks.
    @InjectMocks
    private ClienteService cs;


    // Preparamos las variables que vamos a usar en los tests.
    // Declaramos las variables.
    Cliente cliente1;
    Cliente cliente2;

    // Generamos una acción que se hace antes de cada test, esta acción inicializa las variables.
    @BeforeEach
    void setUp() {
        cliente1 = new Cliente();
        cliente1.setRun_cliente(1L);
        cliente1.setClave("clave");
        cliente1.setDireccion("direccion");
        cliente1.setNombre_completo("nombre completo");
        cliente2 = new Cliente();
        cliente2.setRun_cliente(2L);
        cliente2.setClave("clave 2");
        cliente2.setDireccion("direccion 2");
        cliente2.setNombre_completo("nombre completo segundo");
    }


    // Métodos.
    // Probamos .buscarTodos() cuando hay clientes.
    @Test
    void probarBuscarTodosBien() {
        // Definimos el comportamiento del mock.
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(cr.findAll()).thenReturn(clientes);

        // LLamamos al método .buscarTodos() 
        List<Cliente> resultado = cs.buscarTodos();

        // Verificaciones.
        // Verificamos la longitud de la lista resultado.
        assertEquals(2, resultado.size());
        // Verificamos un dato de cada cliente de la lista.
        assertEquals("nombre completo", resultado.get(0).getNombre_completo());
        assertEquals("nombre completo segundo", resultado.get(1).getNombre_completo());
        // Verificamos que el mock fue llamado una vez.
        verify(cr, times(1)).findAll();
    }

    // Probando .buscarTodosMal()
    @Test
    void probarBuscarTodosMal() {
        // Definimos el comportamiento del mock.
        when(cr.findAll()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarTodosMal()
        List<Cliente> resultado = cs.buscarTodos();

        // Verificiones.
        // Verificamos que la lista esté vacía.
        assertTrue(resultado.isEmpty());
        // Verificamos que el mock fue llamado una vez.
        verify(cr, times(1)).findAll();
    }

    // Probando .buscarPorId() cuando encuentra un cliente.
    @Test
    void probarBuscarPorIdBien() {
        // Definimos el comportamiento del mock.
        when(cr.findById(1L)).thenReturn(Optional.of(cliente1));

        // LLamamos al método .buscarPorId()
        Cliente resultado = cs.buscarPorId(1L);

        // Verificaciones.
        // Verificamos que el nombre coincida con el de cliente1.
        assertEquals("nombre completo", resultado.getNombre_completo());
        // Verificamos que el mock fue llamado una vez.
        verify(cr, times(1)).findById(1L);
    }

    // Probamos .guardar()
    @Test
    void probarGuardar() {
        // Definimos el comportamiento del mock.
        when(cr.save(any(Cliente.class))).thenReturn(cliente1);

        // LLamamos al método .buardar()
        Cliente resultado = cs.guardar(cliente1);

        // Verificaciones.
        // Verificamos que resultado contenga nombre y clave de cliente1.
        assertEquals("nombre completo", resultado.getNombre_completo());
        assertEquals("clave", resultado.getClave());
        // Verificamos que el mock fue llamado una vez.
        verify(cr, times(1)).save(any(Cliente.class));
    }

    // Probamos .borrar()
    @Test
    void probarBorrar() {
        // Definimos el comportamiento del mock.
        doNothing().when(cr).deleteById(1L);

        // LLamamos al método .borrar()
        cs.borrar(1L);

        // Verificaciones.
        // Verificamos que el mock fue llamado una vez.
        verify(cr, times(1)).deleteById(1L);
    }
}
