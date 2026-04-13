package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ArqueoRequest {
    private BigDecimal montoReal; // Lo que contó el vendedor
    private String observaciones;
}

