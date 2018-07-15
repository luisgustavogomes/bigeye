package br.com.tbsa.service;

import br.com.tbsa.entity.Vm;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.VmRepository;
import java.util.List;

public class VmService implements IService<Vm> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryVm;

    private VmService() {
    }

    public static VmService getInstancia() {
        if (instancia == null) {
            repositoryVm = new VmRepository();
            instancia = new VmService();
        }
        return (VmService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Vm t) throws Exception {
        repositoryVm.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Vm t) throws Exception {
        repositoryVm.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Vm t) throws Exception {
        repositoryVm.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Vm> findAll() throws Exception {
        return repositoryVm.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Vm> findAllWithoutClose() throws Exception {
        return repositoryVm.findAllWithoutClose();
    }

    @Override
    public Vm findByCod(int cod) throws Exception {
        return (Vm) repositoryVm.findByCod(cod);
    }

}
