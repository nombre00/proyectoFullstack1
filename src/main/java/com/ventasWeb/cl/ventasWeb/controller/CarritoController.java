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

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.service.CarritoService;
import com.ventasWeb.cl.ventasWeb.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping ("/api/v1/carrito")
// @Tag es para la documentación.
@Tag(name = "Carrito", description = "Son las operaciones relacionadas con los carritos de compra.")
public class CarritoController {

    // Creamos una variable que contiene la funcionalidad del service.
    @Autowired
    private CarritoService cs;
    @Autowired
    private ClienteService cls;

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los carritos.
    @GetMapping ("/listar")
    @Operation(summary = "Obtener todos los carritos.", description = "Obtiene una lista con todos los carritos.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron carritos."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<List<Carrito>> buscarTodos(){
        // Buscamos los carritos y los guardamos en una variable.
        List<Carrito> carritos = cs.buscarTodos();
        // Si no encontramos carritos retornamos una respuesta vacía.
        if (carritos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si tenemos carritos los retornamos dentro de una respuesta.
        return ResponseEntity.ok(carritos);
    }

    // Método que busca por id.
    @GetMapping ("/{id}")
    @Operation(summary = "Busca un carrito por id.", description = "Obtiene un carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos : @Parameter(description = "ID del carrito a buscar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que swagger reconozca el argumento en la URL.
    public ResponseEntity<Carrito> buscarPorRun(@Parameter(description = "ID del carrito a buscar", required = true)
        @PathVariable int id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el carrito en una variable.
            Carrito c = cs.buscarPorId(id);
            // Retornamos una respuesta que contiene el carrito.
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping("/agregar")
    @Operation(summary = "Agrega un carrito nuevo.", description = "Agrega un carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Carrito agregado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Carrito> guardar (
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo carrito",
            required = true,
            content = @Content(schema = @Schema(implementation = Carrito.class))
        )
        @RequestBody Carrito c){
        // Creamos una variable que contiene el nuevo carrito y lo guardamos al mismo tiempo.
        Carrito C = cs.guardar(c);
        // Retornamos una respuesta que contiene el carrito.
        return ResponseEntity.ok(C);
    }

    // Método que borra.
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un carrito por su id.", description = "Elimina un carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos : @Parameter(description = "ID del carrito a eliminar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que swagger reconozca el argumento en la URL.
    public ResponseEntity<?> borrar(@Parameter(description = "ID del carrito a eliminar", required = true)
        @PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            cs.borrar(id);
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
    @Operation(summary = "Actualiza un carrito por su id.", description = "Actualiza un carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Carrito> actualizar (
        // Parameter es para el swagger.
        @Parameter(description = "ID del carrito a actualizar", required = true)
        @PathVariable long id, 
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevos datos del carrito",
            required = true,
            content = @Content(schema = @Schema(implementation = Carrito.class))
        )
        @RequestBody Carrito c){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el producto a editar y lo guardamos en una variable.
            Carrito CA = cs.buscarPorId(id);
            // Reemplazamos los atributos.
            CA.setId_carrito(id); 
            CA.setDetallesCarrito(c.getDetallesCarrito());
            CA.setPagado(c.isPagado());
            // Guardamos los cambios.
            cs.guardar(CA);
            // Retornamos la respuesta con el inventario actualizado.
            return ResponseEntity.ok(CA);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    } 

    // Agregar producto al carrito.
    @PostMapping("/agregar-producto/{id_producto}/{run_cliente}/{cantidad}")
    @Operation(summary = "Agrega un carrito al producto.", description = "Agrega un producto al carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "El producto se agregó al carrito exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto y/o carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<Carrito> agregarProductoCarrito(
        // Parameter es para el swagger.
        @Parameter(description = "ID del producto que vamos a agregar", required = true)@PathVariable long id_producto, 
        @Parameter(description = "ID del cliente", required = true)@PathVariable long run_cliente, 
        @Parameter(description = "cantidad del producto", required = true)@PathVariable int cantidad) {
        // LLamamos a la funcion que hace la pega.
        cs.agregarProductoCarrito(id_producto, run_cliente, cantidad);
        Cliente cliente = cls.buscarPorId(run_cliente);
        Carrito carrito = cliente.getCarrito();
        // Retornamos el carrito actualizado.
        return ResponseEntity.ok(carrito);
    }
    
    
}
