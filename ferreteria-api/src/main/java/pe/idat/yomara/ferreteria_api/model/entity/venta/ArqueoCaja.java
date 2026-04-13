package pe.idat.yomara.ferreteria_api.model.entity.venta;

import jakarta.persistence.*;
import lombok.*;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Sede;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arqueos_caja")
public class ArqueoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCierre;

    // Monto calculado por el sistema (suma de ventas)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoEsperado;

    // Lo que el vendedor dice que tiene físicamente
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoReal;

    // Diferencia (Real - Esperado). Si es negativo, falta dinero.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal diferencia;

    @Column(length = 255)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;
}
