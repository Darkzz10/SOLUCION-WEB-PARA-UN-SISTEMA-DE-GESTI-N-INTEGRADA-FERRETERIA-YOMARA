package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;

@Data
public class SedeRequest {
    private String nombre;
    private String direccion;
    private String telefono;
}