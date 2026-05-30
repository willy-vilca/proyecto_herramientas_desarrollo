package SistemaContador.service.impl;

import SistemaContador.dto.DashboardSummary;
import SistemaContador.model.Transaction;
import SistemaContador.repository.TransactionRepository;
import SistemaContador.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final TransactionRepository repository;

    @Override
    public DashboardSummary getSummary(Integer userId) {

        Double ingresos =
                repository.totalIngresos(userId);

        Double gastos =
                repository.totalGastos(userId);

        return new DashboardSummary(
                ingresos,
                gastos,
                ingresos-gastos
        );
    }

    @Override
    public List<Transaction> getLastMovements(Integer userId) {

        List<Object[]> rows =
                repository.findLastMovements(userId);

        List<Transaction> list =
                new ArrayList<>();

        for(Object[] row : rows){

            Transaction t =
                    new Transaction();

            t.setClientName((String) row[0]);
            t.setType((String) row[1]);
            t.setAmount(
                    ((Number) row[2]).doubleValue()
            );

            t.setTransactionDate(
                    ((Date) row[3]).toLocalDate()
            );

            t.setDescription((String) row[4]);

            list.add(t);
        }

        return list;
    }
}
