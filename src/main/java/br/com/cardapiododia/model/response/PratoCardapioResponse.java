package br.com.cardapiododia.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PratoCardapioResponse {
    private List<String> prato;

    private List<String> pratoPrincipal;

    private List<String> salada;

    private List<String> sobremesa;

    private List<String> suco;

    private List<String> observacoes;
}
