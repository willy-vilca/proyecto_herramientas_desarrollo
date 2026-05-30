package SistemaContador.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummary {

    private Double ingresos;

    private Double gastos;

    private Double balance;
}