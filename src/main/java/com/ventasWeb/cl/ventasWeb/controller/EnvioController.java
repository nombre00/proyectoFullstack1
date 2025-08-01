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

import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.model.Envio;
import com.ventasWeb.cl.ventasWeb.service.EnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping ("/api/v1/envio")
// @Tag es para la documentación.
@Tag(name = "Envio", description = "Son las operaciones relacionadas con los envios.")
public class EnvioController {

    // Creamos una variable que contiene la funcionalidad del service.
    @Autowired
    private EnvioService es;

    // Métodos.

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los envios.
    @GetMapping("/listar")
    @Operation(summary = "Obtener todos los envios.", description = "Obtiene una lista con todos los envios.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron envios."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<List<Envio>> buscarTodos(){
        // Buscamos los envios y los guardamos en una variable.
        List<Envio> envios = es.buscarTodos();
        // Si no encontramos envios retornamos una respuesta vacía.
        if (envios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos carritos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(envios);
    }

    // Método que busca por id.
    @GetMapping ("/{id}")
    @Operation(summary = "Obtener un envio por su id.", description = "Obtiene un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Envio> buscarPorId(@Parameter(description = "ID del envio a buscar", required = true)
        @PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el envio en una variable.
            Envio s = es.buscarPorId(id);
            // Retornamos una respuesta que contiene el carrito.
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping ("/agregar")
    @Operation(summary = "Guardar un envio.", description = "Guarda un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Creación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Envio> guardar (
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo envío",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody Envio e){
        // Creamos una variable que contiene el nuevo envio y lo guardamos al mismo tiempo.
        Envio E = es.guardar(e);
        // Retornamos una respuesta que contiene el envio.
        return ResponseEntity.ok(E);
    }

    // Método que borra.
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un envio por su id.", description = "Elimina un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<?> borrar(@Parameter(description = "ID del envio a borrar", required = true)
        @PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            es.borrar(id);
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
    @Operation(summary = "Actualiza un envio por su id.", description = "Actualiza un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Envio> actualizar (
        @Parameter(description = "ID del envio a actualizar", required = true)@PathVariable long id, 
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos nuevos del envío",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody Envio en){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el envio a editar y lo guardamos en una variable.
            Envio EA = es.buscarPorId(id);
            // Reemplazamos los atributos.
            EA.setId_envio(id);
            EA.setCliente(en.getCliente());
            EA.setCarrito(en.getCarrito());
            EA.setDireccion(en.getDireccion());
            EA.setFecha(en.getFecha());
            // Guardamos los cambios.
            es.guardar(EA);
            // Retornamos la respuesta con el envio actualizado.
            return ResponseEntity.ok(EA);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para buscar estado del envio por id.
    @GetMapping ("/revisar_estado/{id}")
    @Operation(summary = "Obtener el estado de un envio por su id.", description = "Obtiene un estado de envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<String>estadoPorId(@Parameter(description = "ID del envio a buscar", required = true)
        @PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el pedido en una variable.
            Envio e = es.buscarPorId(id);
            // Guardamos el estado en una variable.
            String E = e.getEstado();
            // Retornamos una respuesta que contiene el estado.
            return ResponseEntity.ok(E);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
    // Método para editar el estado del pedido por id.
    @PutMapping ("/editar_estado/{id}")
    @Operation(summary = "Edita el estado de un envio por su id.", description = "Edita el estado de un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Edición ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Envio> editarEstado(
        @Parameter(description = "ID del envio a buscar", required = true)@PathVariable int id, 
        @Parameter(description = "Nuevo estado del envío", required = true)@PathVariable String estado){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el pedido en una variable.
            Envio e = es.buscarPorId(id);
            // Cambiamos el estado del pedido.
            e.setEstado(estado);
            // Guardamos los cambios hechos.
            es.guardar(e);
            // Retornamos una respuesta que contiene el pedido con el estado actualizado.
            return ResponseEntity.ok(e);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
}
