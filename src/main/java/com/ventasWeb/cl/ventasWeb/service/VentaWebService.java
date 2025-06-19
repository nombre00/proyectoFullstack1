package com.ventasWeb.cl.ventasWeb.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.model.Cliente_ventaWeb;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.Envio;
import com.ventasWeb.cl.ventasWeb.model.Factura;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.model.VentaWeb;
import com.ventasWeb.cl.ventasWeb.repository.Cliente_ventaWebRepository;
import com.ventasWeb.cl.ventasWeb.repository.FacturaRepository;
import com.ventasWeb.cl.ventasWeb.repository.VentaWebRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class VentaWebService {

    // LLamamos al repositorio de la clase.
    @Autowired
    private VentaWebRepository vwr;
    // LLamamos al repositorio de cliente, envio, producto, detalleproducto, 
    // cliente, factura, ventaWeb y carrito ya que hacemos colaboración de clases.
    @Autowired
    private CarritoService cs;
    @Autowired
    private DetalleCarritoService dcs;
    @Autowired
    private ProductoService ps;
    @Autowired
    private ClienteService cls;
    @Autowired
    private EnvioService es;
    @Autowired
    private Cliente_ventaWebRepository cvr;
    @Autowired
    private FacturaRepository fr;

    // Métodos genéricos. 

    // Buscar todos.
    public List<VentaWeb> buscarTodos(){
        return vwr.findAll();
    }
    // Buscar por id.
    public VentaWeb buscarPorId(long id){
        return vwr.findById(id).get();
    }
    // Guardar.
    public VentaWeb guardar(VentaWeb v){
        return vwr.save(v);
    }
    // Borrar.
    public void borrar(long id){
        vwr.deleteById(id);
    }

    // Gestión de productos.

    // Buscar todos los productos.
    public List<Producto> buscarProductos(){
        return ps.buscarTodos();
    }
    // Buscar producto por id.
    public Producto buscarProductoPorId(long id){
        return ps.buscarPorId(id);
    }
    // Buscar producto por nombre.
    public Producto buscarProductoPorNombre(String nombre){
        return ps.buscarPorNombre(nombre);
    }
    
    
    // Gestion carrito de compra:

    // Eliminar producto del carrito.
    public Carrito eliminarProductoCarrito(long run, long id_detalle){
        // eliminamos el detalle que contiene el producto que no queremos en el carrito.
        // Retornamos el carrito actualizado. 
        return cs.eliminarDetalle(id_detalle, cls.buscarPorId(run).getCarrito().getId_carrito());
    }

    // Calcular valor carrito (devuelve un int para calculos).
    public int valorCarrito(long run){
        // Buscamos el cliente por run.
        Cliente c = cls.buscarPorId(run);
        // Revisamos que el cliente y el carrito existan.
        if (c == null || c.getCarrito() == null) {
            return 0;
        }
        // Retornamos el valor del carrito.
        return cs.valor(c.getCarrito().getId_carrito());
    }
    
    // Calcular valor carrito y devolver un String para imprimirlo en pantalla.
    public String valorCarrito2(long id){
        // Calculamos el valor del carrito:
        int costo = valorCarrito(id);
        String sentencia = ("El valor del carrito es: " + costo + "\n");
        // Buscamos el carrito:
        Carrito carrito = cls.buscarPorId(id).getCarrito();
        // Buscamos los detalles del carrito.
        String nombre;
        int cantidad;
        List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
        for (DetalleCarrito d : detalles){
            nombre = d.getProducto().getNombre();
            cantidad = d.getCantidadSolicitada();
            sentencia += ("Producto: " + nombre + "       Cantidad: " + cantidad + "\n");
        }
        return sentencia;
    }

    // agregar producto a carrito.
    public Carrito ingresarProductoCarrito(int run, Integer id_p, Integer cantidad){
        // Buscamos el cliente y lo guardamos en una variable.
        Cliente c = cls.buscarPorId(run);
        // Tomamos el carrito del usuario y lo guardamos en una variable.
        Carrito carrito = c.getCarrito();
        // Revisamos que el cliente tenga un carrito, sino, lo creamos.
        if (carrito == null) {
            Carrito carritoNuevo = new Carrito();
            carrito = carritoNuevo;
        }
        // Creamos un nuevo detalle con los datos de la función.
        DetalleCarrito detalleNuevo = new DetalleCarrito();
        detalleNuevo.setProducto(ps.buscarPorId(id_p));
        detalleNuevo.setCantidadSolicitada(cantidad);
        // Guardamos el detalle en la tabla de detalles.
        dcs.guardar(detalleNuevo);
        // Sumamos el detalleCarrito al carrito.
        cs.agregarDetalleCarrito(carrito.getId_carrito(), detalleNuevo.getId_detalleCarrito());
        // Actualizamos el carrito con la nueva lista detalleProducto y lo guardamos.
        cs.guardar(carrito);
        // Actualizamos el cliente con el nuevo carrito actualizado.
        cls.guardar(c);
        // Retornamos el carrito con el producto ingresado.
        return carrito;
    }

    
    // Gestión venta.
    // Generar venta (crea la ventaWeb, crea la factura, cambia el estado del carrito, cambia el stock de productos y genera el envio).
    public VentaWeb comprarCarrito(long run, int pago){
        // Primero creamos una venta.
        VentaWeb ventaWeb =  new VentaWeb(); 
        // Luego buscamos el cliente y lo guardamos en una variable.
        Cliente cliente = cls.buscarPorId(run);
        // Luego buscamos el carrito del cliente y lo guardamos en una variable.
        Carrito carrito = cliente.getCarrito();

        // Ahora tenemos que revisar que los de productos existan.
        if (cs.revisarStock(carrito.getId_carrito())){

            // Luego revisamos que el pago sea igual al costo del carrito.
            if (cs.valor(cliente.getCarrito().getId_carrito()) == pago){

                // Luego revisamos que la cantidad de productos existan:
                // Buscamos los detallesCarrito y los guardamos en una variable.
                List<DetalleCarrito> detalles = carrito.getDetallesCarrito();
                // Buscamos los productos y los guardamos en una variable.
                List<Producto> productos = ps.buscarTodos();
                // Iteramos los detalles.
                for (DetalleCarrito d : detalles){
                    // Iteramos los productos.
                    for (Producto p : productos){
                        // Comparamos los Ids
                        if (d.getProducto().getId_producto() == p.getId_producto()){
                            // Si el id coincide comparamos las cantidades.
                            if (d.getCantidadSolicitada() > p.getCantidad()){
                                // Si la cantidad solicitada es mayor que el stock del producto salimos.
                                return ventaWeb;
                            }
                        }
                    }
                }
                // Si el código continúa hasta acá es que hay stock de los productos.
                // Ahora volvemos a iterar los productos y detalles para cambiar el stock.
                for (DetalleCarrito d : detalles){
                    for (Producto p : productos){
                        // Comparamos los Ids
                        if (d.getProducto().getId_producto() == p.getId_producto()){
                            // Si el id coincide cambiamos las cantidades.
                            p.setCantidad(p.getCantidad() - d.getCantidadSolicitada());
                            // Guardamos el cambio.
                            ps.guardar(p);
                        }
                    }
                }

                // Cambiamos el valor de pago a true.
                carrito.setPagado(true);
                // Guardamos carrito.
                cs.guardar(carrito);
                // Luego guardamos el cliente.
                cls.guardar(cliente);

                // Creamos un envio nuevo.
                Envio envio = new Envio();
                // LLenamos los atributos con la info correspondiente.
                envio.setCliente(cliente);
                envio.setDireccion(cliente.getDireccion());
                envio.setEstado("preparando envio.");
                envio.setFecha("hoy");
                envio.setCarrito(carrito);
                // Guardamos el envio.
                es.guardar(envio);

                // Creamos una nueva factura.
                Factura factura = new Factura();
                // Luego llenamos los datos de la factura.
                factura.setClienteEmail(cliente.getMail());
                factura.setFechaEmision("hoy");
                factura.setContenidoFactura(valorCarrito2(run));
                // Guardamos la factura.
                fr.save(factura);

                // Luego llenamos los datos de la ventaWeb.
                ventaWeb.setCliente(cliente);
                ventaWeb.setDireccion(cliente.getDireccion());
                ventaWeb.setFecha(envio.getFecha());
                ventaWeb.setTotalPagado(pago);
                ventaWeb.setEnvio(envio);
                ventaWeb.setFactura(factura);
                // Guardamos la ventaWeb.
                vwr.save(ventaWeb);

                // Creamos una entrada en la tabla de intersección cliente_ventaweb.
                Cliente_ventaWeb c_v = new Cliente_ventaWeb();
                // La llenamos con los datos de la venta y el cliente.
                c_v.setCliente(cliente);
                c_v.setVentaWeb(ventaWeb);
                // Guardamos la entrada.
                cvr.save(c_v);

                // Retornamos la venta.
                return ventaWeb;
            }
        }
        return ventaWeb;
    }
}
