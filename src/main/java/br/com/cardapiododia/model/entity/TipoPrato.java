package br.com.cardapiododia.model.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document("tipos_prato")
public class TipoPrato {
    @Id
    private String id;

    private String tipo;

    private String prato;
}
