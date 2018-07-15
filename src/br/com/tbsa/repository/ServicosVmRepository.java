package br.com.tbsa.repository;

import br.com.tbsa.entity.ServicosVm;
import java.io.Serializable;

public class ServicosVmRepository extends RepositoryHibernate<ServicosVm, Serializable> implements IRepositoryHibernate<ServicosVm> {

    public ServicosVmRepository() {
        super(new ServicosVm());
    }

}
