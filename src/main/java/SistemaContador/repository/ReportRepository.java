package SistemaContador.repository;

import SistemaContador.model.Transaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository
        extends JpaRepository<Transaction,Integer> {

    @Query(value = """
            SELECT t.*
            FROM transactions t
            WHERE t.client_id = :clientId
            AND t.transaction_date BETWEEN :startDate AND :endDate
            ORDER BY t.transaction_date DESC
            """,
            nativeQuery = true)
    List<Transaction> findTransactions(
            @Param("clientId") Integer clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
            SELECT type,
                   COALESCE(SUM(amount),0) total
            FROM transactions
            WHERE client_id = :clientId
            AND transaction_date BETWEEN :startDate AND :endDate
            GROUP BY type
            """,
            nativeQuery = true)
    List<Object[]> getSummary(
            @Param("clientId") Integer clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
            SELECT c.name,
                   COALESCE(SUM(t.amount),0) total
            FROM transactions t
            LEFT JOIN categories c
            ON t.category_id = c.category_id
            WHERE t.client_id = :clientId
            AND t.transaction_date BETWEEN :startDate AND :endDate
            GROUP BY c.name
            ORDER BY total DESC
            """,
            nativeQuery = true)
    List<Object[]> getCategorySummary(
            @Param("clientId") Integer clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
