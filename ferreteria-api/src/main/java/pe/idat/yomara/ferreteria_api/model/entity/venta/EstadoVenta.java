package pe.idat.yomara.ferreteria_api.model.entity.venta;

public enum EstadoVenta {
    COMPLETADA,             // Venta procesada
    PENDIENTE_DESPACHO,     // Esperando en almacén
    EN_RUTA,                // En el camión
    ENTREGADO,              // Entregado en domicilio (con evidencia)
    ENTREGADO_EN_TIENDA,    // Cliente se lo llevó al momento
    ANULADO                 // Venta cancelada
}