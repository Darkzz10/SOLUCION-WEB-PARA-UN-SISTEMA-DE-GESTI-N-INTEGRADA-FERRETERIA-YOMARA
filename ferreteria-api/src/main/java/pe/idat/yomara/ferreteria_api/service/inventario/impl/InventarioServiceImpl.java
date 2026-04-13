package pe.idat.yomara.ferreteria_api.service.inventario.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.request.MovimientoStockRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.AlertaStockResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.DetalleStockSede;
import pe.idat.yomara.ferreteria_api.model.dto.response.MovimientoResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.StockGlobalResponse;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.MovimientoStock;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.Producto;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.StockSede;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.TipoMovimiento;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Rol;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Sede;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;
import pe.idat.yomara.ferreteria_api.repository.inventario.MovimientoStockRepository;
import pe.idat.yomara.ferreteria_api.repository.inventario.ProductoRepository;
import pe.idat.yomara.ferreteria_api.repository.inventario.StockSedeRepository;
import pe.idat.yomara.ferreteria_api.repository.usuario.SedeRepository;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;
import pe.idat.yomara.ferreteria_api.service.inventario.InventarioService;
import pe.idat.yomara.ferreteria_api.model.dto.request.TransferenciaStockRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final StockSedeRepository stockSedeRepository;
    private final MovimientoStockRepository movimientoStockRepository;
    private final ProductoRepository productoRepository;
    private final SedeRepository sedeRepository;
    private final UsuarioRepository usuarioRepository;

    public InventarioServiceImpl(StockSedeRepository stockSedeRepository,
                                 MovimientoStockRepository movimientoStockRepository,
                                 ProductoRepository productoRepository,
                                 SedeRepository sedeRepository,
                                 UsuarioRepository usuarioRepository) {
        this.stockSedeRepository = stockSedeRepository;
        this.movimientoStockRepository = movimientoStockRepository;
        this.productoRepository = productoRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public StockGlobalResponse consultarStockPriorizado(Long productoId, String usernameAutenticado) {
        Usuario usuario = usuarioRepository.findByUsername(usernameAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Long sedeIdLocal = usuario.getSede().getId();

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        List<StockSede> todosLosStocks = stockSedeRepository.findByProductoId(productoId);

        List<DetalleStockSede> detalles = todosLosStocks.stream()
                .map(stock -> {
                    DetalleStockSede d = new DetalleStockSede();
                    d.setSedeId(stock.getSede().getId());
                    d.setNombreSede(stock.getSede().getNombre());
                    d.setCantidad(stock.getCantidad());
                    d.setEsSedeLocal(stock.getSede().getId().equals(sedeIdLocal));
                    return d;
                })
                .sorted((a, b) -> Boolean.compare(b.getEsSedeLocal(), a.getEsSedeLocal()))
                .toList();

        Integer total = detalles.stream().mapToInt(DetalleStockSede::getCantidad).sum();

        StockGlobalResponse response = new StockGlobalResponse();
        response.setNombreProducto(producto.getNombre());
        response.setSku(producto.getSku());
        response.setTotalGlobal(total);
        response.setExistencias(detalles);

        return response;
    }

    @Override
    @Transactional
    public void registrarMovimiento(MovimientoStockRequest request, String usernameAutenticado) {
        // 1. Obtener usuario y contexto
        Usuario usuario = usuarioRepository.findByUsername(usernameAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rolUsuario = usuario.getRol();

        // 2. Seguridad interna (Doble validación por si falla el controlador)
        if (rolUsuario != Rol.ADMIN && rolUsuario != Rol.ALMACENERO) {
            throw new RuntimeException("Acceso denegado: Rol sin permisos de inventario.");
        }

        //  3. AQUI ESTÁ EL CAMBIO: Llamamos a tu nuevo método estricto
        Sede sedeAfectada = determinarSedeAfectada(usuario, request.getSedeId());

        // 4. Validar reglas del movimiento
        validarPermisoMovimiento(rolUsuario, request.getTipoMovimiento());

        // 5. Procesar Stock
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        StockSede stockSede = stockSedeRepository.findByProductoIdAndSedeId(producto.getId(), sedeAfectada.getId())
                .orElse(StockSede.builder().producto(producto).sede(sedeAfectada).cantidad(0).build());

        actualizarCantidadStock(stockSede, request.getCantidad(), request.getTipoMovimiento());

        stockSedeRepository.save(stockSede);

        // 6. Guardar Auditoría (Kardex)
        MovimientoStock movimiento = MovimientoStock.builder()
                .tipoMovimiento(request.getTipoMovimiento())
                .cantidad(request.getCantidad())
                .motivo(request.getMotivo())
                .producto(producto)
                .sede(sedeAfectada)
                .usuario(usuario)
                .build();

        movimientoStockRepository.save(movimiento);
    }

    @Override
    @Transactional // Si falla el ingreso en B, se cancela la salida de A.
    public void trasladarStock(TransferenciaStockRequest request, String usernameAutenticado) {
        // 1. Contexto de seguridad
        Usuario usuario = usuarioRepository.findByUsername(usernameAutenticado).get();

        // Determinar origen (Si es Almacenero, su sede es fija. Si es Admin, puede elegir origen)
        Long origenId = (usuario.getRol() == Rol.ADMIN && request.getSedeOrigenId() != null)
                ? request.getSedeOrigenId() : usuario.getSede().getId();

        if (request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad a trasladar debe ser mayor a cero.");
        }


        if (origenId.equals(request.getSedeDestinoId())) {
            throw new RuntimeException("No puedes realizar un traslado a la misma sede de origen.");
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        //  PROCESAR SALIDA DE SEDE ORIGEN
        StockSede stockOrigen = stockSedeRepository.findByProductoIdAndSedeId(producto.getId(), origenId)
                .orElseThrow(() -> new RuntimeException("No hay registro de stock en la sede de origen."));

        if (stockOrigen.getCantidad() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente en origen. Disponible: " + stockOrigen.getCantidad());
        }

        stockOrigen.setCantidad(stockOrigen.getCantidad() - request.getCantidad());
        stockSedeRepository.save(stockOrigen);

        // Registro Kardex de Salida
        movimientoStockRepository.save(MovimientoStock.builder()
                .tipoMovimiento(TipoMovimiento.TRASLADO_SALIDA) //  Asegúrate de tener este Enum
                .cantidad(request.getCantidad())
                .motivo("Traslado a sede ID " + request.getSedeDestinoId() + ": " + request.getMotivo())
                .producto(producto)
                .sede(stockOrigen.getSede())
                .usuario(usuario)
                .build());

        // PROCESAR ENTRADA EN SEDE DESTINO
        Sede sedeDestino = sedeRepository.findById(request.getSedeDestinoId())
                .orElseThrow(() -> new RuntimeException("Sede destino no encontrada"));

        StockSede stockDestino = stockSedeRepository.findByProductoIdAndSedeId(producto.getId(), sedeDestino.getId())
                .orElse(StockSede.builder().producto(producto).sede(sedeDestino).cantidad(0).build());

        stockDestino.setCantidad(stockDestino.getCantidad() + request.getCantidad());
        stockSedeRepository.save(stockDestino);

        // Registro Kardex de Entrada
        movimientoStockRepository.save(MovimientoStock.builder()
                .tipoMovimiento(TipoMovimiento.TRASLADO_ENTRADA)
                .cantidad(request.getCantidad())
                .motivo("Traslado desde sede ID " + origenId + ": " + request.getMotivo())
                .producto(producto)
                .sede(sedeDestino)
                .usuario(usuario)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> obtenerKardex(Long sedeId, TipoMovimiento tipo, LocalDate inicio, LocalDate fin) {
        // Convertimos LocalDate a LocalDateTime para la consulta
        LocalDateTime fecInicio = (inicio != null) ? inicio.atStartOfDay() : null;
        LocalDateTime fecFin = (fin != null) ? fin.atTime(23, 59, 59) : null;

        return movimientoStockRepository.buscarConFiltros(sedeId, tipo, fecInicio, fecFin)
                .stream()
                .map(this::convertirMovimientoADto)
                .toList();
    }

    @Override
    @Transactional
    public void anularMovimiento(Long movimientoId, String motivo, String usernameAutenticado) {
        // 1. Contexto
        Usuario usuarioAnulador = usuarioRepository.findByUsername(usernameAutenticado).get();
        MovimientoStock movOriginal = movimientoStockRepository.findById(movimientoId)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // 2. Validación: No anular algo ya anulado
        if (movOriginal.getMotivo().contains("[ANULADO]")) {
            throw new RuntimeException("Este movimiento ya se encuentra anulado.");
        }

        // 3. Lógica de Reversión de Stock
        StockSede stock = stockSedeRepository.findByProductoIdAndSedeId(
                movOriginal.getProducto().getId(), movOriginal.getSede().getId()).get();

        // Si el original fue ENTRADA, la anulación RESTA. Si fue SALIDA, la anulación SUMA.
        boolean esEntrada = (movOriginal.getTipoMovimiento() == TipoMovimiento.ENTRADA_PROVEEDOR ||
                movOriginal.getTipoMovimiento() == TipoMovimiento.TRASLADO_ENTRADA);

        if (esEntrada) {
            stock.setCantidad(stock.getCantidad() - movOriginal.getCantidad());
        } else {
            stock.setCantidad(stock.getCantidad() + movOriginal.getCantidad());
        }
        stockSedeRepository.save(stock);

        // 4. Registrar Movimiento de Compensación (Kardex)
        movimientoStockRepository.save(MovimientoStock.builder()
                .tipoMovimiento(TipoMovimiento.AJUSTE)
                .cantidad(movOriginal.getCantidad())
                .motivo("ANULACIÓN de Op #" + movOriginal.getId() + ": " + motivo)
                .producto(movOriginal.getProducto())
                .sede(movOriginal.getSede())
                .usuario(usuarioAnulador)
                .build());

        // 5. Marcar el original como anulado (opcional: puedes tener un booleano en la entidad)
        movOriginal.setMotivo("[ANULADO] " + movOriginal.getMotivo());
        movimientoStockRepository.save(movOriginal);
    }

    private MovimientoResponse convertirMovimientoADto(MovimientoStock m) {
        MovimientoResponse res = new MovimientoResponse();
        res.setId(m.getId());
        res.setFecha(m.getFechaMovimiento()); // Asumiendo que tu entidad tiene @CreatedDate o LocalDateTime.now()
        res.setTipoMovimiento(m.getTipoMovimiento());
        res.setCantidad(m.getCantidad());
        res.setMotivo(m.getMotivo());
        res.setProductoNombre(m.getProducto().getNombre());
        res.setSedeNombre(m.getSede().getNombre());
        res.setUsuarioNombre(m.getUsuario().getNombreCompleto());
        res.setAnulado(m.getMotivo().startsWith("[ANULADO]"));
        return res;
    }



    private void validarPermisoMovimiento(Rol rol, TipoMovimiento tipo) {
        if (rol == Rol.ALMACENERO && tipo == TipoMovimiento.VENTA) {
            throw new RuntimeException("Regla de negocio: El Almacenero no puede registrar ventas directas desde este módulo.");
        }
    }

    private void actualizarCantidadStock(StockSede stock, Integer cantidad, TipoMovimiento tipo) {
        if (tipo == TipoMovimiento.ENTRADA_PROVEEDOR || tipo == TipoMovimiento.TRASLADO_ENTRADA || tipo == TipoMovimiento.AJUSTE) {
            stock.setCantidad(stock.getCantidad() + cantidad);
        } else {
            if (stock.getCantidad() < cantidad) {
                throw new RuntimeException("Stock insuficiente en " + stock.getSede().getNombre() + ". Actual: " + stock.getCantidad());
            }
            stock.setCantidad(stock.getCantidad() - cantidad);
        }
    }

    private Sede determinarSedeAfectada(Usuario usuario, Long requestedSedeId) {

        if (usuario.getRol() == Rol.ADMIN) {
            // 1. El ADMIN puede enviar un ID para afectar otra sede, o no enviar nada y afectar la suya.
            Long idSedeFinal = (requestedSedeId != null) ? requestedSedeId : usuario.getSede().getId();
            return sedeRepository.findById(idSedeFinal)
                    .orElseThrow(() -> new RuntimeException("Sede destino no encontrada"));

        } else {
            // 2. Lógica para el ALMACENERO: ¡Prohibido enviar sedeId!
            if (requestedSedeId != null) {
                throw new RuntimeException("Error de validación: Como ALMACENERO no tienes permitido especificar el campo 'sedeId'. El sistema registra tus movimientos automáticamente en tu sede asignada.");
            }

            // Si obedeció y mandó el sedeId nulo (o no lo mandó), le asignamos su propia sede.
            return usuario.getSede();
        }
    }

    public List<AlertaStockResponse> obtenerAlertasDeStock() {

        List<Producto> productos = productoRepository.findAll();

        //  Transformamos cada producto en un reporte de alerta
        return productos.stream().map(producto -> {

            // Calculamos cuánto stock hay en TODAS las sedes para este producto
            List<StockSede> stocksDelProducto = stockSedeRepository.findByProductoId(producto.getId());
            Integer stockTotal = stocksDelProducto.stream().mapToInt(StockSede::getCantidad).sum();

            // Comparamos contra el stock mínimo global del producto
            Integer stockMinimo = producto.getStockMinimoGlobal() != null ? producto.getStockMinimoGlobal() : 0;
            boolean requiereAtencion = stockTotal <= stockMinimo; //  Aquí nace la alerta

            return AlertaStockResponse.builder()
                    .productoId(producto.getId())
                    .nombreProducto(producto.getNombre())
                    .sku(producto.getSku())
                    .stockActualTotal(stockTotal)
                    .stockMinimoRequerido(stockMinimo)
                    .alertaRoja(requiereAtencion) // true si el stock es crítico
                    .build();

        }).toList();
    }


}