package br.com.cardapiododia.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CardapioResponse {
    private LocalDate data;

    private String tipo;

    private PratoCardapioResponse cardapio;
}
