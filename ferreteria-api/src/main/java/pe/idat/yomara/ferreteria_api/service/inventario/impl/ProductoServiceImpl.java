package pe.idat.yomara.ferreteria_api.service.inventario.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.request.ProductoRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ProductoResponse;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.Categoria;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.Producto;
import pe.idat.yomara.ferreteria_api.repository.inventario.CategoriaRepository;
import pe.idat.yomara.ferreteria_api.repository.inventario.ProductoRepository;
import pe.idat.yomara.ferreteria_api.service.inventario.ProductoService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        validarNegocio(request);

        // 1. Validar SKU único
        if (productoRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("Error: El SKU '" + request.getSku() + "' ya está registrado.");
        }

        // 2.  AQUÍ USAMOS EL MÉTODO: Validar Nombre único
        if (productoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Error: Ya existe un producto con el nombre '" + request.getNombre() + "'.");
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        Producto producto = Producto.builder()
                .sku(request.getSku())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precioBase(request.getPrecioBase())
                .stockMinimoGlobal(request.getStockMinimoGlobal())
                .urlImagen(request.getUrlImagen())
                .activo(true)
                .categoria(categoria)
                .build();

        return convertirADto(productoRepository.save(producto));
    }

    @Override
    public ProductoResponse buscarPorSku(String sku) {
        // Usamos findBySku (exacto) en lugar del que busca por nombre (parcial)
        return productoRepository.findBySku(sku)
                .map(this::convertirADto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con SKU: " + sku));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Producto no encontrado."));

        validarNegocio(request);

        // 3. Validar SKU (si cambió, que no exista en otro)
        if (!producto.getSku().equals(request.getSku()) && productoRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("Error: El nuevo SKU '" + request.getSku() + "' ya lo tiene otro producto.");
        }

        // 4. AQUÍ USAMOS EL MÉTODO: Validar Nombre (si cambió, que no exista en otro)
        if (!producto.getNombre().equalsIgnoreCase(request.getNombre()) &&
                productoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Error: Ya existe otro producto con el nombre '" + request.getNombre() + "'.");
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        producto.setSku(request.getSku());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioBase(request.getPrecioBase());
        producto.setStockMinimoGlobal(request.getStockMinimoGlobal());
        producto.setUrlImagen(request.getUrlImagen());
        producto.setCategoria(categoria);

        return convertirADto(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarActivos() {
        // Solo devuelve los que están en true
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertirADto)
                .toList();
    }

    // filtra por activos.
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombreOSku(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return listarActivos(); // Si no hay filtro, muestra solo los activos
        }
        return productoRepository.findByActivoTrueAndNombreContainingIgnoreCaseOrActivoTrueAndSkuContainingIgnoreCase(filtro, filtro)
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    @Override
    public ProductoResponse buscarPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::convertirADto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
    }

    @Override
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    private void validarNegocio(ProductoRequest request) {
        if (request.getPrecioBase() == null || request.getPrecioBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0.");
        }
        if (request.getCategoriaId() == null) {
            throw new RuntimeException("La categoría es obligatoria.");
        }
    }

    private ProductoResponse convertirADto(Producto producto) {
        ProductoResponse resp = new ProductoResponse();
        resp.setId(producto.getId());
        resp.setSku(producto.getSku());
        resp.setNombre(producto.getNombre());
        resp.setDescripcion(producto.getDescripcion());
        resp.setPrecioBase(producto.getPrecioBase());
        resp.setStockMinimoGlobal(producto.getStockMinimoGlobal());
        resp.setUrlImagen(producto.getUrlImagen());
        resp.setActivo(producto.getActivo());
        if (producto.getCategoria() != null) {
            resp.setCategoriaId(producto.getCategoria().getId());
            resp.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        return resp;
    }
}