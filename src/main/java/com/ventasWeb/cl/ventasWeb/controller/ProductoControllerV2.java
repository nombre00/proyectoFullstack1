package com.ventasWeb.cl.ventasWeb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventasWeb.cl.ventasWeb.assemblers.ProductoRepresentationModelAssembler;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping ("/api/v2/producto")
// @Tag es para la documentación.
@Tag(name = "Producto", description = "Son las operaciones relacionadas con los productos.")
public class ProductoControllerV2 {

    // Creamos la variable que contiene la funcionalidad del service.
    @Autowired
    private ProductoService Pservice;
    // Inicializamos un assembler para combertir los objetos de clase a entidadesModelo que contienen hiperLinks.
    private final ProductoRepresentationModelAssembler assembler = new ProductoRepresentationModelAssembler();

    // Métodos. 
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los productos.
    @GetMapping (value = "/listar", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Obtener todos los productos.", description = "Obtiene una lista con todos los productos.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "204", description = "Sin contenido."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public CollectionModel<EntityModel<Producto>> buscarTodos(){
        // Buscamos los productos y los transformamos con el assembler
        List<EntityModel<Producto>> productos = Pservice.buscarTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        // Si no encontramos productos, retornamos una respuesta vacía
        if (productos.isEmpty()) {
            return CollectionModel.empty();
        }

        // Si tenemos productos, los retornamos dentro de una respuesta con enlace self.
        return CollectionModel.of(productos,WebMvcLinkBuilder.
        linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).buscarTodos()).withSelfRel());
    }

    // Método para buscar por id.
    @GetMapping (value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Obtener un producto por su id.", description = "Obtiene un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos : @Parameter(description = "ID del producto a buscar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que swagger reconozca el argumento en la URL.
    public ResponseEntity<EntityModel<Producto>> buscarPorRun(@Parameter(description = "ID del producto a buscar", required = true)
    @PathVariable long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por ID y la guardamos en una variable.
            Producto producto = Pservice.buscarPorId(id);
            // Si el objeto está vacío retornamos una respuesta not found.
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Producto> entityModel = assembler.toModel(producto);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para buscar por nombre.
    @GetMapping (value = "/buscar/{nombre}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Obtener un producto por su nombre.", description = "Obtiene un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos @Parameter(description = "Nombre del producto a buscar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que suagger reconozca el argumento en la URL.
    public ResponseEntity<EntityModel<Producto>> buscarPorNombre(@Parameter(description = "Nombre del producto a buscar", required = true)
    @PathVariable String nombre){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y transformamos con el assembler.
            Producto producto = Pservice.buscarPorNombre(nombre);
            if (producto == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            EntityModel<Producto> entidadModelo = assembler.toModel(producto);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entidadModelo);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método que guarda.
    @PostMapping (value = "/agregar", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Agrega un producto", description = "Crea un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "201", description = "Creación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Producto>> guardar (
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo producto",
            required = true,
            content = @Content(schema = @Schema(implementation = Producto.class))
        )
        @RequestBody Producto p ){

            // Guardamos el producto nuevo en la base de datos.
            Producto producto = Pservice.guardar(p);
            // Creamos una variable que contiene el nuevo producto y lo transformamos con el assembler.
            EntityModel<Producto> entidadModelo = assembler.toModel(producto);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entidadModelo);
    }

    // Método que borra.
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Elimina un producto", description = "Elimina un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el producto."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos: @Parameter(description = "ID del producto a buscar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que suagger reconozca el argumento en la URL.
    public ResponseEntity<?> borrar(@Parameter(description = "ID del producto a eliminar", required = true)
        @PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            Pservice.borrar(id);
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
    @PutMapping (value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Actualiza un producto", description = "Actualiza un producto.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json",
        schema = @Schema (implementation = Producto.class))),
    @ApiResponse(responseCode = "400", description = "Solicitud inválida (datos incorrectos)."),
    @ApiResponse(responseCode = "404", description = "no se creó el producto."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Producto>> actualizar (
        // Parameter es para el swagger.
        @Parameter(description = "ID del producto a actualizar", required = true)
        @PathVariable long id, 
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevos datos del producto",
            required = true,
            content = @Content(schema = @Schema(implementation = Producto.class))
        )
        @RequestBody Producto p){
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
            // Con assembler los transformamos en un entityModel.
            EntityModel<Producto> entidadModelo = assembler.toModel(Pactualizado);
            // Retornamos la respuesta con el inventario actualizado.
            return ResponseEntity.ok(entidadModelo);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
