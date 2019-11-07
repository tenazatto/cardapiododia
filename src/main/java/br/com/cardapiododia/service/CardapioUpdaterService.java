package br.com.cardapiododia.service;

import br.com.cardapiododia.model.entity.Cardapio;
import br.com.cardapiododia.model.request.CardapioDatesRequest;
import br.com.cardapiododia.model.response.CardapioUpdateResponse;
import br.com.cardapiododia.model.response.UpdateResponse;
import br.com.cardapiododia.repository.CardapioRepository;
import br.com.cardapiododia.repository.TipoRefeicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardapioUpdaterService {
    @Autowired
    CardapioRepository cardapioRepository;

    @Autowired
    TipoRefeicaoRepository tipoRefeicaoRepository;

    @Autowired
    CardapioScraperService cardapioScraperService;

    public CardapioUpdateResponse update(CardapioDatesRequest cardapioDatesRequest) {
        CardapioUpdateResponse updateResponse = new CardapioUpdateResponse();
        List<UpdateResponse> updates = new ArrayList<>();

        List<Cardapio> cardapios = cardapioScraperService.findCardapios();

        cardapioDatesRequest.getDates().forEach(d -> {
            List<Cardapio> cardapiosData = cardapios.stream()
                    .filter(cardapio -> d.equals(cardapio.getData()))
                    .collect(Collectors.toList());

            if (cardapiosData.size() == 0) {
                UpdateResponse update = new UpdateResponse();

                update.setDate(d);
                update.setStatus(HttpStatus.NOT_FOUND.value());
                update.setDescription(HttpStatus.NOT_FOUND.getReasonPhrase());

                updates.add(update);
            } else {
                cardapiosData.forEach(cardapio -> {
                    UpdateResponse update = new UpdateResponse();

                    update.setDate(d);
                    update.setTipo(tipoRefeicaoRepository.findByTipo(cardapio.getTipo()).getRefeicao());
                    try {
                        Cardapio cardapioCheck = cardapioRepository.findByDataAndTipo(cardapio.getData(), cardapio.getTipo());

                        if (cardapioCheck != null) {
                            update.setStatus(HttpStatus.CONFLICT.value());
                            update.setDescription(HttpStatus.CONFLICT.getReasonPhrase());
                        } else {
                            cardapioRepository.insert(cardapio);
                            update.setStatus(HttpStatus.OK.value());
                            update.setDescription(HttpStatus.OK.getReasonPhrase());
                        }
                    } catch (Exception e) {
                        update.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        update.setDescription(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                    }

                    updates.add(update);
                });
            }


        });

        updateResponse.setResponses(updates);

        return updateResponse;
    }
}
