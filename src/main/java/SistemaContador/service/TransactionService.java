package SistemaContador.service;

import SistemaContador.model.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getByClient(Integer clientId);

    void save(Transaction transaction);

    void update(Transaction transaction);

    void delete(Integer id);

    double[] getSummaryByClient(Integer clientId);

    double[] getSummaryByUser(Integer userId);

    List<Object[]> getLastMovements(Integer userId);
}