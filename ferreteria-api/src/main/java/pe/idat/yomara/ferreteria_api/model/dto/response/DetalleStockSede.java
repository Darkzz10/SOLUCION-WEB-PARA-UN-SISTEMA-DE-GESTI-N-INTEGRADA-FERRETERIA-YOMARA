package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;

@Data
public class DetalleStockSede {
    private Long sedeId;
    private String nombreSede;
    private Integer cantidad;
    private Boolean esSedeLocal;
}
