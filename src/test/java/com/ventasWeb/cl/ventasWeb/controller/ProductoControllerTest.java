package com.ventasWeb.cl.ventasWeb.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.service.ProductoService;

// Antes usabamos @WebMvcTest y @MocjBeans pero me daba warnings amarillas, para evitar eso ahora usamos @Mock y @InjectMocks
// @WebMvcTest se usa para probar los controladores springMVC de forma aislada, se usa para pruebas unitarias.
//@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    // MockMvc permite simular peticiones HTTP para probar controladores sin conección.
    private MockMvc mmvc;

    // Mockeamos (fingimos) la funcionalidad del service de Producto.
    @Mock
    private ProductoService ps;

    //Generamos una instancia de ProductoController y le inyectamos la funcionalidad del mock ProductoService. 
    @InjectMocks
    private ProductoController pc;

    // Permite convertir objetos java a JSON y viceversa.
    @Autowired
    private ObjectMapper om;

    // Declaramos un producto para almacenar los datos de prueba.
    private Producto p;


    // Creamos un objeto que vamos a usar para cada prueba.
    @BeforeEach
    void setup(){
        // Inicializa los mocks.
        MockitoAnnotations.openMocks(this);
        // Inicializamos objectMapper.
        om = new ObjectMapper();
        // Configuramos MockMvc manualmente, 
        // así evitamos cargar un constexto de spring.
        mmvc = MockMvcBuilders.standaloneSetup(pc).build();
        // Inicializamos el producto.
        p = new Producto();
        p.setId_producto(Long.valueOf(1));
        p.setNombre("galletas integrales");
        p.setPrecio(1200);
        p.setCantidad(40);
        p.setMaterial("cartón");
        p.setFecha_vencimiento("12/08/2025");
        p.setRefrigerar(false);
    }


    // Métodos Test.
    // Este test verifica el endpoint GET /api/v1/producto/listar cuando hay productos disponibles.
    @Test
    void probarListarBien() throws Exception{
        // Configuramos el Mock ProductoService para que .buscarTodos() devuelva una lista con p dentro.
        // Creamos una lista y le agregamos p.
        List<Producto> productos = Arrays.asList(p);
        // Fingimos el método.
        when(ps.buscarTodos()).thenReturn(productos);

        // Fingimos un GET y le pasamos la ruta.
        mmvc.perform(get("/api/v1/producto/listar"))
                // Verificamos la respuesta HTTP 200.
                .andExpect(status().isOk())
                // Verificamos que recibimos un JSON.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verificamos el id del primer producto.
                .andExpect(jsonPath("$[0].id_producto").value(p.getId_producto()))
                // Verificamos el nombre del producto.
                .andExpect(jsonPath("$[0].nombre").value(p.getNombre()))
                // Verificamos el precio.
                .andExpect(jsonPath("$[0].precio").value(p.getPrecio()));

        // Verificamos que el mock fue llamado exactamente una vez.
        verify(ps, times(1)).buscarTodos();
    }

    // Este test verifica el endpoint GET /api/v1/producto/listar cuando no hay productos disponibles.
    @Test
    void probarListarMal() throws Exception{
        // Configuramos el Mock para que devuelva una lista vacía.
        when(ps.buscarTodos()).thenReturn(List.of());

        // Fingimos un GET y le pasamos la ruta.
        mmvc.perform(get("/api/v1/producto/listar"))
            // Verificamos que la respuesta es HTTP 204, no content.
            .andExpect(status().isNoContent());
        
        // Verificamos que el método buscarTodos() del servicio fue llamado exactamente una vez.
        verify(ps, times(1)).buscarTodos();
    }

    // Este test verifica el endpoint GET /api/v1/producto/{id} cuando hay productos.
    @Test
    void probarBuscarPorRunBien() throws Exception{
        // Configuramos el Mock ProductoService para que .buscarPorId(1) devuelva p.
        when(ps.buscarPorId(1)).thenReturn(p);

        // Fingimos un GET y le pasamos la ruta.
        mmvc.perform(get("/api/v1/producto/1"))
            // Verificamos que la respuesta es HTTP 200.
            .andExpect(status().isOk())
            // Verificamos que recibimos un JSON.
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Verificamos que el id del Producto retornado es 1.
            .andExpect(jsonPath("$.id_producto").value(p.getId_producto()))
            // Verificamos que el nombre del producto es el que corresponde.
            .andExpect(jsonPath("$.nombre").value(p.getNombre()));
        
        // Verificamos que el servicio fue llamado con el id correcto.
        verify(ps, times(1)).buscarPorId(1);
    }

    // Este test verifica el endpoint GET /api/v1/producto/{id} cuando no hay productos.
    @Test
    void probarBuscarPorRunMal() throws Exception {
        // Configuramos el ProductoService para que .buscarPorId(1) lance una excepción.
        when(ps.buscarPorId(1)).thenThrow(new RuntimeException("Producto no encontrado."));

        // Fingimos un GET y le pasamos la ruta.
        mmvc.perform(get("/api/v1/producto/1"))
            // Verificamos que la respuesta es HTTP 204, no content.
            .andExpect(status().isNoContent());

        // Verificamos que el servicio fue llamado con el id correcto.
        verify(ps, times(1)).buscarPorId(1);
    }

    // Este test verifica el endpoint GET /api/v1/producto/buscar/{nombre} cuando hay productos.
    @Test
    void probarBuscarPorNombreBien() throws Exception {
        // Configuramos el ProductoService para que .buscarPorNombre("galletas integrales") retorne p.
        when(ps.buscarPorNombre("galletas integrales")).thenReturn(p);

        // Fingimos un GET y le pasamos la ruta.
        mmvc.perform(get("/api/v1/producto/buscar/galletas integrales"))
            // Verificamos que la respuesta es HTTP 200.
            .andExpect(status().isOk())
            // Verificamos que recibimos un JSON.
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Verificamos que el nombre es el que corresponde.
            .andExpect(jsonPath("$.nombre").value(p.getNombre()));
        
        // Verificamos que el servicio fue llamado con el nombre correcto.
        verify(ps, times(1)).buscarPorNombre("galletas integrales");
    }

    // Este test verifica el endpoint GET /api/v1/producto/buscar/{nombre} cuando no hay productos.
    @Test
    void probarBuscarPorNombreMal() throws Exception {
        // Configuramos el ProductoService para que .buscarPorNombre("galletas integrales") lance una excepción.
        when(ps.buscarPorNombre("galletas integrales")).thenThrow(new RuntimeException("Producto no encontrado."));

        // Fingimos un GET y le pasamos la ruta.
        mmvc.perform(get("/api/v1/producto/buscar/galletas integrales"))
            // Verificamos que la respuesta es HTTP 204, no content.
            .andExpect(status().isNoContent());

        // Verificamos que el servicio fue llamado con el nombre correcto.
        verify(ps, times(1)).buscarPorNombre("galletas integrales");
    }

    // Este test verifica el endpoint POST /api/v1/producto/agregar para crear un producto.
    @Test
    void probarGuardarBien() throws Exception {
        // Configuramos el ProductoService para que .guardar(p) devuelva el producto creado.
        when(ps.guardar(any(Producto.class))).thenReturn(p);

        // Fingimos un POST y le pasamos la ruta.
        mmvc.perform(post("/api/v1/producto/agregar")
                // Especificamos que el cuerpo es JSON.
                .contentType(MediaType.APPLICATION_JSON)
                // Convertimos p a JSON.
                .content(om.writeValueAsString(p)))
            // Verificamos que la respuesta es HTTP 200.
            .andExpect(status().isOk())
            // Verificamos que la respuesta es JSON.
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Verificamos que los datos retornados son los que corresponden.
            .andExpect(jsonPath("$.id_producto").value(p.getId_producto()))
            .andExpect(jsonPath("$.nombre").value(p.getNombre()));

        // Verificamos que el servicio fue llamado una vez para guardar p.
        verify(ps, times(1)).guardar(any(Producto.class));
    }

    // Este test verifica el endpoint DELETE /api/v1/producto/{id} cuando el producto existe.
    @Test
    void probarBorrarBien() throws Exception {
        // Configuramos el ProductoService para que .borrar() no lance excepciones.
        doNothing().when(ps).borrar(1);

        // Fingimos un DELETE y le pasamos la ruta.
        mmvc.perform(delete("/api/v1/producto/1"))
            // Verificamos que la respuesta HTTP es 204, no contenido.
            .andExpect(status().isNoContent());

        // Verificamos que el servicio fue llamado 1 vez y con el id que corresponde.
        verify(ps, times(1)).borrar(1);
    }

    // Este test verifica el endpoint DELETE /api/v1/producto/{id} cuando el producto no existe.
    @Test
    void probarBorrarMal() throws Exception {
        // Configuramos el ProductoService para que .borrar() lance una excepción.
        doThrow(new RuntimeException("Producto no encontrado.")).when(ps).borrar(1);

        // Fingimos un DELETE y le pasamos la ruta.
        mmvc.perform(delete("/api/v1/producto/1"))
            // Verificamos que la respuesta HTTP es 204, no contenido.
            .andExpect(status().isNoContent());

        // Verificamos que el servicio fue llamado 1 vez y con el id que corresponde.
        verify(ps, times(1)).borrar(1);
    }

    // Este test verifica el endpoint PUT /api/v1/producto/{id} para actualizar un producto.
    @Test
    void probarActualizarBien() throws Exception {
        // Creamos un producto nuevo con datos actualizados.
        Producto p2 = new Producto();
        // Generamos los datos que vamos a dar de argumentos.
        p2.setId_producto(Long.valueOf(1));
        p2.setNombre("galletas integrales chocolate");
        p2.setPrecio(1400);
        p2.setCantidad(44);
        p2.setMaterial("cartón");
        p2.setFecha_vencimiento("12/09/2025");
        p2.setRefrigerar(false);

        // Configuramos el ProductoService para que .buscarPorId(1) devuelva p.
        when(ps.buscarPorId(1)).thenReturn(p);
        // Configuramos el ProductoService para que .guardar(1) retorne p2.
        when(ps.guardar(any(Producto.class))).thenReturn(p2);

        // Fingimos POST y le pasamos la ruta.
        mmvc.perform(put("/api/v1/producto/1")
                // Especificamos que el cuerpo es JSON.
                .contentType(MediaType.APPLICATION_JSON)
                // Convertimos p2 a JSON.
                .content(om.writeValueAsString(p2)))
            // Verificamos que la respuesta es HTTP 200.
            .andExpect(status().isOk())
            // Verificamos que recibimos un JSON.
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Verificamos los datos actualizados.
            .andExpect(jsonPath("$.nombre").value(p2.getNombre()))
            .andExpect(jsonPath("$.precio").value(p2.getPrecio()))
            .andExpect(jsonPath("$.cantidad").value(p2.getCantidad()));

        // Verificamos que el servicio fue llamado para buscar y guardar el producto.
        verify(ps, times(1)).buscarPorId(1);
        verify(ps, times(1)).guardar(any(Producto.class));
    }

    // Este test verifica el endpoint PUT /api/v1/producto/{id} cuando el producto no existe.
    @Test
    void probarActualizarMal() throws Exception {
        // Configuramos ProductoService para que .buscarPorId(1) lance una excepción.
        when(ps.buscarPorId(1)).thenThrow(new RuntimeException("Producto no encontrado."));

        // Fingimos POST y le pasamos la ruta.
        mmvc.perform(put("/api/v1/producto/1")
                // Especificamos que el cuerpo es JSON.
                .contentType(MediaType.APPLICATION_JSON)
                // Convertimos p a JSON.
                .content(om.writeValueAsString(p)))
                // Verificamos que la respuesta es HTTP 204, no content.
            .andExpect(status().isNoContent());

        // Verificamos que el servicio fue llamado para buscar, pero no para guardar.
        verify(ps, times(1)).buscarPorId(1);
        verify(ps, never()).guardar(any(Producto.class));
    }
}
