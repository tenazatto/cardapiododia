package br.com.cardapiododia.repository;

import br.com.cardapiododia.model.entity.Cardapio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CardapioRepository extends MongoRepository<Cardapio, String> {
    Cardapio findByDataAndTipo(LocalDate data, String tipo);

    List<Cardapio> findByData(LocalDate data);
}
