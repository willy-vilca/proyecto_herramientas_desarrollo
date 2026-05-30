package SistemaContador.service;

import SistemaContador.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {

    List<Transaction> getTransactions(
            Integer clientId,
            LocalDate startDate,
            LocalDate endDate
    );

    double[] getSummary(
            Integer clientId,
            LocalDate startDate,
            LocalDate endDate
    );

    Map<String,Double> getCategorySummary(
            Integer clientId,
            LocalDate startDate,
            LocalDate endDate
    );
}
