package br.com.tbsa.repository;

import br.com.tbsa.entity.Perfil;
import java.io.Serializable;

public class PerfilRepository extends RepositoryHibernate<Perfil, Serializable> implements IRepositoryHibernate<Perfil> {

    public PerfilRepository() {
        super(new Perfil());
    }

}
