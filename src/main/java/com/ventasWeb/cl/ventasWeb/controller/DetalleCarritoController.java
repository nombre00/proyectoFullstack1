package com.ventasWeb.cl.ventasWeb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.service.DetalleCarritoService;


@RestController
@RequestMapping ("/api/v1/detalle_carrito")
public class DetalleCarritoController {

    // Creamos la variable que contiene la funcionalidad del service.
    @Autowired
    private DetalleCarritoService DCS;

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los detalleCarrito.
    @GetMapping("/listar")
    public ResponseEntity<List<DetalleCarrito>> buscarTodos(){
        // Buscamos los detalles y los guardamos en una variable.
        List<DetalleCarrito> detalles = DCS.buscarTodos();
        // Si no encontramos detalles retornamos una respuesta vacía.
        if (detalles.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos detalles los retornamos dentro de una respuesta.
        return ResponseEntity.ok(detalles);
    }

    // Método para buscar por id.
    @GetMapping ("/{id}")
    public ResponseEntity<DetalleCarrito> buscarPorRun(@PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el detalle en una variable.
            DetalleCarrito dc = DCS.buscarPorId(id);
            // Retornamos una respuesta que contiene el detalle.
            return ResponseEntity.ok(dc);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping("/agregar")
    public ResponseEntity<DetalleCarrito> guardar (@RequestBody DetalleCarrito dc){
        // Creamos una variable que contiene el nuevo detalle y lo guardamos al mismo tiempo.
        DetalleCarrito DC = DCS.guardar(dc);
        // Retornamos una respuesta que contiene el detalle.
        return ResponseEntity.ok(DC);
    }

    // Método que borra.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            DCS.borrar(id);
            // Retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            // Cualquier error tambien retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método que actualiza.
    // Los argumentos que recibe la funcion es un id para buscar el detalle a editar y un detalle nuevo,
    // los atributos de este detalle nuevo van a reemplazar los atributos del detalle encontrado.
    @PutMapping ("/{id}")
    public ResponseEntity<DetalleCarrito> actualizar (@PathVariable long id, @RequestBody DetalleCarrito dc){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el detalle a editar y lo guardamos en una variable.
            DetalleCarrito DCactualizado = DCS.buscarPorId(id);
            // Reemplazamos los atributos.
            DCactualizado.setId_detalleCarrito(id);
            DCactualizado.setCantidadSolicitada(dc.getCantidadSolicitada());
            DCactualizado.setProducto(dc.getProducto());
            // Guardamos los cambios.
            DCS.guardar(DCactualizado);
            // Retornamos la respuesta con el detalle actualizado.
            return ResponseEntity.ok(DCactualizado);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
}



    
