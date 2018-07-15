package br.com.tbsa.repository;

import br.com.tbsa.entity.Ambiente;
import java.io.Serializable;

public class AmbienteRepository extends RepositoryHibernate<Ambiente, Serializable> implements IRepositoryHibernate<Ambiente> {

    public AmbienteRepository() {
        super(new Ambiente());
    }

}
