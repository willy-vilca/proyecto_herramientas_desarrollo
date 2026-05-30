package SistemaContador.service;

import SistemaContador.dto.DashboardSummary;
import SistemaContador.model.Transaction;

import java.util.List;

public interface DashboardService {

    DashboardSummary getSummary(Integer userId);

    List<Transaction> getLastMovements(Integer userId);
}
