package br.com.cardapiododia.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PratoCardapio {
    private List<String> prato;

    private List<String> pratoPrincipal;

    private List<String> salada;

    private List<String> sobremesa;

    private List<String> suco;

    private List<String> observacoes;
}
