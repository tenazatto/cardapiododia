package br.com.cardapiododia.repository;

import br.com.cardapiododia.model.entity.TipoPrato;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPratoRepository extends MongoRepository<TipoPrato, String> {
    TipoPrato findByTipo(String tipo);
}
