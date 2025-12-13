package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.api.dto.CarrinhoDTO;
import com.pixelguardian.pharmanetapi.api.dto.ItemCarrinhoDTO;
import com.pixelguardian.pharmanetapi.api.dto.PagamentoDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.*;
import com.pixelguardian.pharmanetapi.model.repository.ItemPedidoRepository;
import com.pixelguardian.pharmanetapi.model.repository.PedidoCompraRepository;
import com.pixelguardian.pharmanetapi.model.repository.VendaRepository;
import com.pixelguardian.pharmanetapi.model.repository.PagamentoRepository;
import com.pixelguardian.pharmanetapi.util.RandomNumberGenerator;
import com.pixelguardian.pharmanetapi.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PedidoCompraService {

    private final PedidoCompraRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final VendaRepository vendaRepository;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final ReceitaService receitaService;

    public List<PedidoCompra> getPedidoCompras() {
        return repository.findAll();
    }

    public Optional<PedidoCompra> getPedidoCompraById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public PedidoCompra salvar(PedidoCompra pedidoCompra) {
        validar(pedidoCompra);
        return repository.save(pedidoCompra);
    }

    @Transactional
    public PedidoCompra criarPedido(CarrinhoDTO dto) {

        Usuario usuario = usuarioService.getUsuarioById(dto.getIdUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário do pedido não encontrado."));

        Endereco endereco = usuario.getEndereco();
        if (dto.getTipoEntrega().equalsIgnoreCase("delivery") && endereco == null) {
            throw new RegraNegocioException("Endereço é obrigatório para entrega tipo 'delivery'.");
        }

        PedidoCompra pedidoCompra = new PedidoCompra();
        pedidoCompra.setUsuario(usuario);
        pedidoCompra.setEndereco(endereco);
        pedidoCompra.setTipoEntrega(dto.getTipoEntrega());
        pedidoCompra.setCodigo(gerarCodigo());
        pedidoCompra.setDataCriacao(DateUtil.formatarHifenReverso(LocalDate.now()));
        pedidoCompra.setStatus("pagamento pendente");
        pedidoCompra.setStatusEntrega("pendente");

        pedidoCompra = repository.save(pedidoCompra);

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RegraNegocioException("O pedido deve conter pelo menos um item.");
        }

        for (ItemCarrinhoDTO itemDto : dto.getItens()) {
            Produto produto = produtoService.getProdutoById(itemDto.getIdProduto())
                    .orElseThrow(() -> new RegraNegocioException("Produto ID " + itemDto.getIdProduto() + " não encontrado."));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedidoCompra(pedidoCompra);
            itemPedido.setQuantidade(itemDto.getQuantidade());
            itemPedido.setPrecoUnitario(itemDto.getPrecoUnitario());
            itemPedido.setNomeProduto(produto.getNome());

            Optional<Estoque> estoqueOptional = estoqueService.getEstoqueByProdutoId(produto.getId())
                    .stream()
                    .findFirst();
            itemPedido.setEstoque(estoqueOptional.orElse(null));

            if (itemDto.getIdReceita() != null && itemDto.getIdReceita() != 0) {
                Receita receita = receitaService.getReceitaById(itemDto.getIdReceita())
                        .orElseThrow(() -> new RegraNegocioException("Receita ID " + itemDto.getIdReceita() + " não encontrada."));
                itemPedido.setReceita(receita);
            }

            itemPedidoRepository.save(itemPedido);
        }

        return pedidoCompra;
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
    public PedidoCompra confirmarPagamento(Long id, PagamentoDTO pagamentoDto) {
        Optional<PedidoCompra> pedidoOptional = repository.findById(id);
        if (pedidoOptional.isEmpty()) {
            throw new RegraNegocioException("Pedido de compra não encontrado.");
        }
        PedidoCompra pedidoCompra = pedidoOptional.get();
        if (pedidoCompra.getStatus().equals("pagamento pendente")) {

            Pagamento pagamento = new Pagamento();
            pagamento.setFormaPagamento(pagamentoDto.getFormaPagamento());
            pagamento.setValor(pagamentoDto.getValor());
            pagamento.setDataPagamento(DateUtil.formatarHifenReverso(LocalDate.now()));

            if (pagamento.getValor() == null) {
                float valorTotalCalculado = calcularValorTotal(pedidoCompra);
                pagamento.setValor(valorTotalCalculado);
            }

            Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

            Venda venda = new Venda();
            venda.setDataVenda(DateUtil.formatarHifenReverso(LocalDate.now()));
            venda.setPagamento(pagamentoSalvo);
            venda.setPedidoCompra(pedidoCompra);
            vendaRepository.save(venda);

            pedidoCompra.setStatus("entrega pendente");
            pedidoCompra.setStatusEntrega("pendente");
            return repository.save(pedidoCompra);
        } else {
            throw new RegraNegocioException("O pagamento deste pedido não pode ser confirmado.");
        }
    }

    @Transactional
    public PedidoCompra confirmarEntrega(Long id) {
        Optional<PedidoCompra> pedidoOptional = repository.findById(id);
        if (pedidoOptional.isEmpty()) {
            throw new RegraNegocioException("Pedido de compra não encontrado.");
        }
        PedidoCompra pedidoCompra = pedidoOptional.get();

        if (pedidoCompra.getStatusEntrega().equals("pendente")) {

            if (pedidoCompra.getStatus().equals("pagamento pendente")) {
                throw new RegraNegocioException("Não é possível confirmar a entrega sem a confirmação de pagamento.");
            }

            pedidoCompra.setStatusEntrega("entregue");
            pedidoCompra.setDataEntrega(DateUtil.formatarHifenReverso(LocalDate.now()));
            pedidoCompra.setStatus("finalizado");
            return repository.save(pedidoCompra);
        } else {
            throw new RegraNegocioException("A entrega deste pedido não pode ser confirmada.");
        }
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

    public String gerarCodigo() {
        String data, codigo;

        data = DateUtil.formatarColadoReverso(LocalDate.now());

        RandomNumberGenerator rng = RandomNumberGenerator.getRng();

        do {
            codigo = data + rng.gerarAlphaNumericoSeisDigitos();
        } while (repository.existsByCodigo(codigo));

        return codigo;
    }

    public boolean existsPedidoCompraByCodigo(String codigo) {
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
        if (pedidoCompra.getTipoEntrega().equals("delivery") && (pedidoCompra.getEndereco() == null || pedidoCompra.getEndereco().getId() == null)) {
            throw new RegraNegocioException("Endereço de entrega é obrigatório para o tipo de entrega 'delivery'");
        }
    }
}