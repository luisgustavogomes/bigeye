package br.com.tbsa.service;

import br.com.tbsa.entity.LinhaSistema;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.LinhaSistemaRepository;
import java.util.List;

public class LinhaSistemaService implements IService<LinhaSistema> {

    private static IService intancia;
    private static IRepositoryHibernate repositoryLinhaSistema;

    private LinhaSistemaService() {
    }

    public static LinhaSistemaService getInstancia() {
        if (intancia == null) {
            repositoryLinhaSistema = new LinhaSistemaRepository();
            intancia = new LinhaSistemaService();
        }
        return (LinhaSistemaService) intancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(LinhaSistema t) throws Exception {
        repositoryLinhaSistema.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(LinhaSistema t) throws Exception {
        repositoryLinhaSistema.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(LinhaSistema t) throws Exception {
        repositoryLinhaSistema.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LinhaSistema> findAll() throws Exception {
        return repositoryLinhaSistema.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LinhaSistema> findAllWithoutClose() throws Exception {
        return repositoryLinhaSistema.findAllWithoutClose();
    }

    @Override
    public LinhaSistema findByCod(int cod) throws Exception {
        return (LinhaSistema) repositoryLinhaSistema.findByCod(cod);
    }

}
