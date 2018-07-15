package br.com.tbsa.repository;

import br.com.tbsa.entity.Vm;
import java.io.Serializable;

public class VmRepository extends RepositoryHibernate<Vm, Serializable> implements IRepositoryHibernate<Vm> {

    public VmRepository() {
        super(new Vm());
    }

}
