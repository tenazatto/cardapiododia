package br.com.cardapiododia.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardapioUpdateResponse {
    List<UpdateResponse> responses;
}
