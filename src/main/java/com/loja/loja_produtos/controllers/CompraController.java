package com.loja.loja_produtos.controllers;

import com.loja.loja_produtos.Dtos.CompraDTO;
import com.loja.loja_produtos.models.Cliente;
import com.loja.loja_produtos.models.Compra;
import com.loja.loja_produtos.models.Produto;
import com.loja.loja_produtos.models.Vendedor;
import com.loja.loja_produtos.repositories.ClienteRepository;
import com.loja.loja_produtos.repositories.CompraRepository;
import com.loja.loja_produtos.repositories.ProdutoRepository;
import com.loja.loja_produtos.repositories.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/compra")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // Receber pedido de compra
    @PostMapping
    public ResponseEntity<CompraDTO> criarPedidoDeCompra(@RequestBody CompraDTO compraDTO) {
        // Verificar se o cliente existe
        Cliente cliente = clienteRepository.findById(compraDTO.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Client not found"));

        // Verificar se o vendedor existe
        Vendedor vendedor = vendedorRepository.findById(compraDTO.getVendedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));

        // Verificar se os produtos existem e reduzir o estoque
        List<Produto> produtos = produtoRepository.findAllById(compraDTO.getProdutosIds());
        if (produtos.size() != compraDTO.getProdutosIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"One or more products were not found");
        }

        produtos.forEach(produto -> {
            if (produto.getEstoque() <= 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There is not enough storage for this product: " + produto.getName());
            }
            produto.setEstoque(produto.getEstoque() - 1); // Reduz o estoque
        });

        produtoRepository.saveAllAndFlush(produtos); // Salva o estoque atualizado

        // Criar a entidade Compra
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setVendedor(vendedor);
        compra.setProdutos(produtos);
        compra.setDataCompra(LocalDateTime.now());

        // Salvar no banco de dados
        Compra novaCompra = compraRepository.saveAndFlush(compra);

        // Converter para DTO antes de retornar
        CompraDTO novaCompraDTO = new CompraDTO(
                novaCompra.getId(),
                novaCompra.getCliente().getId(),
                novaCompra.getVendedor().getId(),
                novaCompra.getProdutos().stream().map(Produto::getId).toList()
        );

        return ResponseEntity.ok(novaCompraDTO);
    }

    // Cancelar pedido de compra
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedidoDeCompra(@PathVariable Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found"));

        // Restaurar o estoque dos produtos
        List<Produto> produtos = compra.getProdutos();
        produtos.forEach(produto -> produto.setEstoque(produto.getEstoque() + 1)); // Restaura o estoque
        produtoRepository.saveAllAndFlush(produtos); // Salva o estoque atualizado

        // Deletar a compra
        compraRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

