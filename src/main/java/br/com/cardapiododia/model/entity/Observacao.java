package br.com.cardapiododia.model.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("observacoes")
public class Observacao {
    @Id
    private String id;

    private String hash;

    private String observacao;
}
