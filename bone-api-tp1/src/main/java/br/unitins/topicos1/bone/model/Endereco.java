package br.unitins.topicos1.bone.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cep;
    private String logradouro;
    private String numero;

    @Column(name = "nome_destinatario")
    private String nomeDestinatario;

    @ManyToOne
    @JoinColumn(name = "id_cidade")
    private Cidade cidade;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public String getNomeDestinatario(){
        return nomeDestinatario;
    }

    public void setNomeDestinatario(String nomeDestinatario){
        this.nomeDestinatario = nomeDestinatario;
    }
}
