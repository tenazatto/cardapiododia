package br.com.cardapiododia.model.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@Setter
@Document("pratos_cardapio")
public class Prato {
    @Id
    private String id;

    private String tipo;

    private String prato;

    private String nome;

    @Field("media_notas")
    private BigDecimal mediaNotas;
}
