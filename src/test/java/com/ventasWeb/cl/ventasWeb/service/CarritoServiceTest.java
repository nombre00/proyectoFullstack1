package com.ventasWeb.cl.ventasWeb.service;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
// import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ventasWeb.cl.ventasWeb.model.Carrito;
import com.ventasWeb.cl.ventasWeb.model.Cliente;
import com.ventasWeb.cl.ventasWeb.model.DetalleCarrito;
import com.ventasWeb.cl.ventasWeb.model.Producto;
import com.ventasWeb.cl.ventasWeb.repository.CarritoRepository;

// Al hacer esto podemos hacer las pruebas sin tener que cargar todo el hambiente, hacer hibernate, etc...
@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {

    // Creamos el mock del repositorio para simular su comportamiento.
    @Mock
    private CarritoRepository cr;
    // Creamos el mock del ProductoServicio para simular su comportamiento.
    @Mock
    private ProductoService ps;
    // Creamos el mock del DetalleCarritoService para simular su comportamiento.
    @Mock
    private DetalleCarritoService dcs;
    // Creamos el mock de ClienteService para simular su comportamiento.
    @Mock
    private ClienteService cls;

    // Creamos una istancia de CarritoService y le inyectamos los mocks que creamos.
    @InjectMocks
    private CarritoService cs;


    // Preparamos las variables que vamos a usar en los tests.
    // Declaramos las variables.
    private Producto p1;
    private Producto p2;
    private DetalleCarrito dc1;
    private DetalleCarrito dc2;
    private Carrito c1;
    private Carrito c2;
    private Cliente cl1;

    // Generamos una acción que se hace antes de cada test, esta acción inicializa las variables.
    @BeforeEach
    void setUp(){
        // Inicializamos 2 productos.
        p1 = new Producto();
        p2 = new Producto();
        p1.setId_producto(Long.valueOf(1));
        p2.setId_producto(Long.valueOf(2));
        p1.setCantidad(50);
        p2.setCantidad(40);
        p1.setNombre("frutos secos");
        p2.setNombre("bolsas papel");
        p1.setPrecio(1300);
        p2.setPrecio(900);
        // Inicializamos 2 detalleCarritos.
        dc1 = new DetalleCarrito();
        dc2 = new DetalleCarrito();
        dc1.setId_detalleCarrito(Long.valueOf(1));
        dc2.setId_detalleCarrito(Long.valueOf(2));
        // Inicializamos 2 carritos.
        c1 = new Carrito();
        c2 = new Carrito();
        c1.setId_carrito(Long.valueOf(1));
        c2.setId_carrito(Long.valueOf(2));
        c1.setDetallesCarrito(new ArrayList<DetalleCarrito>());
        c2.setDetallesCarrito(new ArrayList<DetalleCarrito>());
        c1.setPagado(false);
        c2.setPagado(false);
        // Inicializamos 1 cliente.
        cl1 = new Cliente();
        cl1.setRun_cliente(1L);
        cl1.setNombre_completo("nombre");
    }

    // Métodos.
    // Probando .buscarTodos() cuando hay carritos.
    @Test
    void probarBuscarTodosBien(){
        // Definimos el comportamiento del Mock repositorio.
        // Hacemos una lista y le agregamos los carritos.
        List<Carrito> carritos = Arrays.asList(c1,c2);
        when(cr.findAll()).thenReturn(carritos);

        // LLamamos el método .buscarTodos()
        List<Carrito> carritos2 = cs.buscarTodos();

        // Verificaciones.
        // Verificamos la longitud de la lista.
        assertEquals(2, carritos2.size());
        // Verificamos datos de la lista.
        assertEquals(1, carritos2.get(0).getId_carrito());
        assertEquals(2, carritos2.get(1).getId_carrito());
        assertFalse(carritos2.get(0).isPagado());
        assertFalse(carritos2.get(1).isPagado());
        // Verificamos que el método haya sido llamado una vez.
        verify(cr, times(1)).findAll();
    }

    // Probando .buscarTodos() cuando no hay productos.
    @Test
    void probarBuscarTodosMal() {
        // Definimos el comportamiento del mock repositorio.
        when(cr.findAll()).thenReturn(Collections.emptyList());

        // LLamamos al método .buscarTodos() del carritoService.
        List<Carrito> carritos2 = cs.buscarTodos();

        // Verificaciones.
        // Verificamos que la lista esté vacía.
        assertTrue(carritos2.isEmpty());
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).findAll();
    }

    // Probando .buscarPorId() cuando encuentra un carrito.
    @Test
    void probarBuscarPorIdBien() {
        // Definimos el comportamiento del mock repositorio.
        when(cr.findById(Long.valueOf(1))).thenReturn(Optional.of(c1));

        // LLamamos al método .buscarPorId() del carritoService.
        Carrito c3 = cs.buscarPorId(c1.getId_carrito());

        // Verificaciones.
        // Verificamos que c3 no sea nulo.
        assertNotNull(c3);
        // Verificamos que el id de c3 sea igual al de c1.
        assertEquals(c1.getId_carrito(), c3.getId_carrito());
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).findById(Long.valueOf(1));
    }

    // Probamos .guardar.
    @Test
    void probarGuardar() {
        // Definimos el comportamiento del mock repositorio.
        when(cr.save(any(Carrito.class))).thenReturn(c1);

        // LLamamos al método .guardar() de carritoService.
        Carrito c3 = cs.guardar(c1);

        // Verificaciones.
        // Verificamos que c3 no sea nulo.
        assertNotNull(c3);
        // Verificamos que el id de c3 sea igual al de c1.
        assertEquals(c1.getId_carrito(), c3.getId_carrito());
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).save(c1);
    }

    // Probamos .borrar().
    @Test
    void probarBorrar() {
        // Definimos el comportamiento del mock repositorio.
        doNothing().when(cr).deleteById(c1.getId_carrito());

        // LLamamos al método .borrar() de carritoService.
        cs.borrar(c1.getId_carrito());

        // Verificaciones.
        // Verificamos que el método fue llamado una vez. 
        verify(cr, times(1)).deleteById(c1.getId_carrito());
    }

    // Probamos calcularValorCarrito().
    @Test
    void probarValor() {
        // Preparamos las variables, vamos a agregar 2 detalleCarrito al carrito, cada una con un producto.
        dc1.setProducto(p1);
        dc1.setCantidadSolicitada(3);
        dc2.setProducto(p2);
        dc2.setCantidadSolicitada(4);
        c1.getDetallesCarrito().add(dc1);
        c1.getDetallesCarrito().add(dc2);

        // Definimos el comportamiento los mocks.
        when(cr.findById(1L)).thenReturn(Optional.of(c1));
        // Recibimos cualquier long de argumento y retornamos un valor dinámico relativo al input.
        when(dcs.valor(any(Long.class))).thenAnswer(invocation -> {
            // Inicializamos una variable de tipo detalleCarrito.
            DetalleCarrito dc = new DetalleCarrito();
            // Tomamos el argumento dado y lo guardamos en una variable.
            Long id = invocation.getArgument(0);
            // Dependiendo del valor de la variable guardamos en dc el detalleCarrito correspondiente.
            if (id == dc1.getId_detalleCarrito()){
                dc = dc1;
            }
            if (id == dc2.getId_detalleCarrito()){
                dc = dc2;
            }
            // Retornamos el valor del detalleCarrito.
            return dc.getCantidadSolicitada() * dc.getProducto().getPrecio();
        });

        // LLamamos al método .valor() de carritoService.
        int precio = cs.valor(1);

        // Verificiones.
        // Verificamos que el precio sea el que corresponde.
        int total = dc1.getCantidadSolicitada() * dc1.getProducto().getPrecio() + dc2.getCantidadSolicitada() * dc2.getProducto().getPrecio();
        assertEquals(total, precio);
        // Verificamos que el método fue llamado una vez.
        verify(cr, times(1)).findById(1L);
    }

    // Probamos .agregarDetalleCarrito()
    @Test
    void probarAgregarDetalleCarrito() {
        // Definimos el comportamiento de los mocks.
        when(cr.findById(1L)).thenReturn(Optional.of(c1));
        when(dcs.buscarPorId(1L)).thenReturn(dc1);
        when(cr.save(c1)).thenReturn(c1);

        // LLamamos al método .agregarDetalleCarrito()
        Carrito c3 = cs.agregarDetalleCarrito(1L, 1L);

        // Verificiones.
        // Verificamos que c3  contenga un carrito con id 1L.
        assertEquals(1L, c3.getDetallesCarrito().get(0).getId_detalleCarrito());
        // Verificamos que cada método fue llamado una vez.
        verify(cr, times(1)).findById(1L);
        verify(dcs, times(1)).buscarPorId(1L);
        verify(cr, times(1)).save(c1);
    }

    //probar .agregarProductoCarrito()
    @Test
    void probarAgregarProductoCarrito() {
        // Preparamos las variables a usar.
        cl1.setCarrito(c1);
        // Definimos el comportamiento de los mocks.
        when(cls.buscarPorId(1L)).thenReturn(cl1);
        when(ps.buscarPorId(1L)).thenReturn(p1);
        // Retornamos el detalleCarrito pasado de argumento.
        when(dcs.guardar(any(DetalleCarrito.class))).thenAnswer(invocation -> {
            DetalleCarrito dc = invocation.getArgument(0);
            return dc;
        });
        when(cs.guardar(any(Carrito.class))).thenReturn(c1);
        when(cls.guardar(cl1)).thenReturn(cl1);

        // LLamamos al método.
        Carrito c3 = cs.agregarProductoCarrito(1L, 1L, 11);

        // Verificaciones.
        // Verificamos que el carrito contenga el producto agregado.
        assertEquals(p1.getNombre(), c3.getDetallesCarrito().get(0).getProducto().getNombre());
        verify(cls, times(2)).buscarPorId(1L);
        verify(ps, times(1)).buscarPorId(1L);
        verify(dcs, times(1)).guardar(any(DetalleCarrito.class));
        verify(cls, times(1)).guardar(cl1);
    }

    // Probando .eliminarDetalle()
    @Test
    void probarEliminarDetalle() {
        // Preparamos las variables.
        c1.getDetallesCarrito().add(dc1);
        c1.getDetallesCarrito().add(dc2);
        // Definimos el comportamiento de los mocks.
        when(cr.findById(1L)).thenReturn(Optional.of(c1));
        doNothing().when(dcs).borrar(1L);
        // Retornamos el carrito pasado de argumento.
        when(cr.save(any(Carrito.class))).thenAnswer(invocation -> {
            Carrito c = invocation.getArgument(0);
            return c;
        });

        // LLamámos al método .eliminarDetalle()
        Carrito c3 = cs.eliminarDetalle(1, 1);

        // Verificaciones.
        // Verificamos que la longitud de la lista detalles sea 1.
        assertEquals(1, c3.getDetallesCarrito().size());
        // Verificamos que los métodos fueron llamados cada uno una vez.
        verify(cr, times(1)).findById(1L);
        verify(dcs, times(1)).borrar(1L);
        verify(cr, times(1)).save(any(Carrito.class));
    }

    // Probar buscarIdsProductos()
    @Test
    void probarBuscarIDsProductos() {
        // Preparamos las variables.
        dc1.setProducto(p1);
        dc1.setCantidadSolicitada(4);
        dc2.setProducto(p2);
        dc2.setCantidadSolicitada(5);
        c1.getDetallesCarrito().add(dc1);
        c1.getDetallesCarrito().add(dc2);
        // Definimos el comportamiento de los mocks.
        when(cr.findById(any(Long.class))).thenReturn(Optional.of(c1));

        // LLamamos al método .buscarIDsProductos.
        List<Long> ids = cs.buscarIDsProductos(1L);

        // Verificaciones.
        // Verificamos que ids tenga la longitud que corresponda y guarde 1 y 2.
        assertEquals(2, ids.size());
        assertEquals(Long.valueOf(1), ids.get(0));
        assertEquals(Long.valueOf(2), ids.get(1));
    }

    // Probamos .buscarDetalles()
    @Test
    void probarBuscarDetalles() {
        // Preparamos las variables.
        dc1.setProducto(p1);
        dc1.setCantidadSolicitada(4);
        dc2.setProducto(p2);
        dc2.setCantidadSolicitada(5);
        c1.getDetallesCarrito().add(dc1);
        c1.getDetallesCarrito().add(dc2);
        // Definimos el comportamiento de los mocks.
        when(cr.findById(1L)).thenReturn(Optional.of(c1));

        // LLamamos al método .buscarDetalles()
        List<DetalleCarrito> detalles = cs.buscarDetalles(1L);

        // Verificaciones.
        // Verificamos que la longitud de la lista sea 2.
        assertEquals(2, detalles.size());
        // Verificamos que la lista contenga el producto de cada detalle.
        assertEquals(p1, detalles.get(0).getProducto());
        assertEquals(p2, detalles.get(1).getProducto());
    }

    // Probamos .revisarStock()
    @Test
    void probarRevisarStock() {
        // Preparamos las variables.
        dc1.setProducto(p1);
        dc1.setCantidadSolicitada(4);
        dc2.setProducto(p2);
        dc2.setCantidadSolicitada(5);
        c1.getDetallesCarrito().add(dc1);
        c1.getDetallesCarrito().add(dc2);
        // Lista de ids de productos de la base de datos.
        List<Long> ids = new ArrayList<>();
        ids.add(Long.valueOf(1));
        ids.add(Long.valueOf(2));
        // Definimos el comportamiento de los mocks.
        when(cr.findById(1L)).thenReturn(Optional.of(c1));
        when(ps.buscarTodosIds()).thenReturn(ids);

        // LLamamos al método .revisarStock()
        Boolean existen = cs.revisarStock(1L);

        // Verificaciones.
        // Verificamos que existen sea verdadero.
        assertTrue(existen);
        // Verificamos que cada método fue llamado una vez.
        verify(cr, times(1)).findById(1L);
        verify(ps, times(1)).buscarTodosIds();
    }

    // Probamos .revisarPago()
    @Test
    void probarRevisarPago() {
        // Preparamos la variable.
        c1.getDetallesCarrito().add(dc1);
        c1.setPagado(true);
        // Definimos el comportamiento de los mocks.
        when(cr.findById(1L)).thenReturn(Optional.of(c1));
        // Retornamos el carrito que pasamos de argumento.
        when(cr.save(any(Carrito.class))).thenAnswer(invocation -> {
            Carrito c = invocation.getArgument(0);
            return c;
        });

        // LLamamos al método revisarPago()
        Carrito c3 = cs.revisarPago(c1);

        // Verificaciones.
        // Verificamos que el carro esté pagado.
        assertEquals(true, c3.isPagado());
        // Verificamos que la lista de detalles del carrito esté vacía.
        assertEquals(null, c3.getDetallesCarrito());
        // Verificamos que cada método fue llamado una vez.
        verify(cr, times(1)).findById(1L);
        verify(cr, times(1)).save(any(Carrito.class));
    }
}
