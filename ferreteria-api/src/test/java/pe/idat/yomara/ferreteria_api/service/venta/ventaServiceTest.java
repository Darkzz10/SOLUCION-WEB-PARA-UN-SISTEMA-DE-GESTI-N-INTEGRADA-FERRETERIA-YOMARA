package pe.idat.yomara.ferreteria_api.service.venta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.idat.yomara.ferreteria_api.model.dto.request.VentaRequest;
import pe.idat.yomara.ferreteria_api.model.dto.request.VentaDetalleRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.VentaResponse;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.*;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.*;
import pe.idat.yomara.ferreteria_api.model.entity.venta.*;
import pe.idat.yomara.ferreteria_api.repository.inventario.*;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;
import pe.idat.yomara.ferreteria_api.repository.venta.*;
import pe.idat.yomara.ferreteria_api.service.venta.impl.VentaServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock private VentaRepository ventaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ProductoRepository productoRepository;
    @Mock private StockSedeRepository stockSedeRepository;
    @Mock private MovimientoStockRepository movimientoStockRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void registrarVenta_CalculoMatematico_EsCorrecto() {
        String username = "vendedor_test";

        Sede sede = Sede.builder().id(1L).nombre("Sede Central").build();
        Usuario usuario = Usuario.builder().username(username).sede(sede).build();
        Cliente cliente = Cliente.builder().id(1L).nombreCompleto("Juan Perez").documentoNumero("77777777").build();

        Producto producto = Producto.builder()
                .id(10L).nombre("Taladro").precioBase(new BigDecimal("50.00")).sku("TAL-01").build();

        StockSede stockSede = StockSede.builder().cantidad(10).build();

        VentaDetalleRequest item = new VentaDetalleRequest();
        item.setProductoId(10L);
        item.setCantidad(2);
        item.setDescuento(BigDecimal.ZERO);

        VentaRequest request = new VentaRequest();
        request.setClienteId(1L);
        request.setTipoComprobante("BOLETA");
        request.setTipoEntrega(Venta.TipoEntrega.TIENDA);
        request.setMedioPago(Venta.MedioPago.EFECTIVO);
        request.setDetalles(List.of(item));

        // Definir comportamiento de Mocks (Stubbing)
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(stockSedeRepository.findByProductoIdAndSedeId(10L, 1L)).thenReturn(Optional.of(stockSede));

        // Al guardar la venta, devolvemos la misma entidad (simulado)
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArguments()[0]);

        // (Ejecución)
        VentaResponse response = ventaService.registrarVenta(request, username);

        // ASSERT (Validaciones)
        assertNotNull(response);

        BigDecimal totalEsperado = new BigDecimal("118.00");

        assertEquals(0, totalEsperado.compareTo(response.getTotal()),
                "El total debería ser 118.00 (100 de productos + 18 de IGV)");

        assertEquals("TIENDA", response.getTipoEntrega());
        assertEquals("EFECTIVO", response.getMedioPago());

        verify(stockSedeRepository).save(any(StockSede.class));
        verify(movimientoStockRepository).save(any(MovimientoStock.class));
    }
}
