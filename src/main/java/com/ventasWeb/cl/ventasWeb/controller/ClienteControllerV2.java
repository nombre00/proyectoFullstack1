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

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.service.CarritoService;
import com.ventasWeb.cl.ventasWeb.service.ClienteService;

import com.ventasWeb.cl.ventasWeb.assemblers.ClienteRepresentationModelAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;


@RestController
@RequestMapping ("/api/v2/cliente")
// @Tag es para la documentación.
@Tag(name = "Cliente", description = "Son las operaciones relacionadas con los clientes.")
public class ClienteControllerV2 {

    // Creamos la variable que contiene la funcionalidad del service.
    @Autowired
    private ClienteService cs;
    @Autowired
    private CarritoService carS;
    // Inicializamos un assembler para combertir los objetos de clase a entidadesModelo que contienen hiperLinks.
    private final ClienteRepresentationModelAssembler assembler = new ClienteRepresentationModelAssembler();

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica don una página web.

    // Método que lista los clientes.
    @GetMapping (value = "/listar", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener todos los clientes.", description = "Obtiene una lista con todos los clientes.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron clientes."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public CollectionModel<EntityModel<Cliente>> buscarTodos(){
        // Buscamos los clientes y los transformamos con el assembler
        List<EntityModel<Cliente>> clientes = cs.buscarTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        // Si no encontramos clientes retornamos una respuesta vacía.
        if (clientes.isEmpty()){
            return CollectionModel.empty();
        }
        // Si tenemos clientes, los retornamos dentro de una respuesta con enlace self.
        return CollectionModel.of(clientes,WebMvcLinkBuilder.
        linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).buscarTodos()).withSelfRel());
    }

    // Método para buscar por id.
    @GetMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener un cliente por su id.", description = "Obtiene un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos : @Parameter(description = "ID del cliente a buscar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que swagger reconozca el argumento en la URL.
    public ResponseEntity<EntityModel<Cliente>> buscarPorRun(@Parameter(description = "ID del cliente a buscar", required = true)
        @PathVariable long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos por run y guardamos el cliente en una variable.
            Cliente cliente = cs.buscarPorId(id);
            // Si el objeto está vacío retornamos una respuesta not found.
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Cliente> entityModel = assembler.toModel(cliente);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si algo falla retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método que guarda.
    @PostMapping (value = "/agregar", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Agrega un cliente nuevo.", description = "Agrega un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Cliente agregado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Cliente>> guardar (
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo cliente",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody Cliente c){
        // Creamos una variable que contiene el nuevo cliente y y revisamos si incluye un carrito, sino, lo creamos.
        Cliente C = c;
        if (C.getCarrito() == null){
            Carrito carrito = new Carrito();
            // Guardamos el carrito.
            carS.guardar(carrito);
            C.setCarrito(carrito);
        }
        // Guardamos el cliente nuevo con su carrito.
        cs.guardar(C);
        // Transformamos el objeto en una entidadModelo con assembler.
        EntityModel<Cliente> entityModel = assembler.toModel(C);
        // Retornamos la entidadModelo con el objeto y links.
        return ResponseEntity.ok(entityModel);
    }

    // Método que borra.
    @DeleteMapping(value = "/borrar/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Elimina un cliente por su id.", description = "Elimina un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    // Agregamos : @Parameter(description = "ID del cliente a eliminar", required = true).
    // Esto es para agregar una explicación de que hace esa variable y señalar que es obligatoria.
    // No es necesaria para que swagger reconozca el argumento en la URL.
    public ResponseEntity<?> borrar(@Parameter(description = "ID del cliente a eliminar", required = true)
        @PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Como acá no guardamos usamos el service directamente.
            cs.borrar(id);
            // Retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e){
            // Retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método que actualiza.
    // Los argumentos que recibe la funcion es un id para buscar el cliente a editar y un cliente nuevo,
    // los atributos de este cliente nuevo van a reemplazar los atributos del cliente encontrado.
    @PutMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Actualiza un cliente por su id.", description = "Actualiza un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Cliente>> actualizar (
        // Parameter es para el swagger.
        @Parameter(description = "ID del cliente a actualizar", required = true)
        @PathVariable long id, 
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevos datos del cliente",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody Cliente c){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos el cliente a editar y lo guardamos en una variable.
            Cliente Cactualizado = cs.buscarPorId(id);
            // Reemplazamos los atributos.
            Cactualizado.setRun_cliente(id);
            Cactualizado.setCarrito(c.getCarrito());
            Cactualizado.setDetallesCompras(c.getDetallesCompras());
            Cactualizado.setDireccion(c.getDireccion());
            Cactualizado.setDireccion(c.getDireccion());
            Cactualizado.setDv(c.getDv());
            Cactualizado.setHistorial_comprasWeb(c.getHistorial_comprasWeb());
            Cactualizado.setMail(c.getMail());
            Cactualizado.setNombre_completo(c.getNombre_completo());
            Cactualizado.setClave(c.getClave());
            // Guardamos los cambios.
            cs.guardar(Cactualizado);

            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<Cliente> entityModel = assembler.toModel(Cactualizado);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    } 

    // Método para ingresar a la cuenta.
    @GetMapping(value = "/ingresar_cuenta/{nombre}/{clave}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Simula un ingreso a la cuenta de un cliente", description = "Ingreso de sesión de un cliente.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Ingreso ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public EntityModel<Map<String, String>> ingresar(
        // Parameter es para el swagger.
        @Parameter(description = "Nombre del cliente", required = true)@PathVariable String nombre, 
        @Parameter(description = "Clave del cliente", required = true)@PathVariable String clave){
        // Creamos un string.
        String sentencia = "";
        // Le pasamos los argumentos a la función que revisa la clave e id.
        if (cs.ingresar(nombre, clave)){
            sentencia = "Ingresando a la página, bienvenido.";
            // Guardamos la variable dentro de un Map ya que hateoas espera una estructura con clave-valor.
            Map<String,String> respuesta = new HashMap<>();
            respuesta.put("clave", sentencia);
            // Retornamos la sentencia dentro de la respuesta.
            return EntityModel.of(respuesta,
            linkTo(methodOn(ClienteControllerV2.class).ingresar(nombre, clave)).withSelfRel(),
            linkTo(methodOn(ProductoControllerV2.class).buscarTodos()).withRel("Productos disponibles.")
            );
        }
        // Si no, retornamos una respuesta vacía.
        return EntityModel.of(null);
    }

    // Método que actualiza el carro del cliente, pasa un carrito que se pagó a la lista de carritos pagados,
    // y le pasa al cliente un nuevo carrito para comprar.
    @PutMapping(value = "/actualizarCarro/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Actualiza el carrito de un cliente por su id.", 
    description = "Obtiene un cliente, pasa su carrito pagado a la lista de carritos pagados y lo reempaza por un carrito nuevo.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Reemplazo ejecutado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el cliente."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<Cliente>> actualizarCarro(@Parameter(description = "Id del cliente", required = true)
    @PathVariable long id){
        // Buscamos el cliente y lo guardamos en una variable.
        Cliente cliente = cs.buscarPorId(id);
        // Buscamos el carrito viejo y lo guardamos en una variable.
        Carrito carritoViejo = cliente.getCarrito();

        // Creamos un nuevo carrito.
        Carrito carrito = new Carrito();
        // Seteamos el cliente del carrito.
        carrito.setCliente(cliente);
        // Guardamos el carrito nuevo.
        carS.guardar(carrito);

        // Movemos el carrito viejo a la lista de carritos viejos.
        List<Carrito> listaCarrito = cliente.getCarritosViejos();
        listaCarrito.add(carritoViejo);
        cliente.setCarritosViejos(listaCarrito);

        // Ahora pasamos el carrito nuevo como carrito actual del cliente.
        cliente.setCarrito(carrito);
        // Guardamos el cliente editado.
        cs.guardar(cliente);

        // Transformamos el objeto en una entidadModelo con assembler.
        EntityModel<Cliente> entityModel = assembler.toModel(cliente);
        // Retornamos la entidadModelo con el objeto y links.
        return ResponseEntity.ok(entityModel);
    }
}