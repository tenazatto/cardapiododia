package br.com.cardapiododia.model.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document("cardapio")
public class Cardapio {
    @Id
    private String id;

    private LocalDate data;

    private String tipo;

    private PratoCardapio cardapio;
}
