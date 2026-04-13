package pe.idat.yomara.ferreteria_api.service.inventario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.idat.yomara.ferreteria_api.model.dto.request.MovimientoStockRequest;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.*;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.*;
import pe.idat.yomara.ferreteria_api.repository.inventario.*;
import pe.idat.yomara.ferreteria_api.repository.usuario.SedeRepository;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;
import pe.idat.yomara.ferreteria_api.service.inventario.impl.InventarioServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private StockSedeRepository stockSedeRepository;
    @Mock
    private MovimientoStockRepository movimientoStockRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SedeRepository sedeRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Test
    void registrarMovimiento_EntradaProveedor_IncrementaStockCorrectamente() {

        String username = "admin_yomara";
        Sede sede = Sede.builder().id(1L).nombre("Sede Central").build();
        Usuario usuario = Usuario.builder().username(username).rol(Rol.ADMIN).sede(sede).build();
        Producto producto = Producto.builder().id(10L).nombre("Cemento Sol").build();


        StockSede stockExistente = StockSede.builder().id(1L).producto(producto).sede(sede).cantidad(5).build();

        MovimientoStockRequest request = new MovimientoStockRequest();
        request.setProductoId(10L);
        request.setSedeId(1L);
        request.setCantidad(10);
        request.setTipoMovimiento(TipoMovimiento.ENTRADA_PROVEEDOR);
        request.setMotivo("Compra lote Marzo");

        // Stubbing: Definimos qué devuelven los mocks al ser llamados
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));
        when(sedeRepository.findById(1L)).thenReturn(Optional.of(sede));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(stockSedeRepository.findByProductoIdAndSedeId(10L, 1L)).thenReturn(Optional.of(stockExistente));

        // ACT (Ejecución)
        inventarioService.registrarMovimiento(request, username);

        // ASSERT (Validación)
        assertEquals(15, stockExistente.getCantidad(), "El stock debe ser 15 (5 inicial + 10 de entrada)");

        // Verifica que se llamaron a los métodos save
        verify(stockSedeRepository, times(1)).save(any(StockSede.class));
        verify(movimientoStockRepository, times(1)).save(any(MovimientoStock.class));
    }
}