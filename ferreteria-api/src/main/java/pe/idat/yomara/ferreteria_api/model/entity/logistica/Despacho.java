package pe.idat.yomara.ferreteria_api.model.entity.logistica;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Venta;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "despachos")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direccion_destino", nullable = false, length = 250)
    private String direccionDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_despacho", nullable = false, length = 20)
    private EstadoDespacho estadoDespacho;

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Column(name = "evidencia_foto_url", length = 500)
    private String evidenciaFotoUrl;

    // Un despacho pertenece a una única venta
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    private Venta venta;

    // El transportista es un Usuario del sistema
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportista_id")
    private Usuario transportista;
}
