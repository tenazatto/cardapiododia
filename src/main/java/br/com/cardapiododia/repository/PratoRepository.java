package br.com.cardapiododia.repository;

import br.com.cardapiododia.model.entity.Prato;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PratoRepository extends MongoRepository<Prato, String> {
    Prato findByTipoAndPrato(String tipo, String prato);
}
