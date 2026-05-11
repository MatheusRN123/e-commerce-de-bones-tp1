package br.unitins.topicos1.bone.repository;

import java.util.List;

import br.unitins.topicos1.bone.model.Cidade;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CidadeRepository implements PanacheRepository<Cidade> {
    
    public List<Cidade> findByNome(String nome){
        return find("SELECT c FROM Cidade c WHERE c.nome LIKE ?1", "%" + nome + "%").list();
    }

    public List<Cidade> listByIds(List<Long> ids) {
        return find("SELECT c FROM Cidade c WHERE id IN ?1", ids).list();
    }
}
