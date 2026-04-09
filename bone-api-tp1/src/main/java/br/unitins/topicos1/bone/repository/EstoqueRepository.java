package br.unitins.topicos1.bone.repository;

import br.unitins.topicos1.bone.model.Estoque;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstoqueRepository implements PanacheRepository<Estoque> {
    
    public PanacheQuery<Estoque> findByQuantidade(Integer quantidade){
        return find("SELECT e FROM Estoque e WHERE e.quantidade = ?1 ", quantidade);
    }

    @Override
    public PanacheQuery<Estoque> findAll() {
       return find("SELECT e FROM Estoque e ORDER BY e.quantidade");
    }
}
