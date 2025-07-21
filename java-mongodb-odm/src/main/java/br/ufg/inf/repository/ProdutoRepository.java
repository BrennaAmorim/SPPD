package br.ufg.inf.repository; // PACOTE CORRIGIDO AQUI!

import br.ufg.inf.model.Produto; // Importa a classe Produto do pacote correto
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository // Marca a interface como um componente de repositório Spring
public interface ProdutoRepository extends MongoRepository<Produto, String> {
    // O Spring Data MongoDB vai gerar automaticamente as implementações para métodos
    // como save(produto), findById(id), findAll, deleteById, etc.
    // Você não precisa escrevê-los aqui!
}