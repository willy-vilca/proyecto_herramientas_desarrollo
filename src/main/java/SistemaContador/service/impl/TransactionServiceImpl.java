package SistemaContador.service.impl;

import SistemaContador.model.Transaction;
import SistemaContador.repository.TransactionRepository;
import SistemaContador.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl
        implements TransactionService {

    private final TransactionRepository repository;

    @Override
    public List<Transaction> getByClient(
            Integer clientId
    ) {

        List<Object[]> rows =
                repository.findMovementsByClient(clientId);

        List<Transaction> list =
                new ArrayList<>();

        for(Object[] row : rows){

            Transaction t =
                    new Transaction();

            t.setTransactionId(
                    ((Number) row[0]).intValue()
            );

            t.setClientId(
                    ((Number) row[1]).intValue()
            );

            if(row[2] != null){

                t.setCategoryId(
                        ((Number) row[2]).intValue()
                );
            }

            t.setCategoryName(
                    (String) row[3]
            );

            t.setType(
                    (String) row[4]
            );

            t.setAmount(
                    ((Number) row[5]).doubleValue()
            );

            t.setTransactionDate(
                    (LocalDate) row[6]
            );

            t.setDescription(
                    (String) row[7]
            );

            list.add(t);
        }

        return list;
    }

    @Override
    public void save(Transaction transaction) {

        repository.save(transaction);
    }

    @Override
    public void update(Transaction transaction) {

        repository.save(transaction);
    }

    @Override
    public void delete(Integer id) {

        repository.deleteById(id);
    }

    @Override
    public double[] getSummaryByClient(Integer clientId) {

        Double ingresos =
                repository.totalIngresosCliente(clientId);

        Double gastos =
                repository.totalGastosCliente(clientId);

        return new double[]{
                ingresos,
                gastos,
                ingresos - gastos
        };
    }

    @Override
    public double[] getSummaryByUser(Integer userId) {

        Double ingresos =
                repository.totalIngresos(userId);

        Double gastos =
                repository.totalGastos(userId);

        if (ingresos == null) ingresos = 0.0;
        if (gastos == null) gastos = 0.0;

        return new double[]{
                ingresos,
                gastos,
                ingresos - gastos
        };
    }

    @Override
    public List<Object[]> getLastMovements(
            Integer userId
    ) {
        return repository
                .findLastMovements(userId);
    }
}
