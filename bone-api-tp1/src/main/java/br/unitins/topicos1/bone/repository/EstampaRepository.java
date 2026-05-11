package br.unitins.topicos1.bone.repository;

import java.util.List;

import br.unitins.topicos1.bone.model.Estampa;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstampaRepository implements PanacheRepository<Estampa> {
    
    public PanacheQuery<Estampa> findByNome(String nome){
        return find("SELECT e FROM Estampa e WHERE lower(e.nome) LIKE lower(?1)", "%" + nome + "%");
    }

    @Override
    public PanacheQuery<Estampa> findAll() {
       return find("SELECT e FROM Estampa e ORDER BY e.nome");
    }

    public List<Estampa> listByIds(List<Long> ids) {
        return find("SELECT e FROM Estampa e WHERE id IN ?1", ids).list();
    }

    public PanacheQuery<Estampa> findByTipo(String tipo) {
        return find("SELECT e FROM Estampa e WHERE tipo_estampa = ?1", tipo);
    }
}
