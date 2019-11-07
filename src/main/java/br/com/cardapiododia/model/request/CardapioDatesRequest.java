package br.com.cardapiododia.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CardapioDatesRequest {
    @NotNull
    @JsonDeserialize(contentUsing = LocalDateDeserializer.class)
    @JsonSerialize(contentUsing = LocalDateSerializer.class)
    private List<LocalDate> dates;
}
