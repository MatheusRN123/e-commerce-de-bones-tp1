package br.unitins.topicos1.bone.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estoque")
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Integer quantidade = 0;

    @Column(name = "data_atualizacao")
    @NotNull
    private LocalDate dataAtualizacao;

    @OneToOne(mappedBy = "estoque")
    private Bone bone;


    public Boolean verificarDisponibilidade() {
        return quantidade != null && quantidade > 0;
    }

    public void atualizarQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        this.dataAtualizacao = LocalDate.now();
    }

    public void adicionarQuantidade(Integer quantidade){
        if(this.quantidade == null){
            this.quantidade = 0;
        }

        this.quantidade += quantidade;
        this.dataAtualizacao = LocalDate.now();
    }


    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bone getBone(){
        return bone;
    }

    public void setBone(Bone bone){
        this.bone = bone;
    }
}
