package br.unitins.topicos1.bone.dto;

import java.util.List;

import br.unitins.topicos1.bone.model.Bone;
import br.unitins.topicos1.bone.model.EstampaBordada;
import br.unitins.topicos1.bone.model.EstampaDigital;

public record BoneDTOResponse(
    Long id,
    String nome,
    String cor,
    String nomeMaterial,
    String categoriaAba,
    Float tamanhoAba,
    Float profundidade,
    String circunferencia,
    //Bordado bordado,
    String bordado,
    String nomeMarca,
    //EstoqueDTOResponse estoque,
    Integer quantidadeEstoque,
    String nomeModelo,
    List<?> estampas,
    Double preco
)  {
    
        public static BoneDTOResponse valueOf(Bone bone){

        List<?> estampas = (bone.getEstampas() != null) ? bone.getEstampas().stream().map(estampa -> {
            if (estampa instanceof EstampaBordada eb){
                return EstampaBordadaDTOResponse.valueOf(eb);
            } else if (estampa instanceof EstampaDigital ed){
                return EstampaDigitalDTOResponse.valueOf(ed);
            }
            return null;
        }).toList() : null;

        return new BoneDTOResponse(
            bone.getId(),
            bone.getNome(),
            bone.getCor(),
            bone.getMaterial().getNome(),
            bone.getCategoriaAba(),
            bone.getTamanhoAba(),
            bone.getProfundidade(),
            bone.getCircunferencia(),
            bone.getBordado().getNome(),
            bone.getMarca().getNome(),
            // EstoqueDTOResponse.valueOf(bone.getEstoque()),
            bone.getEstoque() != null ? bone.getEstoque().getQuantidade() : null,
            bone.getModelo().getNome(),
            estampas,
            bone.getPreco()
        );
    }
}

