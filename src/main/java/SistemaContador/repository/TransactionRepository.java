package SistemaContador.repository;

import SistemaContador.model.Transaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction,Integer> {

    @Query(value = """
            SELECT
                COALESCE(SUM(
                    CASE
                        WHEN t.type='INGRESO'
                        THEN t.amount
                    END
                ),0)
            FROM transactions t
            INNER JOIN clients c
                ON t.client_id=c.client_id
            WHERE c.user_id=:userId
            AND c.status=true
            """,
            nativeQuery = true)
    Double totalIngresos(@Param("userId") Integer userId);

    @Query(value = """
            SELECT
                COALESCE(SUM(
                    CASE
                        WHEN t.type='GASTO'
                        THEN t.amount
                    END
                ),0)
            FROM transactions t
            INNER JOIN clients c
                ON t.client_id=c.client_id
            WHERE c.user_id=:userId
            AND c.status=true
            """,
            nativeQuery = true)
    Double totalGastos(@Param("userId") Integer userId);

    @Query(value = """
SELECT
c.full_name as clientName,
t.type,
t.amount,
t.transaction_date,
t.description
FROM transactions t
INNER JOIN clients c
ON t.client_id = c.client_id
WHERE c.user_id = :userId
AND c.status = true
ORDER BY t.transaction_date DESC
LIMIT 10
""", nativeQuery = true)
    List<Object[]> findLastMovements(@Param("userId") Integer userId);

}