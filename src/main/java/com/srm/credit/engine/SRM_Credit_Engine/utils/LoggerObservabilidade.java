package com.srm.credit.engine.SRM_Credit_Engine.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitária para simplificar logs com padrão de observabilidade.
 * Todos os logs incluem o contexto de rastreamento automaticamente.
 */
@Slf4j
@Component
public class LoggerObservabilidade {
    
    /**
     * Registra o início de uma operação
     */
    public void inicioOperacao(String nomeOperacao, Map<String, Object> parametros) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        log.info("[{}] Iniciando operação: {} - Parâmetros: {}", 
                idRequisicao, nomeOperacao, parametros);
    }
    
    /**
     * Registra o término bem-sucedido de uma operação
     */
    public void fimOperacao(String nomeOperacao, Object resultado) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        Long tempoDecorrido = ContextoRastreamento.calcularTempoDecorrido();
        log.info("[{}] Operação concluída com sucesso: {} - Tempo: {}ms - Resultado: {}", 
                idRequisicao, nomeOperacao, tempoDecorrido, resultado);
    }
    
    /**
     * Registra um erro durante a operação
     */
    public void erroOperacao(String nomeOperacao, Exception e, Map<String, Object> contexto) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        log.error("[{}] Erro ao executar operação: {} - Contexto: {}", 
                idRequisicao, nomeOperacao, contexto, e);
    }
    
    /**
     * Registra informação geral com contexto de rastreamento
     */
    public void info(String mensagem, Object... argumentos) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        String mensagemFormatada = String.format("[%s] %s", idRequisicao, mensagem);
        log.info(mensagemFormatada, argumentos);
    }
    
    /**
     * Registra aviso com contexto de rastreamento
     */
    public void aviso(String mensagem, Object... argumentos) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        String mensagemFormatada = String.format("[%s] %s", idRequisicao, mensagem);
        log.warn(mensagemFormatada, argumentos);
    }
    
    /**
     * Registra erro com contexto de rastreamento
     */
    public void erro(String mensagem, Throwable throwable, Object... argumentos) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        String mensagemFormatada = String.format("[%s] %s", idRequisicao, mensagem);
        log.error(mensagemFormatada, argumentos, throwable);
    }
    
    /**
     * Registra debug com contexto de rastreamento
     */
    public void debug(String mensagem, Object... argumentos) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        String mensagemFormatada = String.format("[%s] %s", idRequisicao, mensagem);
        log.debug(mensagemFormatada, argumentos);
    }
    
    /**
     * Registra cálculo de preço com detalhes
     */
    public void registrarCalculoPreco(BigDecimal valorFace, int diasVencimento, 
                                      String tipoRecebivel, BigDecimal valorCalculado) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        Map<String, Object> detalhes = new HashMap<>();
        detalhes.put("valorFace", valorFace);
        detalhes.put("diasVencimento", diasVencimento);
        detalhes.put("tipoRecebivel", tipoRecebivel);
        detalhes.put("resultado", valorCalculado);
        
        log.info("[{}] Cálculo de preço executado: {}", idRequisicao, detalhes);
    }
    
    /**
     * Registra operação de câmbio com detalhes
     */
    public void registrarOperacaoCambio(String moedaOrigem, String moedaDestino, 
                                        BigDecimal taxa) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        log.info("[{}] Taxa de câmbio obtida: {} -> {} = {}", 
                idRequisicao, moedaOrigem, moedaDestino, taxa);
    }
    
    /**
     * Registra criação de transação
     */
    public void registrarCriacaoTransacao(String idTransacao, BigDecimal valor, 
                                         String moeda, String tipoRecebivel) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        Map<String, Object> detalhes = new HashMap<>();
        detalhes.put("idTransacao", idTransacao);
        detalhes.put("valor", valor);
        detalhes.put("moeda", moeda);
        detalhes.put("tipoRecebivel", tipoRecebivel);
        
        log.info("[{}] Nova transação criada: {}", idRequisicao, detalhes);
    }
    
    /**
     * Registra acesso ao banco de dados
     */
    public void registrarAcessoBD(String operacao, String entidade, int registrosAfetados) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        log.debug("[{}] Acesso BD - Operação: {}, Entidade: {}, Registros: {}", 
                idRequisicao, operacao, entidade, registrosAfetados);
    }
}

