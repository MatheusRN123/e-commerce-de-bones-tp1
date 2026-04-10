package br.unitins.topicos1.bone.repository;

import br.unitins.topicos1.bone.model.Marca;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarcaRepository implements PanacheRepository<Marca> {
    
    public PanacheQuery<Marca> findByNome(String nome){
        return find("SELECT m FROM Marca m WHERE lower(m.nome) LIKE lower(?1) ", "%"+nome+"%");
    }

    @Override
    public PanacheQuery<Marca> findAll() {
       return find("SELECT m FROM Marca m ORDER BY m.nome");
    }
}
