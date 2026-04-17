package br.unitins.topicos1.bone.repository;

import br.unitins.topicos1.bone.model.Modelo;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModeloRepository implements PanacheRepository<Modelo> {
    
    public PanacheQuery<Modelo> findByNome(String nome){
        return find("SELECT m FROM Modelo m WHERE lower(m.nome) LIKE lower(?1)", "%" + nome + "%");
    }

    @Override
    public PanacheQuery<Modelo> findAll() {
       return find("SELECT m FROM Modelo m ORDER BY m.nome");
    }

}