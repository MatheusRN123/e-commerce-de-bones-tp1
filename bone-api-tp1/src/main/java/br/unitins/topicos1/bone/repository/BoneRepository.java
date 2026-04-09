package br.unitins.topicos1.bone.repository;

import br.unitins.topicos1.bone.model.Bone;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BoneRepository implements PanacheRepository<Bone> {
    
    public PanacheQuery<Bone> findByNome(String nome){
        return find("SELECT b FROM Bone b WHERE b.nome LIKE ?1", "%" + nome + "%");
    }

    @Override
    public PanacheQuery<Bone> findAll() {
       return find("SELECT b FROM Bone b ORDER BY b.nome");
    }

}
