package br.com.tbsa.repository;

import br.com.tbsa.entity.ProgramasVm;
import java.io.Serializable;

public class ProgramasVmRepository extends RepositoryHibernate<ProgramasVm, Serializable> implements IRepositoryHibernate<ProgramasVm> {

    public ProgramasVmRepository() {
        super(new ProgramasVm());
    }

}
