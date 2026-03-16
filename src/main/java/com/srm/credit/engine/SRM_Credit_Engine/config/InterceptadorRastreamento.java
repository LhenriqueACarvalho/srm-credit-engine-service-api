package com.srm.credit.engine.SRM_Credit_Engine.config;

import com.srm.credit.engine.SRM_Credit_Engine.utils.ContextoRastreamento;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Interceptador que adiciona suporte a rastreamento distribuído.
 * Captura ou gera IDs de requisição para correlacionar logs entre serviços.
 */
@Slf4j
@Component
public class InterceptadorRastreamento implements HandlerInterceptor {
    
    private static final String CABECALHO_ID_REQUISICAO = "X-Request-ID";
    private static final String CABECALHO_ID_USUARIO = "X-User-ID";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Obter ou gerar ID de requisição
        String idRequisicao = request.getHeader(CABECALHO_ID_REQUISICAO);
        if (idRequisicao == null || idRequisicao.isEmpty()) {
            idRequisicao = UUID.randomUUID().toString();
        }
        
        // Obter ID do usuário se disponível
        String idUsuario = request.getHeader(CABECALHO_ID_USUARIO);
        
        // Definir contexto de rastreamento
        ContextoRastreamento.definirIdRequisicao(idRequisicao);
        if (idUsuario != null && !idUsuario.isEmpty()) {
            ContextoRastreamento.definirIdUsuario(idUsuario);
        }
        ContextoRastreamento.definirOperacao(request.getMethod() + " " + request.getRequestURI());
        ContextoRastreamento.definirTempoInicio(System.currentTimeMillis());
        
        // Adicionar ID da requisição no header da resposta
        response.addHeader(CABECALHO_ID_REQUISICAO, idRequisicao);
        
        log.info("Iniciando requisição: {} - URI: {}", idRequisicao, request.getRequestURI());
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                 Object handler, Exception ex) {
        Long tempoDecorrido = ContextoRastreamento.calcularTempoDecorrido();
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        
        if (ex != null) {
            log.error("Erro ao processar requisição: {} - Tempo: {}ms - Status: {} - Erro: {}", 
                    idRequisicao, tempoDecorrido, response.getStatus(), ex.getMessage(), ex);
        } else {
            log.info("Requisição finalizada com sucesso: {} - Tempo: {}ms - Status: {}", 
                    idRequisicao, tempoDecorrido, response.getStatus());
        }
        
        // Limpar contexto de rastreamento
        ContextoRastreamento.limpar();
    }
}

