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
@RequestMapping ("/api/v1/venta_web")
// @Tag es para la documentación.
@Tag(name = "VentaWeb", description = "Son las operaciones relacionadas con las ventasWeb.")
public class VentaWebController {

    // LLamamos el service de la clase.
    @Autowired
    private VentaWebService vws;
    @Autowired
    private CarritoService cs;
    @Autowired
    private ClienteService cls;

    // Métodos.
    // Abajo se usa un tipo de dato ResposeEntity, es el saludo 
    // que se da cuando se comunica con una página web.

    // Método que lista las ventasWeb.
    @GetMapping("/listar")
    @Operation(summary = "Obtener todas las VentasWeb.", description = "Obtiene una lista con todas las VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontraron VentasWeb."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<List<VentaWeb>> buscarTodos(){
        // Buscamos las ventasWeb y las guardamos en una variable.
        List<VentaWeb> ventasWeb = vws.buscarTodos();
        // Si no encontramos ventasWeb reornamos una respuesta vacía.
        if (ventasWeb.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Si no, retornamos las ventasWeb en una respuesta.
        return ResponseEntity.ok(ventasWeb);
    }

    // Método que busca por id.
    @GetMapping ("/{id}")
    @Operation(summary = "Obtener una VentasWeb por su id.", description = "Obtiene un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró la VentasWeb."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<VentaWeb> BuscaPorId(@Parameter(description = "ID de la VentaWeb a buscar", required = true)
        @PathVariable int id){
        // Encerramos la funcionalidad en un try/catch
        try {
            // Buscamos por id y guardamos la venta en una variable.
            VentaWeb vw = vws.buscarPorId(id);
            // Retornamos una respuesta que contiene la ventaWeb.
            return ResponseEntity.ok(vw);
        } catch (Exception e) {
            // Si algo falla retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para eliminar.
    @DeleteMapping ("/{id}")
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
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    }

    // Método para actualizar.
    // Los argumentos que recibe la funcion es un id para buscar la ventaWeb a editar y recibe una ventaWeb nueva, 
    // los atributos de esta ventaWeb nueva van a reemplazar los atributos de la ventaWeb encontrada.
    @PutMapping ("/{id}")
    @Operation(summary = "Actualiza una VentasWeb por su id.", description = "Actualiza un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Actualización ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró la VentasWeb."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<VentaWeb> actualizar (
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
            // Retornamos la respuesta con la ventaWeb actualizada.
            return ResponseEntity.ok(VW);
        } catch (Exception e) {
            // Si no guardamos retornamos una respuesta vacía.
            return ResponseEntity.noContent().build();
        }
    } 

    // Crea una venta
    @PostMapping("/agregar/{run}/{pago}")
    @Operation(summary = "Crea una VentasWeb.", description = "Crea un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Creación ejecutada exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<?> comprar2(
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

        // Retornamos una respuesta que contiene la ventaWeb.
        return ResponseEntity.ok(vw);
    }

    // Método para calcular el costo del carrito.
    @GetMapping("/carrito_costo/{run}")
    @Operation(summary = "Calcula el costo de un carrito por su id.", description = "Calcula el costo de un VentasWeb.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Cálculo ejecutado exitosamente.",
        content = @Content(mediaType = "application/json", 
        schema = @Schema (implementation = Cliente.class))),
    @ApiResponse(responseCode = "404", description = "no se encontró el carrito."),
    @ApiResponse(responseCode = "500", description = "Error interno del sistema.")
    })
    public ResponseEntity<String>valorCarrito(
        @Parameter(description = "ID del cliente de cuyo carrito queremos saber el costo.", required = true)
        @PathVariable long run){

        // Buscamos al cliente y lo guardamos en una variable.
        Cliente cliente = cls.buscarPorId(run);
        System.out.println("nombre cliente: " + cliente.getNombre_completo());
        // Buscamos el carrito:
        Carrito carrito = cliente.getCarrito();
        // Calculamos el valor del carrito:
        int costo = cs.valor(carrito.getId_carrito());
        String sentencia = ("El valor del carrito es: " + costo + "\n");
        // Buscamos los detalles del carrito.
        String nombre;
        int cantidad;
        List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
        for (DetalleCarrito d : detalles){
            nombre = d.getProducto().getNombre();
            cantidad = d.getCantidadSolicitada();
            sentencia += ("Producto: " + nombre + "       Cantidad: " + cantidad + "\n");
        }
        return ResponseEntity.ok(sentencia);
    }
}
