package com.srm.credit.engine.SRM_Credit_Engine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface LiquidationStatementRepository extends JpaRepository<Object, UUID> {

    /**
     * Consulta otimizada para extrair dados de liquidação com filtros
     * Usa SQL nativo para melhor performance em grandes volumes de dados
     *
     * @param startDate Data inicial do período
     * @param endDate Data final do período
     * @param cedenteName Nome do cedente (opcional)
     * @param currencyCode Código da moeda de pagamento (opcional)
     * @param pageable Informações de paginação
     * @return Página com itens de liquidação
     */
    @Query(value = """
            SELECT 
                t.id AS transactionId,
                r.id AS receivableId,
                'Cedente Sistema' AS cedenteName,
                rt.name AS receivableType,
                r.face_value AS faceValue,
                c.code AS currency,
                c.code AS paymentCurrency,
                r.maturity_date AS maturityDate,
                t.present_value AS presentValue,
                t.exchange_rate AS exchangeRate,
                t.final_value AS finalValue,
                t.created_at AS transactionDate
            FROM transactions t
            INNER JOIN receivables r ON t.receivable_id = r.id
            INNER JOIN receivable_types rt ON r.type_id = rt.id
            INNER JOIN currencies c ON r.currency_id = c.id
            WHERE t.created_at >= :startDate 
              AND t.created_at < :endDate
              AND (:cedenteName IS NULL OR 'Cedente Sistema' LIKE %:cedenteName%)
              AND (:currencyCode IS NULL OR c.code = :currencyCode)
            ORDER BY t.created_at DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM transactions t
            INNER JOIN receivables r ON t.receivable_id = r.id
            INNER JOIN receivable_types rt ON r.type_id = rt.id
            INNER JOIN currencies c ON r.currency_id = c.id
            WHERE t.created_at >= :startDate 
              AND t.created_at < :endDate
              AND (:cedenteName IS NULL OR 'Cedente Sistema' LIKE %:cedenteName%)
              AND (:currencyCode IS NULL OR c.code = :currencyCode)
            """,
            nativeQuery = true)
    Page<Map<String, Object>> findLiquidationStatement(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("cedenteName") String cedenteName,
            @Param("currencyCode") String currencyCode,
            Pageable pageable
    );

    /**
     * Consulta para totalizações de valores no período
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @param cedenteName Nome do cedente (opcional)
     * @param currencyCode Código da moeda (opcional)
     * @return Map com totalizações
     */
    @Query(value = """
            SELECT 
                SUM(r.face_value) AS totalFaceValue,
                SUM(t.final_value) AS totalFinalValue,
                COUNT(*) AS transactionCount
            FROM transactions t
            INNER JOIN receivables r ON t.receivable_id = r.id
            INNER JOIN receivable_types rt ON r.type_id = rt.id
            INNER JOIN currencies c ON r.currency_id = c.id
            WHERE t.created_at >= :startDate 
              AND t.created_at < :endDate
              AND (:cedenteName IS NULL OR 'Cedente Sistema' LIKE %:cedenteName%)
              AND (:currencyCode IS NULL OR c.code = :currencyCode)
            """,
            nativeQuery = true)
    Map<String, Object> getLiquidationTotals(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("cedenteName") String cedenteName,
            @Param("currencyCode") String currencyCode
    );

    /**
     * Consulta para análise de volumes por moeda no período
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista com análise por moeda
     */
    @Query(value = """
            SELECT 
                c.code AS currency,
                COUNT(*) AS transactionCount,
                SUM(r.face_value) AS totalFaceValue,
                SUM(t.final_value) AS totalFinalValue,
                AVG(t.exchange_rate) AS avgExchangeRate
            FROM transactions t
            INNER JOIN receivables r ON t.receivable_id = r.id
            INNER JOIN currencies c ON r.currency_id = c.id
            WHERE t.created_at >= :startDate 
              AND t.created_at < :endDate
            GROUP BY c.code
            ORDER BY SUM(t.final_value) DESC
            """,
            nativeQuery = true)
    List<Map<String, Object>> getAnalyticsByVolume(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Consulta para análise de volumes por tipo de recebível
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista com análise por tipo
     */
    @Query(value = """
            SELECT 
                rt.name AS receivableType,
                COUNT(*) AS transactionCount,
                SUM(r.face_value) AS totalFaceValue,
                SUM(t.final_value) AS totalFinalValue,
                AVG(rt.spread) AS avgSpread
            FROM transactions t
            INNER JOIN receivables r ON t.receivable_id = r.id
            INNER JOIN receivable_types rt ON r.type_id = rt.id
            WHERE t.created_at >= :startDate 
              AND t.created_at < :endDate
            GROUP BY rt.name
            ORDER BY SUM(t.final_value) DESC
            """,
            nativeQuery = true)
    List<Map<String, Object>> getAnalyticsByReceivableType(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}


