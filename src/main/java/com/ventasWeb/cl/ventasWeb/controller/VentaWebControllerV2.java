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

import com.ventasWeb.cl.ventasWeb.assemblers.VentaWebRepresentationModelAssembler;
import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.VentaWeb;
import com.ventasWeb.cl.ventasWeb.service.CarritoService;
import com.ventasWeb.cl.ventasWeb.service.ClienteService;
import com.ventasWeb.cl.ventasWeb.service.VentaWebService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping ("/api/v2/venta_web")
// @Tag es para la documentación.
@Tag(name = "VentaWeb", description = "Son las operaciones relacionadas con las ventasWeb.")
public class VentaWebControllerV2 {

    // LLamamos el service de la clase.
    @Autowired
    private VentaWebService vws;
    @Autowired
    private CarritoService cs;
    @Autowired
    private ClienteService cls;
    // Inicializamos un assembler para combertir los objetos de clase a entidadesModelo que contienen hiperLinks.
    private final VentaWebRepresentationModelAssembler assembler = new VentaWebRepresentationModelAssembler();

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo que se da cuando se comunica con una página web.

    // Método que lista las ventasWeb.
    @GetMapping(value = "/listar", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener todas las VentasWeb.", description = "Obtiene una lista con todas las VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron VentasWeb."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public CollectionModel<EntityModel<VentaWeb>> buscarTodos(){
        // Buscamos los ventasWeb y los transformamos con el assembler
        List<EntityModel<VentaWeb>> ventas = vws.buscarTodos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        // Si no encontramos ventasWeb retornamos una respuesta vacía.
        if (ventas.isEmpty()){
            return CollectionModel.empty();
        }

        // Si tenemos ventasWeb, las retornamos dentro de una respuesta con enlace self.
        return CollectionModel.of(ventas,WebMvcLinkBuilder.
        linkTo(WebMvcLinkBuilder.methodOn(VentaWebControllerV2.class).buscarTodos()).withSelfRel());
    }

    // Método que busca por id.
    @GetMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Obtener una VentasWeb por su id.", description = "Obtiene un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró la VentasWeb."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<VentaWeb>> BuscaPorId(@Parameter(description = "ID de la VentaWeb a buscar", required = true)
        @PathVariable long id){
        // Encerramos la funcionalidad en un try/catch
        try {
            // Buscamos por ID y la guardamos en una variable.
            VentaWeb venta = vws.buscarPorId(id);
            // Si el objeto está vacío retornamos una respuesta not found.
            if (venta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<VentaWeb> entityModel = assembler.toModel(venta);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si algo falla retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para eliminar.
    @DeleteMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Elimina una VentasWeb por su id.", description = "Elimina un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Eliminación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró la VentasWeb."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<?> borrar(
        @Parameter(description = "ID de la VentaWeb a borrar", required = true)@PathVariable Long id){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Usamos el service directamente.
            vws.borrar(id);
            // Retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            // Retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para actualizar.
    // Los argumentos que recibe la funcion es un id para buscar la ventaWeb a editar y recibe una ventaWeb nueva, 
    // los atributos de esta ventaWeb nueva van a reemplazar los atributos de la ventaWeb encontrada.
    @PutMapping (value = "/{id}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Actualiza una VentasWeb por su id.", description = "Actualiza un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró la VentasWeb."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<VentaWeb>> actualizar (
        @Parameter(description = "ID de la VentaWeb a actualizar", required = true)@PathVariable Integer id, 
        // Lo de abajo es para el swagger, da una descripción, avisa que cuerpo requerido es obligatorio,
        // y content = @Content() es para que swagger genere una interfaz para ingresar en input y hacer la operación desde la documentacion.
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevos datos de la VentaWeb.",
            required = true,
            content = @Content(schema = @Schema(implementation = Cliente.class))
        )
        @RequestBody VentaWeb vw){
        // Encerramos la funcionalidad dentro de un try/catch.
        try {
            // Buscamos la ventaWeb a editar y la guardamos en una variable.
            VentaWeb VW = vws.buscarPorId(id);
            // Reemplazamos los atributos.
            VW.setDireccion(vw.getDireccion());
            VW.setFecha(vw.getFecha());
            VW.setEnvio(vw.getEnvio());
            VW.setCliente(vw.getCliente());
            VW.setTotalPagado(vw.getTotalPagado());
            // Guardamos los cambios.
            vws.guardar(VW);

            // Transformamos el objeto en una entidadModelo con assembler.
            EntityModel<VentaWeb> entityModel = assembler.toModel(VW);
            // Retornamos la entidadModelo con el objeto y links.
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            // Si algo falla retornamos una respuesta vacía.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    } 

    // Crea una venta
    @PostMapping(value = "/agregar/{run}/{pago}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Crea una VentasWeb.", description = "Crea un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Creación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<EntityModel<VentaWeb>> comprar(
        @Parameter(description = "ID del cliente", required = true)@PathVariable long run, 
        @Parameter(description = "Monto a pagar por la compra", required = true)@PathVariable int pago) {

        Cliente cliente = cls.buscarPorId(run);
        // Creamos una ventaWeb nueva y la guardamos en una variable.
        VentaWeb vw = vws.comprarCarrito(cliente.getRun_cliente(), pago);
        System.out.println("Revisando salida");
        System.out.println(vw.getCliente().getRun_cliente());
        // La guardamos 
        vws.guardar(vw);

        // Escribimos una salida que señala los productos comprados:
        int total = vws.valorCarrito(run);
        System.out.println("Costo de la compra: " + total);
        Carrito carrito = cliente.getCarrito();
        List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
        for (DetalleCarrito d : detalles){
            System.out.println("Producto: " + d.getProducto().getNombre() + ",  Cantidad: " + d.getCantidadSolicitada());
        }
        System.out.println("\n");

        // Transformamos el objeto en una entidadModelo con assembler.
        EntityModel<VentaWeb> entityModel = assembler.toModel(vw);
        // Retornamos la entidadModelo con el objeto y links.
        return ResponseEntity.ok(entityModel);
    }

    // Método para calcular el costo del carrito.
    @GetMapping(value = "/carrito_costo/{run}", produces = {
        MediaTypes.HAL_JSON_VALUE,
        MediaType.APPLICATION_JSON_VALUE
    })
    @Operation(summary = "Calcula el costo de un carrito por su id.", description = "Calcula el costo de un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Cálculo ejecutado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public EntityModel<Map<String, String>>valorCarrito(
        @Parameter(description = "ID del cliente de cuyo carrito queremos saber el costo.", required = true)
        @PathVariable long run){

        // Buscamos al cliente y lo guardamos en una variable.
        Cliente cliente = cls.buscarPorId(run);
        System.out.println("nombre cliente: " + cliente.getNombre_completo());
        // Buscamos el carrito:
        Carrito carrito = cliente.getCarrito();

        // Calculamos el valor del carrito:
        int costo = cs.valor(carrito.getId_carrito());
        String sentencia1 = ("El valor del carrito es: " + costo);
        
        // Como hateoas tiene problemas para resolver un string con saltos de linea guardamos el string dentro de un 
        // hashmap para tener pares key-value que resolver.
        Map<String, String> respuesta = new HashMap<>();
        // Agregamos el valor total a la respuesta.
        respuesta.put("linea0", sentencia1);
        // Creamos un string para nombrar la llave de cada valor del dicionario y le incorporamos un contador.
        String linea = "linea";
        int contador = 1;

        // Buscamos los detalles del carrito.
        String nombre;
        int cantidad;
        List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
        for (DetalleCarrito d : detalles){
            // agregamos el contador a linea.
            String llave = linea + contador;
            // Creamos un string que va a recibir los valores.
            String sentencia = "";
            nombre = d.getProducto().getNombre();
            cantidad = d.getCantidadSolicitada();
            sentencia += ("Producto: " + nombre + "       Cantidad: " + cantidad);
            // Agregamos la "linea de texto" al dicionario respuesta.
            respuesta.put(llave, sentencia);
            contador ++;
        }
        // Retornamos el entityModel con el Map y links para si mismo, el cliente y ver todos los productos.
        return EntityModel.of(respuesta,
        linkTo(methodOn(VentaWebControllerV2.class).valorCarrito(run)).withSelfRel(),
        linkTo(methodOn(ClienteControllerV2.class).buscarPorRun(run)).withRel("cliente."),
        linkTo(methodOn(VentaWebControllerV2.class).comprar(run, costo)).withRel("Comprar."),
        linkTo(methodOn(ProductoControllerV2.class).buscarTodos()).withRel("Productos disponibles.")
        );
    }
}