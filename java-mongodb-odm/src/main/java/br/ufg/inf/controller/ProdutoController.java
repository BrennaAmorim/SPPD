package br.ufg.inf.controller;

import br.ufg.inf.model.Produto;
import br.ufg.inf.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // --- OPERAÇÕES CRUD COMPLETAS ---

    // POST: Criar um novo produto
    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoRepository.save(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED); // Retorna 201 Created
    }

    // GET: Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodosProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return new ResponseEntity<>(produtos, HttpStatus.OK); // Retorna 200 OK
    }

    // GET: Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable String id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Retorna 200 OK se encontrado
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retorna 404 Not Found se não encontrado
    }

    // PUT: Atualizar um produto existente - ID NA URL (Formato Padrão RESTful)
    @PutMapping("/{id}") // <<<<< ID AGORA ESTÁ NA URL
    public ResponseEntity<Produto> atualizarProduto(@PathVariable String id, @RequestBody Produto produtoAtualizado) {
        if (!produtoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 se o produto não existe
        }
        produtoAtualizado.setId(id); // Garante que o ID do produto a ser atualizado seja o da URL
        Produto produtoSalvo = produtoRepository.save(produtoAtualizado);
        return new ResponseEntity<>(produtoSalvo, HttpStatus.OK); // Retorna 200 OK
    }

    // DELETE: Excluir um produto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletarProduto(@PathVariable String id) {
        if (!produtoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 se o produto não existe
        }
        produtoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 No Content
    }
}