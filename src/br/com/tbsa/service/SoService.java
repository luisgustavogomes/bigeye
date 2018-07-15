package br.com.tbsa.service;

import br.com.tbsa.entity.So;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.SoRepository;
import java.util.List;

public class SoService implements IService<So> {

    private static IService instancia;
    private static IRepositoryHibernate repositorySo;

    private SoService() {
    }

    public static SoService getInstancia() {
        if (instancia == null) {
            repositorySo = new SoRepository();
            instancia = new SoService();
        }
        return (SoService) instancia;
    }

    @Override
    public void save(So t) throws Exception {
        repositorySo.save(t);
    }

    @Override
    public void update(So t) throws Exception {
        repositorySo.update(t);
    }

    @Override
    public void delete(So t) throws Exception {
        repositorySo.delete(t);
    }

    @Override
    public List<So> findAll() throws Exception {
        return repositorySo.findAll();
    }

    @Override
    public List<So> findAllWithoutClose() throws Exception {
        return repositorySo.findAllWithoutClose();
    }

    @Override
    public So findByCod(int cod) throws Exception {
        return (So) repositorySo.findByCod(cod);
    }

}
