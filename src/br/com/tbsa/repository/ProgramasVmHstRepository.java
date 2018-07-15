package br.com.tbsa.repository;

import br.com.tbsa.entity.ProgramasVmHst;
import java.io.Serializable;

public class ProgramasVmHstRepository extends RepositoryHibernate<ProgramasVmHst, Serializable> implements IRepositoryHibernate<ProgramasVmHst> {

    public ProgramasVmHstRepository() {
        super(new ProgramasVmHst());
    }

}
