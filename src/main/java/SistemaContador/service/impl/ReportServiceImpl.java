package SistemaContador.service.impl;

import SistemaContador.model.Transaction;
import SistemaContador.repository.ReportRepository;
import SistemaContador.service.ReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl
        implements ReportService {

    private final ReportRepository repository;

    @Override
    public List<Transaction> getTransactions(
            Integer clientId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        return repository.findTransactions(
                clientId,
                startDate,
                endDate
        );
    }

    @Override
    public double[] getSummary(
            Integer clientId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        double ingresos = 0;
        double gastos = 0;

        List<Object[]> rows =
                repository.getSummary(
                        clientId,
                        startDate,
                        endDate
                );

        for(Object[] row : rows){

            String type =
                    (String) row[0];

            Double total =
                    ((Number) row[1]).doubleValue();

            if("INGRESO".equalsIgnoreCase(type)){
                ingresos = total;
            }

            if("GASTO".equalsIgnoreCase(type)){
                gastos = total;
            }
        }

        return new double[]{
                ingresos,
                gastos,
                ingresos - gastos
        };
    }

    @Override
    public Map<String, Double> getCategorySummary(
            Integer clientId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        List<Object[]> rows =
                repository.getCategorySummary(
                        clientId,
                        startDate,
                        endDate
                );

        Map<String,Double> result =
                new LinkedHashMap<>();

        for(Object[] row : rows){

            result.put(
                    (String) row[0],
                    ((Number) row[1]).doubleValue()
            );
        }

        return result;
    }
}