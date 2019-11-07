package br.com.cardapiododia.controller;

import br.com.cardapiododia.model.request.CardapioDatesRequest;
import br.com.cardapiododia.model.response.CardapioResponse;
import br.com.cardapiododia.model.response.CardapioUpdateResponse;
import br.com.cardapiododia.service.CardapioService;
import br.com.cardapiododia.service.CardapioUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
public class CardapioDoDiaController {

    @Autowired
    CardapioUpdaterService cardapioUpdaterService;

    @Autowired
    CardapioService cardapioService;

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CardapioUpdateResponse update(@Valid @RequestBody CardapioDatesRequest cardapioDatesRequest) {
        return cardapioUpdaterService.update(cardapioDatesRequest);
    }

    @GetMapping("/cardapio")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<CardapioResponse> getCardapios(
            @Valid
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data,
            @RequestParam(required = false) String refeicao) {
        return cardapioService.getCardapios(data, refeicao);
    }

}
