package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductoRequest {
    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private Integer stockMinimoGlobal;
    private String urlImagen;
    private Long categoriaId;
}
