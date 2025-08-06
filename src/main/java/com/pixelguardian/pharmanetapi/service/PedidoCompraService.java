package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.ItemPedido;
import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import com.pixelguardian.pharmanetapi.model.repository.ItemPedidoRepository;
import com.pixelguardian.pharmanetapi.model.repository.PedidoCompraRepository;
import com.pixelguardian.pharmanetapi.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.pixelguardian.pharmanetapi.util.DateFormatter;

@Service
@RequiredArgsConstructor
public class PedidoCompraService {

    private final PedidoCompraRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;

    public List<PedidoCompra> getPedidoCompras() {
        return repository.findAll();
    }

    public Optional<PedidoCompra> getPedidoCompraById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public PedidoCompra salvar(PedidoCompra pedidoCompra) {
        validar(pedidoCompra);
        // Recalcular valor total para novos pedidos também
        // Isso garante que mesmo um pedido sem itens seja salvo com valorTotal = 0.0f
        return repository.save(pedidoCompra);
    }

    @Transactional
    public PedidoCompra atualizar(PedidoCompra pedidoCompra) {
        Objects.requireNonNull(pedidoCompra.getId());
        validar(pedidoCompra);

        Optional<PedidoCompra> existingPedidoOptional = repository.findById(pedidoCompra.getId());
        if (existingPedidoOptional.isEmpty()) {
            throw new RegraNegocioException("Pedido de compra não encontrado para atualização.");
        }
        PedidoCompra existingPedido = existingPedidoOptional.get();

        existingPedido.setDataCriacao(pedidoCompra.getDataCriacao());
        existingPedido.setCodigo(pedidoCompra.getCodigo());
        existingPedido.setStatus(pedidoCompra.getStatus());
        existingPedido.setTipoEntrega(pedidoCompra.getTipoEntrega());
        existingPedido.setDataEntrega(pedidoCompra.getDataEntrega());
        existingPedido.setEndereco(pedidoCompra.getEndereco());
        existingPedido.setStatusEntrega(pedidoCompra.getStatusEntrega());

        return repository.save(existingPedido);
    }

    @Transactional
    public void excluir(Long id) {
        Optional<PedidoCompra> pedidoCompraOptional = repository.findById(id);
        if (pedidoCompraOptional.isEmpty()) {
            throw new RegraNegocioException("Pedido de compra não encontrado para exclusão.");
        }
        PedidoCompra pedidoCompra = pedidoCompraOptional.get();

        List<ItemPedido> itensAssociados = itemPedidoRepository.findByPedidoCompra(pedidoCompra);
        if (itensAssociados != null && !itensAssociados.isEmpty()) {
            itemPedidoRepository.deleteAll(itensAssociados);
        }

        repository.delete(pedidoCompra);
    }

    public float calcularValorTotal(PedidoCompra pedidoCompra) {
        Float total = 0.0f;
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoCompra(pedidoCompra);

        if (itens != null && !itens.isEmpty()) {
            for (ItemPedido item : itens) {
                if (item.getQuantidade() != null && item.getPrecoUnitario() != null) {
                    total += item.getQuantidade() * item.getPrecoUnitario();
                }
            }
        }
        return total;
    }

    public String gerarCodigo(){
        String data, codigo;

        data = DateFormatter.formatarData(LocalDate.now());

        RandomNumberGenerator rng = RandomNumberGenerator.getRng();

        do {
            codigo = data + rng.gerarAlphaNumericoSeisDigitos();
            // Só por precaução vamos checar no banco se já existe e gerar de novo até vir um único
        } while(repository.existsByCodigo(codigo));

        return codigo;
    }

    public boolean existsPedidoCompraByCodigo(String codigo){

        return repository.existsByCodigo(codigo);

    }

    public void validar(PedidoCompra pedidoCompra) {
        if (pedidoCompra.getDataCriacao() == null || pedidoCompra.getDataCriacao().trim().isEmpty()) {
            throw new RegraNegocioException("Data de Criação inválida");
        }
        if (pedidoCompra.getCodigo() == null || pedidoCompra.getCodigo().trim().isEmpty()) {
            throw new RegraNegocioException("Código inválido");
        }
        if (pedidoCompra.getStatus() == null || pedidoCompra.getStatus().trim().isEmpty()) {
            throw new RegraNegocioException("Status do Pedido inválido");
        }
        if (pedidoCompra.getTipoEntrega() == null || pedidoCompra.getTipoEntrega().trim().isEmpty()) {
            throw new RegraNegocioException("Tipo de Entrega inválida");
        }
        if (pedidoCompra.getStatusEntrega() == null || pedidoCompra.getStatusEntrega().trim().isEmpty()) {
            throw new RegraNegocioException("Status de Entrega inválido");
        }
        if (pedidoCompra.getEndereco() == null || pedidoCompra.getEndereco().getId() == null) {
            throw new RegraNegocioException("Endereço de entrega inválido");
        }
    }
}