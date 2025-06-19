package com.ventasWeb.cl.ventasWeb.service;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.model.Cliente_ventaWeb;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.Factura;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.model.VentaWeb;
import com.ventasWeb.cl.ventasWeb.repository.Cliente_ventaWebRepository;
import com.ventasWeb.cl.ventasWeb.repository.FacturaRepository;
import com.ventasWeb.cl.ventasWeb.repository.VentaWebRepository;

// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
public class VentaWebServiceTest {

    // Creamos los varios mocks para simular el repositorio y los varios servicios que esta clase llama.
    @Mock
    private VentaWebRepository vwr;
    @Mock
    private CarritoService cs;
    @Mock
    private DetalleCarritoService dcs;
    @Mock
    private ProductoService ps;
    @Mock
    private ClienteService cls;
    @Mock
    private EnvioService es;
    @Mock
    private Cliente_ventaWebRepository cvr;
    @Mock
    private FacturaRepository fr;

    // Creamos una istancia de VentaWebService y le inyectamos los mocks que creamos.
    @InjectMocks
    private VentaWebService vws;

    // Preparamos las variables que vamos a usar en los tests.
    // Declaramos las variables.
    Producto producto1;
    Producto producto2;
    DetalleCarrito detalle1;
    DetalleCarrito detalle2;
    Carrito carrito1;
    Cliente cliente1;
    VentaWeb venta1;
    VentaWeb venta2;

    // Generamos una acción que se hace antes de cada test, esta acción inicializa las variables.
    @BeforeEach
    void setUp () {
        // Inicializamos producto1 y producto2.
        producto1 = new Producto();
        producto2 = new Producto();
        producto1.setId_producto(1L);
        producto2.setId_producto(2L);
        producto1.setNombre("galletas integrales");
        producto2.setNombre("frutos secos");
        producto1.setPrecio(1200);
        producto2.setPrecio(1100);
        producto1.setCantidad(50);
        producto2.setCantidad(60);
        // Inicializamos detalle1 y detalle2.
        detalle1 = new DetalleCarrito();
        detalle2 = new DetalleCarrito();
        detalle1.setId_detalleCarrito(1L);
        detalle2.setId_detalleCarrito(2L);
        // Inicializamos carrito1.
        carrito1 = new Carrito();
        carrito1.setId_carrito(1L);
        carrito1.setDetallesCarrito(new ArrayList<DetalleCarrito>());
        carrito1.setPagado(false);
        // Inicializamos cliente1.
        cliente1 = new Cliente();
        cliente1.setRun_cliente(1L);
        cliente1.setNombre_completo("nombre");
        cliente1.setDireccion("direccion 1234");
        // Inicializamos venta1.
        venta1 = new VentaWeb();
        venta1.setId_ventaWeb(1L);
        venta1.setDetallesCompras(new ArrayList<Cliente_ventaWeb>());
        venta1.setCliente(cliente1);
        venta1.setDireccion(cliente1.getDireccion());
        // Inicializamos venta2.
        venta2 = new VentaWeb();
        venta2.setId_ventaWeb(2L);
        venta2.setDetallesCompras(new ArrayList<Cliente_ventaWeb>());
    }


    // Métodos.
    //probando .buscarTodos() cuando hay ventas.
    @Test
    void probarBuscarTodosBien() {
        // Definimos comportamiento del mock.
        List<VentaWeb> ventas = Arrays.asList(venta1, venta2);
        when(vwr.findAll()).thenReturn(ventas);

        // LLamamos al método .buscarTodos()
        List<VentaWeb> resultado = vws.buscarTodos();

        // Verificaciones.
        // Verificamos que resultado sea de longitud 2.
        assertEquals(2, resultado.size());
        // Verificamos los ids de las ventas retornadas.
        assertEquals(1L, resultado.get(0).getId_ventaWeb());
        assertEquals(2L, resultado.get(1).getId_ventaWeb());
        // Verificamos que el mock fue llamado una vez.
        verify(vwr, times(1)).findAll();
    }

    // Probando .buscarTodos() cuando no hay ventas.
    @Test
    void probarBuscarTodosMal () {
        // Definimos el comportamiento del mock.
        when(vwr.findAll()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarTodos()
        List<VentaWeb> resultado = vws.buscarTodos();

        // Verificaciones.
        // Verificamos que la lista esté vacía.
        assertTrue(resultado.isEmpty());
        // Verificamos que el mock fue llamado una vez.
        verify(vwr, times(1)).findAll();
    }

    // Probando .buscarPorId() cuando encuenta una venta.
    @Test
    void probarBuscarPorIdBien() {
        // Definimos el comportamiento del mock.
        when(vwr.findById(1L)).thenReturn(Optional.of(venta1));

        // LLamamos al método buscarPorId()
        VentaWeb resultado = vws.buscarPorId(1L);

        // Verificaciones.
        // Verificamos el nombre y clave del cliente de la venta.
        assertEquals("nombre", resultado.getCliente().getNombre_completo());
        assertEquals("direccion 1234", resultado.getDireccion());
        // Verificamos que el mock fue llamado una vez.
        verify(vwr, times(1)).findById(1L);
    }

    // Probando .guardar()
    @Test
    void probarGuardar() {
        // Definimos el comportamiento del mock.
        when(vwr.save(any(VentaWeb.class))).thenReturn(venta1);

        // LLamamos al método .guardar()
        VentaWeb resultado = vws.guardar(venta1);

        // Verificaciones.
        // Verificamos que los datos de la venta sean.
        assertEquals("nombre", resultado.getCliente().getNombre_completo());
        assertEquals("direccion 1234", resultado.getDireccion());
        // Verificamos que el mock fue llamado una vez.
        verify(vwr, times(1)).save(any(VentaWeb.class));
    }

    // Probando .borrar()
    @Test
    void probarBorrar() {
        // Definimos el comportamiento del mock.
        doNothing().when(vwr).deleteById(1L);

        // LLamamos al método .borrar() de carritoService.
        vws.borrar(1L);

        // Verificaciones.
        // Verificamos que el método fue llamado una vez. 
        verify(vwr, times(1)).deleteById(1L);
    }

    // Probando .buscarProductos() cuando encuentra
    @Test
    void probarBuscarProductosBien() {
        // Definimos el comportamiento del mock.
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(ps.buscarTodos()).thenReturn(productos);

        // LLamamos al método buscarProductos().
        List<Producto> resultado = vws.buscarProductos();

        // Verificaciones.
        // Verificamos que los productos devueltos tengan datos.
        assertEquals("galletas integrales", resultado.get(0).getNombre());
        assertEquals("frutos secos", resultado.get(1).getNombre());
        // Verificamos que el mock fue llamado una vez.
        verify(ps, times(1)).buscarTodos();
    }

    // Probando .buscarProductos cuando no encuentra.
    @Test
    void probarBuscarProductosMal() {
        // Definimos el comportamiento del mock.
        when(ps.buscarTodos()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarProductos()
        List<Producto> resultado = vws.buscarProductos();

        // Verificaciones.
        // Verificamos que la lista esté vacía.
        assertTrue(resultado.isEmpty());
        // Verificamos que el mock fue llamado una vez.
        verify(ps, times(1)).buscarTodos();
    }

    // Probando .buscarProductoPorId() cuando encuentra.
    @Test
    void probarBuscarProductoPorIdBien() {
        // Definimos el comportamiento del mock.
        when(ps.buscarPorId(1L)).thenReturn(producto1);

        // LLamamos al método .buscarProductoPorId()
        Producto resultado = vws.buscarProductoPorId(1L);

        // Verificaciones.
        // Verificamos que el resultado contenga los datos que buscamos.
        assertEquals("galletas integrales", resultado.getNombre());
        // Verificamos que el mock fue llamado una vez.
        verify(ps, times(1)).buscarPorId(1L);
    }

    // Probando .buscarProductoPorNombre() cuando encuentra.
    @Test
    void probarProductoPorNombre() {
        // Definimos el comportamiento del mock.
        when(ps.buscarPorNombre("frutos secos")).thenReturn(producto2);

        // LLamamos al métodos .buscarProductoPorNombre()
        Producto resultado = vws.buscarProductoPorNombre("frutos secos");

        // Verificaciones.
        // Verificamos que el resultado contenga los datos buscados.
        assertEquals("frutos secos", resultado.getNombre());
        // Verificamos que el mock fue llamado una vez.
        verify(ps, times(1)).buscarPorNombre("frutos secos");
    }

    // Probando .eliminarProductoCarrito()
    @Test
    void probarEliminarProductoCarrito() {
        // Preparamos las variables.
        detalle1.setProducto(producto1);
        detalle1.setCantidadSolicitada(3);
        detalle1.setCarrito(carrito1);
        detalle2.setProducto(producto2);
        detalle2.setCantidadSolicitada(5);
        detalle2.setCarrito(carrito1);
        carrito1.getDetallesCarrito().add(detalle1);
        carrito1.getDetallesCarrito().add(detalle2);
        cliente1.setCarrito(carrito1);
        // Definimos el comportamiento de los mocks.
        when(cls.buscarPorId(1L)).thenReturn(cliente1);
        when(cs.eliminarDetalle(2L, 1L)).thenAnswer(invocation -> {
            // Creamos un carrito y fingimos que buscamos carrito1 y lo guardamos.
            Carrito c = carrito1;
            c.getDetallesCarrito().remove(1);
            return c;
        });

        // LLamamos al método .eliminarProductoCarrito()
        Carrito respuesta = vws.eliminarProductoCarrito(1L, 2L);

        // Verificaciones.
        // Verificamos que la longitud de detallesCarrito sea 1 (antes era 2).
        assertEquals(1, respuesta.getDetallesCarrito().size());
        // Verificamos que el detalle que queda es el 1 y no el 2.
        assertEquals(producto1, respuesta.getDetallesCarrito().get(0).getProducto());
        // Verificamos que los mocks fueron llamados cada uno una vez.
        verify(cls, times(1)).buscarPorId(1L);
        verify(cs, times(1)).eliminarDetalle(2L, 1L);
    }

    // Probar .valorCarrito (para operaciones matemáticas).
    @Test
    void probarValorCarrito() {
        // Preparamos las variables.
        detalle1.setProducto(producto1);
        detalle1.setCantidadSolicitada(3);
        detalle1.setCarrito(carrito1);
        detalle2.setProducto(producto2);
        detalle2.setCantidadSolicitada(5);
        detalle2.setCarrito(carrito1);
        carrito1.getDetallesCarrito().add(detalle1);
        carrito1.getDetallesCarrito().add(detalle2);
        cliente1.setCarrito(carrito1);
        // Definimos el comportamiento de los mocks.
        when(cls.buscarPorId(1L)).thenReturn(cliente1);
        when(cs.valor(1L)).thenAnswer(invocation -> {
            Carrito c = carrito1;
            List<DetalleCarrito> detalles = c.getDetallesCarrito();
            int total = 0;
            for (DetalleCarrito d : detalles){
                total += d.getCantidadSolicitada() * d.getProducto().getPrecio();
            }
            return total;
        });

        // LLamamos al método .valorCarrito()
        int resultado = vws.valorCarrito(1L);

        // Verificaciones.
        // Verificamos que el valor de resultado sea el que corresponde.
        int costo = detalle1.getCantidadSolicitada() * detalle1.getProducto().getPrecio() 
        + detalle2.getCantidadSolicitada() * detalle2.getProducto().getPrecio();
        assertEquals(costo, resultado);
        // Verificamos que los mocks fueron llamados cada uno una vez.
        verify(cls, times(1)).buscarPorId(1L);
        verify(cs, times(1)).valor(1L);
    }

    // Probar .valorCarrito2() (para devolver un string con el costo de la compra).
    @Test
    void probarValorCarrito2() {
        // Preparamos las variables.
        detalle1.setProducto(producto1);
        detalle1.setCantidadSolicitada(3);
        detalle1.setCarrito(carrito1);
        detalle2.setProducto(producto2);
        detalle2.setCantidadSolicitada(5);
        detalle2.setCarrito(carrito1);
        carrito1.getDetallesCarrito().add(detalle1);
        carrito1.getDetallesCarrito().add(detalle2);
        cliente1.setCarrito(carrito1);

        // Definimos el comportamiento de los mocks.

        // Este método llama al método .valorCarrito() del mismo service, por lo que tengo que mockear las dependencias externas que 
        // .valorCarrito() usa.
        // Definimos el comportamiento de los mocks.
        when(cls.buscarPorId(1L)).thenReturn(cliente1);
        when(cs.valor(1L)).thenAnswer(invocation -> {
            Carrito c = carrito1;
            List<DetalleCarrito> detalles = c.getDetallesCarrito();
            int total = 0;
            for (DetalleCarrito d : detalles){
                total += d.getCantidadSolicitada() * d.getProducto().getPrecio();
            }
            return total;
        });
        // Ahora mockeamos las dependencias que esta función llama directamente.
        // La dependencia se repite por lo que la traigo pero la dejo comentada solamente.
        // when(cls.buscarPorId(1L)).thenReturn(cliente1);

        // LLamamos al método .valorCarrito2()
        String respuesta = vws.valorCarrito2(1L);

        // Verificaciones.
        // System.out.println(respuesta);
        String texto = "El valor del carrito es: 9100\nProducto: galletas integrales       Cantidad: 3\nProducto: frutos secos       Cantidad: 5\n";
        // Verificamos que resultado y texto coincidan (texto fue copiado de un sout de una salida de terminal).
        assertEquals(texto, respuesta);
        // Verificamos que los mocks fueron llamados.
        verify(cls, times(2)).buscarPorId(1L);
        verify(cs, times(1)).valor(1L);
    }
}
