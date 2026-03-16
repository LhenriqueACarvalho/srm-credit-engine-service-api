package com.srm.credit.engine.SRM_Credit_Engine.utils;

import java.util.UUID;

/**
 * Classe responsável por gerenciar o contexto de rastreamento distribuído.
 * Utiliza ThreadLocal para armazenar o identificador único de cada requisição.
 */
public class ContextoRastreamento {
    
    private static final ThreadLocal<String> idRequisicao = ThreadLocal.withInitial(
            () -> UUID.randomUUID().toString()
    );
    
    private static final ThreadLocal<String> idUsuario = new ThreadLocal<>();
    private static final ThreadLocal<String> operacao = new ThreadLocal<>();
    private static final ThreadLocal<Long> tempoInicio = new ThreadLocal<>();
    
    /**
     * Obtém o ID único da requisição atual
     */
    public static String obterIdRequisicao() {
        return idRequisicao.get();
    }
    
    /**
     * Define um novo ID de requisição
     */
    public static void definirIdRequisicao(String id) {
        idRequisicao.set(id);
    }
    
    /**
     * Obtém o ID do usuário associado à requisição
     */
    public static String obterIdUsuario() {
        return idUsuario.get();
    }
    
    /**
     * Define o ID do usuário
     */
    public static void definirIdUsuario(String id) {
        idUsuario.set(id);
    }
    
    /**
     * Obtém a operação sendo executada
     */
    public static String obterOperacao() {
        return operacao.get();
    }
    
    /**
     * Define a operação
     */
    public static void definirOperacao(String nomeOperacao) {
        operacao.set(nomeOperacao);
    }
    
    /**
     * Obtém o tempo de início da operação em milissegundos
     */
    public static Long obterTempoInicio() {
        return tempoInicio.get();
    }
    
    /**
     * Define o tempo de início da operação
     */
    public static void definirTempoInicio(Long tempo) {
        tempoInicio.set(tempo);
    }
    
    /**
     * Calcula o tempo decorrido desde o início da operação
     */
    public static Long calcularTempoDecorrido() {
        Long inicio = tempoInicio.get();
        if (inicio == null) {
            return null;
        }
        return System.currentTimeMillis() - inicio;
    }
    
    /**
     * Limpa todo o contexto de rastreamento
     */
    public static void limpar() {
        idRequisicao.remove();
        idUsuario.remove();
        operacao.remove();
        tempoInicio.remove();
    }
}

