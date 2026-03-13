package br.unitins.topicos1.bone.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.bone.model.Pedido;

public record PedidoDTOResponse(
    Long id,
    LocalDateTime data,
    UsuarioDTOResponse Usuario,
    String cep,
    String logradouro,
    String numero,
    String cidade,
    String estado,
    PagamentoDTOResponse pagamento,
    List<ItemPedidoDTOResponse> itens,
    Double valorTotal
) {
    public static PedidoDTOResponse valueOf(Pedido pedido) {
        return new PedidoDTOResponse(
            pedido.getId(),
            pedido.getData(),
            UsuarioDTOResponse.valueOf(pedido.getUsuario()),
            pedido.getCep(),
            pedido.getLogradouro(),
            pedido.getNumero(),
            pedido.getCidade(),
            pedido.getEstado(),
            pedido.getPagamento() != null ? PagamentoDTOResponse.valueOf(pedido.getPagamento()) : null,
            pedido.getItens() != null
                ? pedido.getItens().stream()
                    .map(ItemPedidoDTOResponse::valueOf)
                    .collect(Collectors.toList())
                : null,
            pedido.getValorTotal()
        );
    }
}
