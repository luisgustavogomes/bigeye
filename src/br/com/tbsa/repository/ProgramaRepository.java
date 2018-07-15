package br.com.tbsa.repository;

import br.com.tbsa.entity.Programa;
import java.io.Serializable;

public class ProgramaRepository extends RepositoryHibernate<Programa, Serializable> implements IRepositoryHibernate<Programa> {

    public ProgramaRepository() {
        super(new Programa());
    }

}
