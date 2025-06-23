package com.ventasWeb.cl.ventasWeb.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.ventasWeb.cl.ventasWeb.assemblers.EnvioRepresentationModelAssembler;
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
@RequestMapping ("/api/v2/envio")
// @Tag es para la documentación.
@Tag(name = "Envio", description = "Son las operaciones relacionadas con los envios.")
public class EnvioControllerV2 {

    // Creamos una variable que contiene la funcionalidad del service.
    @Autowired
    private EnvioService es;
    // Inicializamos un assembler para combertir los objetos de clase a entidadesModelo que contienen hiperLinks.
    private static EnvioRepresentationModelAssembler assembler = new EnvioRepresentationModelAssembler();

    // Métodos.

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los envios.
    @GetMapping(value = "/listar", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener todos los envios.", description = "Obtiene una lista con todos los envios.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron envios."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public CollectionModel<EntityModel<Envio>> buscarTodos(){
        // Buscamos los envios y los transformamos con el assembler
        List<EntityModel<Envio>> envios = es.buscarTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        // Si no encontramos envios retornamos una respuesta vacía.
        if (envios.isEmpty()){
            return CollectionModel.empty();
        }
        // Si tenemos envios, los retornamos dentro de una respuesta con enlace self.
        return CollectionModel.of(envios,WebMvcLinkBuilder.
        linkTo(WebMvcLinkBuilder.methodOn(EnvioControllerV2.class).buscarTodos()).withSelfRel());
    }

    // Método que busca por id.
    @GetMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener un envio por su id.", description = "Obtiene un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Envio>> buscarPorId(@Parameter(description = "ID del envio a buscar", required = true)
        @PathVariable long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el envio en una variable.
            Envio envio = es.buscarPorId(id);
            // Si el objeto está vacío retornamos una respuesta not found.
            if (envio == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Envio> entityModel = assembler.toModel(envio);
            // Retornamos la envio con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para guardar.
    @PostMapping (value = "/agregar", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Guardar un envio.", description = "Guarda un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Creación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Envio>> guardar (
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
        // Transformamos el objeto en una entidadModelo con assembler.
        EntityModel<Envio> entityModel = assembler.toModel(E);
        // Retornamos la entidadModelo con el objeto y links.
        return ResponseEntity.ok(entityModel);
    }

    // Método que borra.
    @DeleteMapping(value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
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
    @Operation(summary = "Actualiza un envio por su id.", description = "Actualiza un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Envio>> actualizar (
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

            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Envio> entityModel = assembler.toModel(EA);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para buscar estado del envio por id.
    @GetMapping (value = "/revisar_estado/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener el estado de un envio por su id.", description = "Obtiene un estado de envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envio."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public EntityModel<Map<String, String>>estadoPorId(@Parameter(description = "ID del envio a buscar", required = true)
        @PathVariable long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el pedido en una variable.
            Envio e = es.buscarPorId(id);
            // Guardamos el estado en una variable.
            String E = e.getEstado();
            // Guardamos la variable dentro de un Map ya que hateoas espera una estructura con clave-valor.
            Map<String,String> respuesta = new HashMap<>();
            respuesta.put("clave", E);
            // Retornamos una respuesta que contiene el estado.
            return EntityModel.of(respuesta,
            linkTo(methodOn(EnvioControllerV2.class).buscarPorId(id)).withSelfRel(),
            linkTo(methodOn(ClienteControllerV2.class).buscarPorRun(e.getCliente().getRun_cliente())).withRel("Comprador.")
            );
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return EntityModel.of(null);
        }
    }
    // Método para editar el estado del envío por id.
    @PutMapping (value = "/editar_estado/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Edita el estado de un envío por su id.", description = "Edita el estado de un envío.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Edición ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el envío."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Envio>> editarEstado(
        @Parameter(description = "ID del envío a buscar", required = true)@PathVariable int id, 
        @Parameter(description = "Nuevo estado del envío", required = true)@PathVariable String estado){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por id y guardamos el pedido en una variable.
            Envio e = es.buscarPorId(id);
            // Cambiamos el estado del envío.
            e.setEstado(estado);
            // Guardamos los cambios hechos.
            es.guardar(e);

            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Envio> entityModel = assembler.toModel(e);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no encontramos lo que buscamos devolvemos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }
}