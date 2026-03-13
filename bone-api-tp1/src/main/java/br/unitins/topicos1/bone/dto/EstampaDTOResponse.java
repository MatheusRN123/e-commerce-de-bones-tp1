package br.unitins.topicos1.bone.dto;

import br.unitins.topicos1.bone.model.Estampa;
import br.unitins.topicos1.bone.model.EstampaBordada;
import br.unitins.topicos1.bone.model.EstampaDigital;

public record EstampaDTOResponse(
    Long id,
    String tipo,
    String nome,
    String posicao,
    String descricao,
    String corLinha,
    Integer quantCores,
    String resolucao
) {
    public static EstampaDTOResponse valueOf(Estampa e) {
    
            String corLinha = null;
            Integer quantCores = null;
            String resolucao = null;
    
            if (e instanceof EstampaBordada bordada) {
                corLinha = bordada.getCorLinha();
                quantCores = bordada.getQuantCores();
            }
        
            if (e instanceof EstampaDigital digital) {
                resolucao = digital.getResolucao();
            }
        
            return new EstampaDTOResponse(
                e.getId(),
                e.getTipo(),
                e.getNome(),
                e.getPosicao(),
                e.getDescricao(),
                corLinha,
                quantCores,
                resolucao
            );
        }
    }