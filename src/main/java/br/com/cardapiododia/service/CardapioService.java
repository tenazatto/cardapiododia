package br.com.cardapiododia.service;

import br.com.cardapiododia.model.entity.*;
import br.com.cardapiododia.model.response.CardapioResponse;
import br.com.cardapiododia.model.response.PratoCardapioResponse;
import br.com.cardapiododia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.cardapiododia.utils.CardapioUtils.hashString;

@Service
public class CardapioService {

    @Autowired
    CardapioRepository cardapioRepository;

    @Autowired
    TipoRefeicaoRepository tipoRefeicaoRepository;

    @Autowired
    TipoPratoRepository tipoPratoRepository;

    @Autowired
    PratoRepository pratoRepository;

    @Autowired
    ObservacaoRepository observacaoRepository;

    public List<CardapioResponse> getCardapios(LocalDate data, String refeicao) {
        List<Cardapio> cardapios = cardapioRepository.findByData(data);

        return cardapios.stream()
                .filter(cardapio -> (refeicao != null) ? hashString(refeicao).equals(cardapio.getTipo()) : true)
                .map(cardapio -> mapResponse(cardapio))
                .collect(Collectors.toList());
    }

    private CardapioResponse mapResponse(Cardapio cardapio) {
        CardapioResponse cardapioResponse = new CardapioResponse();

        cardapioResponse.setData(cardapio.getData());
        TipoRefeicao tipoRefeicao = tipoRefeicaoRepository.findByTipo(cardapio.getTipo());
        cardapioResponse.setTipo(tipoRefeicao.getRefeicao());

        PratoCardapioResponse pratoCardapioResponse = new PratoCardapioResponse();

        if(cardapio.getCardapio().getPrato() != null) {
            pratoCardapioResponse.setPrato(cardapio.getCardapio().getPrato().stream()
                .map(prato -> {
                    Prato pratoObj = pratoRepository.findByTipoAndPrato(hashString("Prato Geral"), prato);
                    return pratoObj.getNome();
                })
                .collect(Collectors.toList()));
        }
        if(cardapio.getCardapio().getPratoPrincipal() != null) {
            pratoCardapioResponse.setPratoPrincipal(cardapio.getCardapio().getPratoPrincipal().stream()
                    .map(prato -> {
                        Prato pratoObj = pratoRepository.findByTipoAndPrato(hashString("Prato principal"), prato);
                        return pratoObj.getNome();
                    })
                    .collect(Collectors.toList()));
        }
        if(cardapio.getCardapio().getSalada() != null) {
            pratoCardapioResponse.setSalada(cardapio.getCardapio().getSalada().stream()
                    .map(prato -> {
                        Prato pratoObj = pratoRepository.findByTipoAndPrato(hashString("Salada"), prato);
                        return pratoObj.getNome();
                    })
                    .collect(Collectors.toList()));
        }
        if(cardapio.getCardapio().getSobremesa() != null) {
            pratoCardapioResponse.setSobremesa(cardapio.getCardapio().getSobremesa().stream()
                    .map(prato -> {
                        Prato pratoObj = pratoRepository.findByTipoAndPrato(hashString("Sobremesa"), prato);
                        return pratoObj.getNome();
                    })
                    .collect(Collectors.toList()));
        }
        if(cardapio.getCardapio().getSuco() != null) {
            pratoCardapioResponse.setSuco(cardapio.getCardapio().getSuco().stream()
                    .map(prato -> {
                        Prato pratoObj = pratoRepository.findByTipoAndPrato(hashString("Suco"), prato);
                        return pratoObj.getNome();
                    })
                    .collect(Collectors.toList()));
        }
        if(cardapio.getCardapio().getObservacoes() != null) {
            pratoCardapioResponse.setObservacoes(cardapio.getCardapio().getObservacoes().stream()
                    .map(observacao -> {
                        Observacao observacaoObj = observacaoRepository.findByHash(observacao);
                        return observacaoObj.getObservacao();
                    })
                    .collect(Collectors.toList()));
        }

        cardapioResponse.setCardapio(pratoCardapioResponse);

        return cardapioResponse;
    }
}
