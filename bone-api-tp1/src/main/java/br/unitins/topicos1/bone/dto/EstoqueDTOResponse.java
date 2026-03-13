package br.unitins.topicos1.bone.dto;

import java.time.LocalDate;

import br.unitins.topicos1.bone.model.Estoque;

public record EstoqueDTOResponse(
    Long id,
    Long idBone,
    Integer quantidade,
    LocalDate dataAtualizacao
) {

    public static EstoqueDTOResponse valueOf(Estoque estoque){

        if(estoque == null){
            return null;
        }

        long idBone = estoque.getBone() != null ? estoque.getBone().getId() : null;
        
        return new EstoqueDTOResponse(
            estoque.getId(),
            idBone,
            estoque.getQuantidade(),
            estoque.getDataAtualizacao()
        );

    }

}
