package com.ventasWeb.cl.ventasWeb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.ventasWeb.cl.ventasWeb.assemblers.CarritoRepresentationModelAssembler;
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
@RequestMapping ("/api/v2/carrito")
// @Tag es para la documentación.
@Tag(name = "Carrito", description = "Son las operaciones relacionadas con los carritos de compra.")
public class CarritoControllerV2 {

    // Creamos una variable que contiene la funcionalidad del service.
    @Autowired
    private CarritoService cs;
    @Autowired
    private ClienteService cls;
    // Inicializamos un assembler para combertir los objetos de clase a entidadesModelo que contienen hiperLinks.
    private static CarritoRepresentationModelAssembler assembler = new CarritoRepresentationModelAssembler();

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los carritos.
    @GetMapping (value = "/listar", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener todos los carritos.", description = "Obtiene una lista con todos los carritos.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron carritos."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public CollectionModel<EntityModel<Carrito>> buscarTodos(){
        // Buscamos los carritos y los transformamos con el assembler
        List<EntityModel<Carrito>> carritos = cs.buscarTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        // Si no encontramos carritos retornamos una respuesta vacía.
        if (carritos.isEmpty()){
            return CollectionModel.empty();
        }
        // Si tenemos carritos, los retornamos dentro de una respuesta con enlace self.
        return CollectionModel.of(carritos,WebMvcLinkBuilder.
        linkTo(WebMvcLinkBuilder.methodOn(CarritoControllerV2.class).buscarTodos()).withSelfRel());
    }

    // Método que busca por id.
    @GetMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
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
    public ResponseEntity<EntityModel<Carrito>> buscarPorRun(@Parameter(description = "ID del dueño del carrito a buscar", required = true)
        @PathVariable long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el carrito en una variable.
            Carrito carrito = cs.buscarPorId(id);
            // Si el objeto está vacío retornamos una respuesta not found.
            if (carrito == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Carrito> entityModel = assembler.toModel(carrito);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping(value = "/agregar", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Agrega un carrito nuevo.", description = "Agrega un carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Carrito agregado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Carrito>> guardar (
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
        // Transformamos el objeto en una entidadModelo con assembler.
        EntityModel<Carrito> entityModel = assembler.toModel(C);
        // Retornamos la entidadModelo con el objeto y links.
        return ResponseEntity.ok(entityModel);
    }

    // Método que borra.
    @DeleteMapping(value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e){
            // Cualquier error tambien retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método que actualiza.
    // Los argumentos que recibe la funcion es un id para buscar el inventario a editar y un inventario nuevo, 
    // los atributos de este inventario nuevo van a reemplazar los atributos del inventario encontrado.
    @PutMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Actualiza un carrito por su id.", description = "Actualiza un carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Carrito>> actualizar (
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
            // Buscamos el carrito a editar y lo guardamos en una variable.
            Carrito CA = cs.buscarPorId(id);
            // Reemplazamos los atributos.
            CA.setId_carrito(id); 
            CA.setDetallesCarrito(c.getDetallesCarrito());
            CA.setPagado(c.isPagado());
            // Guardamos los cambios.
            cs.guardar(CA);

            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Carrito> entityModel = assembler.toModel(CA);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }  

    // Agregar producto al carrito.
    @PostMapping(value = "/agregar-producto/{id_producto}/{run_cliente}/{cantidad}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Agrega un carrito al producto.", description = "Agrega un producto al carrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "El producto se agregó al carrito exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Carrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto y/o carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Carrito>> agregarProductoCarrito(
        // Parameter es para el swagger.
        @Parameter(description = "ID del producto que vamos a agregar", required = true)@PathVariable long id_producto, 
        @Parameter(description = "ID del cliente", required = true)@PathVariable long run_cliente, 
        @Parameter(description = "cantidad del producto", required = true)@PathVariable int cantidad) {
        // LLamamos a la funcion que hace la pega.
        cs.agregarProductoCarrito(id_producto, run_cliente, cantidad);
        Cliente cliente = cls.buscarPorId(run_cliente);
        Carrito carrito = cliente.getCarrito();

        // Transformamos el objeto en una entidadModelo con assembler.
        EntityModel<Carrito> entityModel = assembler.toModel(carrito);
        // Retornamos la entidadModelo con el objeto y links.
        return ResponseEntity.ok(entityModel);
    }
    
    
}