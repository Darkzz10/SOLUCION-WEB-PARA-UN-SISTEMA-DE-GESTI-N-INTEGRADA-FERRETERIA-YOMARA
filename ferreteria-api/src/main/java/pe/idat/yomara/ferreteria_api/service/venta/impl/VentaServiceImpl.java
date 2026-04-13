package pe.idat.yomara.ferreteria_api.service.venta.impl;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.request.VentaRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.VentaDetalleResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.VentaResponse;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.MovimientoStock;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.Producto;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.StockSede;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.TipoMovimiento;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Sede;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Cliente; // Asumiendo tu ruta
import pe.idat.yomara.ferreteria_api.model.entity.venta.EstadoVenta;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Venta;
import pe.idat.yomara.ferreteria_api.model.entity.venta.VentaDetalle;
import pe.idat.yomara.ferreteria_api.repository.inventario.MovimientoStockRepository;
import pe.idat.yomara.ferreteria_api.repository.inventario.ProductoRepository;
import pe.idat.yomara.ferreteria_api.repository.inventario.StockSedeRepository;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;
import pe.idat.yomara.ferreteria_api.repository.venta.ClienteRepository; // Asumiendo tu ruta
import pe.idat.yomara.ferreteria_api.repository.venta.VentaRepository;
import pe.idat.yomara.ferreteria_api.service.venta.VentaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final StockSedeRepository stockSedeRepository;
    private final MovimientoStockRepository movimientoStockRepository;

    public VentaServiceImpl(VentaRepository ventaRepository, UsuarioRepository usuarioRepository,
                            ClienteRepository clienteRepository, ProductoRepository productoRepository,
                            StockSedeRepository stockSedeRepository, MovimientoStockRepository movimientoStockRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.stockSedeRepository = stockSedeRepository;
        this.movimientoStockRepository = movimientoStockRepository;
    }

    @Override
    @Transactional //  O se guarda toda la venta con sus descuentos de stock, o se anula todo.
    public VentaResponse registrarVenta(VentaRequest request, String usernameAutenticado) {

        // 1. Obtenemos el usuario de forma directa (Confiamos en el filtro de Seguridad)
        Usuario usuario = usuarioRepository.findByUsername(usernameAutenticado).get();
        Sede sedeVenta = usuario.getSede();

        // 2. Validar Cliente (Aquí sí mantenemos el orElseThrow porque el ID viene del Request, no del Token)
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 3. Crear cabecera de la Venta
        Venta venta = Venta.builder()
                .codigoOperacion("VEN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .tipoComprobante(request.getTipoComprobante())
                .medioPago(request.getMedioPago())
                .estado("COMPLETADA") // Estado administrativo para arqueo de caja

                //  LÓGICA DE LOGÍSTICA: Conecta con la App del transportista
                .tipoEntrega(request.getTipoEntrega())
                .direccionEntrega(request.getDireccionEntrega())

                // Si la entrega es DOMICILIO, el estado nace PENDIENTE para que aparezca en el Android
                .estadoVenta(request.getTipoEntrega() == Venta.TipoEntrega.DOMICILIO ?
                        EstadoVenta.PENDIENTE_DESPACHO : EstadoVenta.COMPLETADA)

                .cliente(cliente)
                .usuario(usuario)
                .sede(sedeVenta)
                .detalles(new ArrayList<>())
                .build();

        BigDecimal subtotalBruto = BigDecimal.ZERO;
        BigDecimal sumaDescuentosDetalles = BigDecimal.ZERO;

        // 4. Procesar Detalles y Stock
        for (var detReq : request.getDetalles()) {

            Producto producto = productoRepository.findById(detReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detReq.getProductoId()));

            // A. Verificar Stock en la Sede del Vendedor
            StockSede stock = stockSedeRepository.findByProductoIdAndSedeId(producto.getId(), sedeVenta.getId())
                    .orElseThrow(() -> new RuntimeException("El producto '" + producto.getNombre() + "' no tiene registro de stock en tu sede."));

            if (stock.getCantidad() < detReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para '" + producto.getNombre() + "'. Tienes: " + stock.getCantidad() + ", solicitas: " + detReq.getCantidad());
            }

            // B. Descontar Stock y registrar Kardex
            stock.setCantidad(stock.getCantidad() - detReq.getCantidad());
            stockSedeRepository.save(stock);

            movimientoStockRepository.save(MovimientoStock.builder()
                    .tipoMovimiento(TipoMovimiento.VENTA)
                    .cantidad(detReq.getCantidad())
                    .motivo("Venta Op: " + venta.getCodigoOperacion())
                    .producto(producto)
                    .sede(sedeVenta)
                    .usuario(usuario)
                    .build());

            // C. Lógica Financiera Segura (Usamos el precio de la BD, no del request)
            BigDecimal precioUnidad = producto.getPrecioBase();
            BigDecimal cantidadBD = new BigDecimal(detReq.getCantidad());
            BigDecimal descLinea = detReq.getDescuento() != null ? detReq.getDescuento() : BigDecimal.ZERO;

            BigDecimal totalLinea = (precioUnidad.multiply(cantidadBD)).subtract(descLinea);

            subtotalBruto = subtotalBruto.add(precioUnidad.multiply(cantidadBD));
            sumaDescuentosDetalles = sumaDescuentosDetalles.add(descLinea);

            // D. Agregar Detalle a la Venta
            VentaDetalle detalle = VentaDetalle.builder()
                    .cantidad(detReq.getCantidad())
                    .precioUnitario(precioUnidad) // El original
                    .descuento(descLinea)
                    .subtotal(totalLinea) // Lo que realmente costó
                    .producto(producto)
                    .venta(venta)
                    .build();

            venta.getDetalles().add(detalle);
        }

        // 5. Cálculos Finales de Cabecera (Subtotal, Descuentos, IGV)
        BigDecimal descuentoGlobal = request.getDescuentoTotal() != null ? request.getDescuentoTotal() : BigDecimal.ZERO;
        BigDecimal totalDescuentos = sumaDescuentosDetalles.add(descuentoGlobal);

        BigDecimal subtotalNeto = subtotalBruto.subtract(totalDescuentos);
        if (subtotalNeto.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Error: Los descuentos no pueden ser mayores al total de la venta.");
        }

        BigDecimal impuesto = subtotalNeto.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalFinal = subtotalNeto.add(impuesto).setScale(2, RoundingMode.HALF_UP);

        venta.setSubtotal(subtotalNeto);
        venta.setDescuentoTotal(totalDescuentos);
        venta.setImpuesto(impuesto);
        venta.setTotal(totalFinal);

        // 6. Guardar en BD (El CascadeType.ALL guardará los detalles automáticamente)
        Venta ventaGuardada = ventaRepository.save(venta);

        // 7. Mapear a DTO Aplanado
        return convertirADto(ventaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarComprobantePdf(Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // 1. CABECERA
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("FERRETERIA YOMARA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph("Comprobante Interno: " + venta.getCodigoOperacion()));
            document.add(new Paragraph("Fecha: " + venta.getFechaVenta()));
            document.add(new Paragraph("Sede: " + venta.getSede().getNombre()));
            document.add(new Paragraph("Vendedor: " + venta.getUsuario().getUsername()));
            document.add(new Paragraph("--------------------------------------------------"));

            // 2. DATOS DEL CLIENTE
            document.add(new Paragraph("Cliente: " + venta.getCliente().getNombreCompleto()));
            document.add(new Paragraph("Documento: " + venta.getCliente().getDocumentoNumero()));
            document.add(new Paragraph(" ")); // Espacio en blanco

            // 3. TABLA DE PRODUCTOS
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 1f, 2f, 2f});

            table.addCell(new PdfPCell(new Phrase("Producto", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
            table.addCell(new PdfPCell(new Phrase("Cant.", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
            table.addCell(new PdfPCell(new Phrase("P.Unit", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
            table.addCell(new PdfPCell(new Phrase("Subtotal", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

            for (var detalle : venta.getDetalles()) {
                table.addCell(detalle.getProducto().getNombre());
                table.addCell(String.valueOf(detalle.getCantidad()));
                table.addCell("S/ " + detalle.getPrecioUnitario().toString());
                table.addCell("S/ " + detalle.getSubtotal().toString());
            }
            document.add(table);
            document.add(new Paragraph(" "));

            // 4. TOTALES
            Paragraph totales = new Paragraph();
            totales.setAlignment(Element.ALIGN_RIGHT);
            totales.add("Subtotal: S/ " + venta.getSubtotal() + "\n");
            totales.add("Descuentos: S/ " + venta.getDescuentoTotal() + "\n");
            totales.add("IGV (18%): S/ " + venta.getImpuesto() + "\n\n");

            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            totales.add(new Phrase("TOTAL A PAGAR: S/ " + venta.getTotal(), fontTotal));

            document.add(totales);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF del comprobante", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarTodas() {
        // Trae todas las ventas de la BD y las convierte a DTOs aplanados
        return ventaRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse buscarPorId(Long id) {
        // Busca por ID o lanza error si no existe
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        return convertirADto(venta);
    }

    // --- Método Ayudante Actualizado para mostrar datos de Entrega ---
    private VentaResponse convertirADto(Venta venta) {
        VentaResponse resp = new VentaResponse();
        resp.setId(venta.getId());
        resp.setCodigoOperacion(venta.getCodigoOperacion());
        resp.setTipoComprobante(venta.getTipoComprobante());
        if (venta.getMedioPago() != null) {
            resp.setMedioPago(venta.getMedioPago().name());
        }
        resp.setSubtotal(venta.getSubtotal());
        resp.setDescuentoTotal(venta.getDescuentoTotal());
        resp.setImpuesto(venta.getImpuesto());
        resp.setTotal(venta.getTotal());
        resp.setEstado(venta.getEstado());
        resp.setFechaVenta(venta.getFechaVenta());

        // MAPEO DE LOGÍSTICA: Esto lo recibe Angular para mostrar el estado del envío
        if (venta.getEstadoVenta() != null) {
            resp.setEstadoVenta(venta.getEstadoVenta().name());
        }
        if (venta.getTipoEntrega() != null) {
            resp.setTipoEntrega(venta.getTipoEntrega().name());
        }
        resp.setDireccionEntrega(venta.getDireccionEntrega());
        resp.setEvidenciaEntrega(venta.getEvidenciaEntrega()); // Foto/Firma que sube el Android

        // Mapeo de Cliente (Igual)
        resp.setClienteId(venta.getCliente().getId());
        resp.setClienteDocumento(venta.getCliente().getDocumentoNumero());
        resp.setClienteNombre(venta.getCliente().getNombreCompleto());

        // Mapeo de Vendedor y Sede (Igual)
        resp.setVendedorId(venta.getUsuario().getId());
        resp.setVendedorNombre(venta.getUsuario().getUsername());
        resp.setSedeId(venta.getSede().getId());
        resp.setSedeNombre(venta.getSede().getNombre());

        // Mapeo de Detalles (Igual)
        resp.setDetalles(venta.getDetalles().stream().map(d -> {
            VentaDetalleResponse detResp = new VentaDetalleResponse();
            detResp.setId(d.getId());
            detResp.setCantidad(d.getCantidad());
            detResp.setPrecioUnitario(d.getPrecioUnitario());
            detResp.setDescuento(d.getDescuento());
            detResp.setSubtotal(d.getSubtotal());
            detResp.setProductoId(d.getProducto().getId());
            detResp.setProductoNombre(d.getProducto().getNombre());
            detResp.setProductoSku(d.getProducto().getSku());
            return detResp;
        }).toList());

        return resp;
    }
}