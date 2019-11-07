package br.com.cardapiododia.repository;

import br.com.cardapiododia.model.entity.TipoRefeicao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRefeicaoRepository extends MongoRepository<TipoRefeicao, String> {
    TipoRefeicao findByTipo(String tipo);
}
