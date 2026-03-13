package br.unitins.topicos1.bone.dto;

import br.unitins.topicos1.bone.model.Endereco;
import br.unitins.topicos1.bone.model.Usuario;

public record EnderecoDTOResponse(
    Long id,
    String NomeDestinatário,
    String cep,
    String logradouro,
    String numero,
    String nomeCidade,
    Usuario usuario
) {
    public static EnderecoDTOResponse valueOf(Endereco endereco){

        return new EnderecoDTOResponse(
            endereco.getId(),
            endereco.getNomeDestinatario(),
            endereco.getCep(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getCidade().getNome(),
            endereco.getUsuario()
        );
    }
}