package br.com.cardapiododia.repository;

import br.com.cardapiododia.model.entity.Observacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservacaoRepository extends MongoRepository<Observacao, String> {
    Observacao findByHash(String obs);
}
