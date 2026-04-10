package br.unitins.topicos1.bone.repository;

import br.unitins.topicos1.bone.model.Material;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaterialRepository implements PanacheRepository<Material> {
    
    public PanacheQuery<Material> findByNome(String nome){
        return find("SELECT m FROM Material m WHERE lower(m.nome) LIKE lower(?1) ", "%"+nome+"%");
    }

    @Override
    public PanacheQuery<Material> findAll() {
       return find("SELECT m FROM Material m ORDER BY m.nome");
    }
}
