package br.com.cardapiododia.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateResponse {
    private LocalDate date;
    private String tipo;
    private Integer status;
    private String description;
}
