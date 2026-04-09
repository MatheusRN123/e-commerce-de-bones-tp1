package br.unitins.topicos1.bone.repository;

import java.util.List;

import br.unitins.topicos1.bone.model.Estampa;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstampaRepository implements PanacheRepository<Estampa> {
    
    public PanacheQuery<Estampa> findByNome(String nome){
        return find("SELECT e FROM Estampa e WHERE e.nome LIKE ?1", "%" + nome + "%");
    }

    @Override
    public PanacheQuery<Estampa> findAll() {
       return find("SELECT e FROM Estampa e ORDER BY e.nome");
    }

    public List<Estampa> findByIds(List<Long> ids) {
        return list("id in ?1", ids);
    }

    public PanacheQuery<Estampa> findByTipo(String tipo) {
        return find("tipo_estampa = ?1", tipo);
    }
}
