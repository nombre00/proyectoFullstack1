package com.ventasWeb.cl.ventasWeb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping ("/api/v1/producto")
// @Tag es para la documentación.
@Tag(name = "Producto", description = "Son las operaciones relacionadas con los productos.")
public class ProductoController {

    // Creamos la variable que contiene la funcionalidad del service.
    @Autowired
    private ProductoService Pservice;

    // Métodos. 
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los productos.
    @GetMapping ("/listar")
    @Operation(summary = "Obtener todos los productos.", description = "Obtiene una lista con todos los productos.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron productos."),
    @ApiResponse(responseCode = "500", description = "Eror interno del sistema.")
    })
    public ResponseEntity<List<Producto>> buscarTodos(){
        // Buscamos los productos y los guardamos en una variable.
        List<Producto> productos = Pservice.buscarTodos();
        // Si no encontramos productos retornamos una respuesta vacía.
        if (productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos productos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(productos);
    }

    // Método para buscar por id.
    @GetMapping ("/{id}")
    @Operation(summary = "Obtener un producto por su id.", description = "Obtiene un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto."),
    @ApiResponse(responseCode = "500", description = "Eror interno del sistema.")
    })
    public ResponseEntity<Producto> buscarPorRun(@PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el producto en una variable.
            Producto p = Pservice.buscarPorId(id);
            // Retornamos una respuesta que contiene el producto.
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para buscar por nombre.
    @GetMapping ("/buscar/{nombre}")
    @Operation(summary = "Obtener un producto por su nombre.", description = "Obtiene un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto."),
    @ApiResponse(responseCode = "500", description = "Eror interno del sistema.")
    })
    public ResponseEntity<Producto> buscarPorNombre(@PathVariable String nombre){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el producto en una variable.
            Producto p = Pservice.buscarPorNombre(nombre);
            // Retornamos una respuesta que contiene el producto.
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método que guarda.
    @PostMapping ("/agregar")
    @Operation(summary = "Agrega un producto", description = "Crea un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Creación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se creó el producto."),
    @ApiResponse(responseCode = "500", description = "Eror interno del sistema.")
    })
    public ResponseEntity<Producto> guardar (@RequestBody Producto p){
        // Creamos una variable que contiene el nuevo producto y lo guardamos al mismo tiempo.
        Producto P = Pservice.guardar(p);
        // Retornamos una respuesta que contiene el producto.
        return ResponseEntity.ok(P);
    }

    // Método que borra.
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un producto", description = "Elimina un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto."),
    @ApiResponse(responseCode = "500", description = "Eror interno del sistema.")
    })
    public ResponseEntity<?> borrar(@PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            Pservice.borrar(id);
            // Retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            // Cualquier error tambien retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método que actualiza.
    // Los argumentos que recibe la funcion es un id para buscar el inventario a editar y un inventario nuevo,
    // los atributos de este inventario nuevo van a reemplazar los atributos del inventario encontrado.
    @PutMapping ("/{id}")
    @Operation(summary = "Actualiza un producto", description = "Actualiza un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se creó el producto."),
    @ApiResponse(responseCode = "500", description = "Eror interno del sistema.")
    })
    public ResponseEntity<Producto> actualizar (@PathVariable long id, @RequestBody Producto p){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el producto a editar y lo guardamos en una variable.
            Producto Pactualizado = Pservice.buscarPorId(id);
            // Reemplazamos los atributos.
            Pactualizado.setNombre(p.getNombre());
            Pactualizado.setCantidad(p.getCantidad());
            Pactualizado.setFecha_vencimiento(p.getFecha_vencimiento());
            Pactualizado.setMaterial(p.getMaterial());
            Pactualizado.setPrecio(p.getPrecio());
            Pactualizado.setRefrigerar(p.isRefrigerar());
            Pactualizado.setTalla(p.getTalla());
            // Guardamos los cambios.
            Pservice.guardar(Pactualizado);
            // Retornamos la respuesta con el inventario actualizado.
            return ResponseEntity.ok(Pactualizado);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
}
