package br.unitins.topicos1.bone.dto;

import br.unitins.topicos1.bone.model.ItemPedido;

public record ItemPedidoDTOResponse(
    Long id,
    Long idPedido,
    BoneDTOResponse bone,
    Integer quantidade,
    Double subtotal
) {
    public static ItemPedidoDTOResponse valueOf(ItemPedido itemPedido){

        Long idPedido = itemPedido.getPedido() != null ? itemPedido.getPedido().getId() : null;

        return new ItemPedidoDTOResponse(
            itemPedido.getId(),
            idPedido,
            BoneDTOResponse.valueOf(itemPedido.getBone()),
            itemPedido.getQuantidade(),
            itemPedido.getSubTotal()
        );
    }
}
