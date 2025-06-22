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
import org.springframework.http.ResponseEntity;

import com.ventasWeb.cl.ventasWeb.assemblers.DetalleCarritoRepresentationModelAssembler;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.service.DetalleCarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping ("/api/v2/detalle_carrito")
// @Tag es para la documentación.
@Tag(name = "DetalleCarrito", description = "Son las operaciones relacionadas con los DetalleCarrito.")
public class DetalleCarritoControllerV2 {

    // Creamos la variable que contiene la funcionalidad del service.
    @Autowired
    private DetalleCarritoService DCS;
    // Inicializamos un assembler para combertir los objetos de clase a entidadesModelo que contienen hiperLinks.
    private static DetalleCarritoRepresentationModelAssembler assembler = new DetalleCarritoRepresentationModelAssembler();

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los detalleCarrito.
    @GetMapping(value = "/listar", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Obtener todos los detalleCarrito.", description = "Obtiene una lista con todos los detalleCarrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = DetalleCarrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron detalleCarritos."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public CollectionModel<EntityModel<DetalleCarrito>> buscarTodos(){
        // Buscamos los DEtalleCarrito y los transformamos con el assembler
        List<EntityModel<DetalleCarrito>> detalles = DCS.buscarTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        // Si no encontramos detalles retornamos una respuesta vacía.
        if (detalles.isEmpty()){
            return CollectionModel.empty();
        }
        // Si tenemos detalles, los retornamos dentro de una respuesta con enlace self.
        return CollectionModel.of(detalles,WebMvcLinkBuilder.
        linkTo(WebMvcLinkBuilder.methodOn(DetalleCarritoControllerV2.class).buscarTodos()).withSelfRel());
    }

    // Método para buscar por id.
    @GetMapping (value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Obtener un detalleCarrito por su id.", description = "Obtiene un detalleCarrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = DetalleCarrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el detalleCarrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<DetalleCarrito>> buscarPorId(@Parameter(description = "ID del detalleCarrito a buscar", required = true)
        @PathVariable long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el detalle en una variable.
            DetalleCarrito detalle = DCS.buscarPorId(id);
            // Si el objeto está vacío retornamos una respuesta not found.
            if (detalle == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<DetalleCarrito> entityModel = assembler.toModel(detalle);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping(value = "/agregar", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Agrega un detalleCarrito nuevo.", description = "Agrega un DetalleCarrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "DetalleCarrito agregado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = DetalleCarrito.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<DetalleCarrito>> guardar (
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo DetalleCarrito",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody DetalleCarrito dc){
        // Creamos una variable que contiene el nuevo detalle y lo guardamos al mismo tiempo.
        DetalleCarrito DC = DCS.guardar(dc);
        // Transformamos el objeto en una entidadModelo con assembler.
        EntityModel<DetalleCarrito> entityModel = assembler.toModel(DC);
        // Retornamos la entidadModelo con el objeto y links.
        return ResponseEntity.ok(entityModel);
    }

    // Método que borra.
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Elimina un detalleCarrito por su id.", description = "Elimina un detalleCarrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = DetalleCarrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el detalleCarrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<?> borrar(@Parameter(description = "ID del detalleCarrito a eliminar", required = true)
        @PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            DCS.borrar(id);
            // Retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e){
            // Cualquier error tambien retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método que actualiza.
    // Los argumentos que recibe la funcion es un id para buscar el detalle a editar y un detalle nuevo,
    // los atributos de este detalle nuevo van a reemplazar los atributos del detalle encontrado.
    @PutMapping (value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    @Operation(summary = "Actualiza un detalleCarrito por su id.", description = "Actualiza un detalleCarrito.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = DetalleCarrito.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el detalleCarrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<DetalleCarrito>> actualizar (
        @Parameter(description = "ID del detalleCarrito a modificar", required = true)@PathVariable long id, 
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del DetalleCarrito que van a reemplazar los datos del detalleCarrito a modificar.",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody DetalleCarrito dc){
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

            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<DetalleCarrito> entityModel = assembler.toModel(DCactualizado);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}