package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;

@Data
public class TransferenciaStockRequest {
    private Long productoId;
    private Long sedeOrigenId;  // Solo lo usará el ADMIN
    private Long sedeDestinoId; // Obligatorio
    private Integer cantidad;
    private String motivo;
}