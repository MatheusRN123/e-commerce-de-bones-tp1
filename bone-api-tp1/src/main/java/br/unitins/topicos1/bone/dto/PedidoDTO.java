package br.unitins.topicos1.bone.dto;

import java.util.List;

public record PedidoDTO(
    Long idEndereco,
    List<ItemPedidoDTO> itens,
    PagamentoDTO pagamento
) {}
