package pe.idat.yomara.ferreteria_api.model.entity.venta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.Producto;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalle_venta")
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    // Precio original del producto en el momento de la venta
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    //  Descuento manual por producto
    @Column(precision = 10, scale = 2)
    private BigDecimal descuento;

    // El costo final de esta línea -> (cantidad * precioUnitario) - descuento
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Relación con Venta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore // Evita bucles infinitos en el JSON
    private Venta venta;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}
