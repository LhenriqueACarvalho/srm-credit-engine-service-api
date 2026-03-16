package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Receivable;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ReceivableType;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableTypeRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import com.srm.credit.engine.SRM_Credit_Engine.utils.ContextoRastreamento;
import com.srm.credit.engine.SRM_Credit_Engine.utils.LoggerObservabilidade;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class TransactionService {

    private final PricingService pricingService;
    private final ExchangeRateService exchangeService;
    private final ReceivableRepository receivableRepo;
    private final TransactionRepository transactionRepo;
    private final ReceivableTypeRepository receivableTypeRepo;
    private final CurrencyRepository currencyRepo;
    private final LoggerObservabilidade loggerObservabilidade;

    public TransactionService(
            PricingService pricingService,
            ExchangeRateService exchangeService,
            ReceivableRepository receivableRepo,
            TransactionRepository transactionRepo,
            ReceivableTypeRepository receivableTypeRepo,
            CurrencyRepository currencyRepo,
            LoggerObservabilidade loggerObservabilidade) {

        this.pricingService = pricingService;
        this.exchangeService = exchangeService;
        this.receivableRepo = receivableRepo;
        this.transactionRepo = transactionRepo;
        this.receivableTypeRepo = receivableTypeRepo;
        this.currencyRepo = currencyRepo;
        this.loggerObservabilidade = loggerObservabilidade;
    }

    @Transactional
    public Transaction execute(CreateReceivableRequest req) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        
        try {
            log.info("[{}] Iniciando execução de nova transação de recebível", idRequisicao);
            ContextoRastreamento.definirOperacao("CriarTransacao");
            
            // Registrar parâmetros de entrada
            Map<String, Object> parametrosEntrada = new HashMap<>();
            parametrosEntrada.put("valorFace", req.getFaceValue());
            parametrosEntrada.put("diasVencimento", req.getDaysToMaturity());
            parametrosEntrada.put("tipoRecebivel", req.getReceivableType());
            parametrosEntrada.put("moedaOrigem", req.getCurrency());
            parametrosEntrada.put("moedaDestino", req.getPaymentCurrency());
            loggerObservabilidade.inicioOperacao("CriarTransacao", parametrosEntrada);

            log.debug("[{}] Calculando valor presente para recebível", idRequisicao);
            BigDecimal pv = pricingService.calculate(
                    req.getFaceValue(),
                    req.getDaysToMaturity(),
                    req.getReceivableType()
            );
            log.debug("[{}] Valor presente calculado: {}", idRequisicao, pv);

            log.debug("[{}] Obtendo taxa de câmbio: {} -> {}", idRequisicao, req.getCurrency(), req.getPaymentCurrency());
            BigDecimal rate = exchangeService.getRate(
                    req.getCurrency(),
                    req.getPaymentCurrency()
            );
            log.debug("[{}] Taxa de câmbio obtida: {}", idRequisicao, rate);

            BigDecimal finalValue = pv.multiply(rate);
            log.debug("[{}] Valor final calculado: {}", idRequisicao, finalValue);

            // Buscar tipo de recebível
            log.debug("[{}] Buscando tipo de recebível: {}", idRequisicao, req.getReceivableType());
            ReceivableType receivableType = receivableTypeRepo.findByName(req.getReceivableType().toUpperCase())
                    .orElseThrow(() -> {
                        String erro = "Tipo de recebível não encontrado: " + req.getReceivableType();
                        log.error("[{}] {}", idRequisicao, erro);
                        return new IllegalArgumentException(erro);
                    });
            log.debug("[{}] Tipo de recebível encontrado: {}", idRequisicao, receivableType.getName());

            // Buscar moeda
            log.debug("[{}] Buscando moeda: {}", idRequisicao, req.getCurrency());
            Currency currency = currencyRepo.findByCode(req.getCurrency().toUpperCase())
                    .orElseThrow(() -> {
                        String erro = "Moeda não encontrada: " + req.getCurrency();
                        log.error("[{}] {}", idRequisicao, erro);
                        return new IllegalArgumentException(erro);
                    });
            log.debug("[{}] Moeda encontrada: {}", idRequisicao, currency.getCode());

            // Criar e preencher receivable
            UUID idRecebivel = UUID.randomUUID();
            Receivable receivable = new Receivable();
            receivable.setId(idRecebivel);
            receivable.setType(receivableType);
            receivable.setFaceValue(req.getFaceValue());
            receivable.setCurrency(currency);
            receivable.setMaturityDate(LocalDate.now().plusDays(req.getDaysToMaturity()));
            
            log.info("[{}] Salvando recebível com ID: {}", idRequisicao, idRecebivel);
            receivable = receivableRepo.save(receivable);
            loggerObservabilidade.registrarAcessoBD("INSERT", "Receivable", 1);

            // Criar transaction
            UUID idTransacao = UUID.randomUUID();
            Transaction t = new Transaction();
            t.setId(idTransacao);
            t.setReceivable(receivable);
            t.setPresentValue(pv);
            t.setExchangeRate(rate);
            t.setFinalValue(finalValue);
            t.setCreatedAt(LocalDateTime.now());
            
            log.info("[{}] Salvando transação com ID: {}", idRequisicao, idTransacao);
            t = transactionRepo.save(t);
            loggerObservabilidade.registrarAcessoBD("INSERT", "Transaction", 1);
            
            // Registrar sucesso
            loggerObservabilidade.registrarCriacaoTransacao(
                    idTransacao.toString(),
                    finalValue,
                    req.getPaymentCurrency(),
                    req.getReceivableType()
            );
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("idTransacao", idTransacao);
            resultado.put("valorFinal", finalValue);
            resultado.put("moeda", req.getPaymentCurrency());
            loggerObservabilidade.fimOperacao("CriarTransacao", resultado);
            
            return t;
            
        } catch (Exception e) {
            Map<String, Object> contextoErro = new HashMap<>();
            contextoErro.put("valorFace", req.getFaceValue());
            contextoErro.put("tipoRecebivel", req.getReceivableType());
            contextoErro.put("moedaOrigem", req.getCurrency());
            loggerObservabilidade.erroOperacao("CriarTransacao", e, contextoErro);
            throw e;
        }
    }
}

