package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductoResponse {
    private Long id;
    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private Integer stockMinimoGlobal;
    private String urlImagen;
    private Boolean activo;
    private Long categoriaId;
    private String categoriaNombre;
}
