package br.com.tbsa.repository;

import br.com.tbsa.entity.LinhaSistema;
import java.io.Serializable;

public class LinhaSistemaRepository extends RepositoryHibernate<LinhaSistema, Serializable> implements IRepositoryHibernate<LinhaSistema> {

    public LinhaSistemaRepository() {
        super(new LinhaSistema());
    }

}
