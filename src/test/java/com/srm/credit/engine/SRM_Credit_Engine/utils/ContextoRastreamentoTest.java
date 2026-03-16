package com.srm.credit.engine.SRM_Credit_Engine.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe ContextoRastreamento
 */
@DisplayName("Testes de Contexto de Rastreamento")
class ContextoRastreamentoTest {

    @BeforeEach
    void setUp() {
        ContextoRastreamento.limpar();
    }

    @Test
    @DisplayName("Deve gerar ID de requisição automaticamente")
    void testeGerarIdRequisicao() {
        String id1 = ContextoRastreamento.obterIdRequisicao();
        String id2 = ContextoRastreamento.obterIdRequisicao();
        
        assertNotNull(id1);
        assertNotNull(id2);
        assertEquals(id1, id2, "ID deve ser mantido dentro da mesma thread");
    }

    @Test
    @DisplayName("Deve definir e recuperar ID de requisição customizado")
    void testeDefinirIdRequisicao() {
        String idCustomizado = "req-2024-01-15-001";
        ContextoRastreamento.definirIdRequisicao(idCustomizado);
        
        assertEquals(idCustomizado, ContextoRastreamento.obterIdRequisicao());
    }

    @Test
    @DisplayName("Deve gerenciar ID de usuário")
    void testeIdUsuario() {
        assertNull(ContextoRastreamento.obterIdUsuario());
        
        ContextoRastreamento.definirIdUsuario("user-123");
        assertEquals("user-123", ContextoRastreamento.obterIdUsuario());
    }

    @Test
    @DisplayName("Deve definir e recuperar operação")
    void testeOperacao() {
        assertNull(ContextoRastreamento.obterOperacao());
        
        ContextoRastreamento.definirOperacao("CriarTransacao");
        assertEquals("CriarTransacao", ContextoRastreamento.obterOperacao());
    }

    @Test
    @DisplayName("Deve calcular tempo decorrido")
    void testeTempoDecorrido() throws InterruptedException {
        ContextoRastreamento.definirTempoInicio(System.currentTimeMillis());
        Thread.sleep(100);
        
        Long tempoDecorrido = ContextoRastreamento.calcularTempoDecorrido();
        assertNotNull(tempoDecorrido);
        assertTrue(tempoDecorrido >= 100, "Tempo decorrido deve ser >= 100ms");
    }

    @Test
    @DisplayName("Deve limpar todo o contexto")
    void testeLimpar() {
        ContextoRastreamento.definirIdRequisicao("req-123");
        ContextoRastreamento.definirIdUsuario("user-456");
        ContextoRastreamento.definirOperacao("Teste");
        
        ContextoRastreamento.limpar();
        
        // Após limpar, deve gerar novos IDs
        String novoId = ContextoRastreamento.obterIdRequisicao();
        assertNotEquals("req-123", novoId, "ID deve ser regenerado após limpeza");
        assertNull(ContextoRastreamento.obterIdUsuario());
        assertNull(ContextoRastreamento.obterOperacao());
    }

    @Test
    @DisplayName("Deve manter isolamento entre threads")
    void testeIsolamentoThreads() throws InterruptedException {
        String idPrincipal = ContextoRastreamento.obterIdRequisicao();
        String[] idSecundaria = new String[1];
        
        Thread thread2 = new Thread(() -> {
            idSecundaria[0] = ContextoRastreamento.obterIdRequisicao();
        });
        
        thread2.start();
        thread2.join();
        
        assertNotEquals(idPrincipal, idSecundaria[0], 
            "Threads diferentes devem ter IDs diferentes");
    }
}

