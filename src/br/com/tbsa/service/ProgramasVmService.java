package br.com.tbsa.service;

import br.com.tbsa.entity.ProgramasVm;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.ProgramasVmRepository;
import java.util.ArrayList;
import java.util.List;

public class ProgramasVmService implements IService<ProgramasVm> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryProgramasVm;

    private ProgramasVmService() {
    }

    public static ProgramasVmService getInstancia() {
        if (instancia == null) {
            repositoryProgramasVm = new ProgramasVmRepository();
            instancia = new ProgramasVmService();
        }
        return (ProgramasVmService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(ProgramasVm t) throws Exception {
        repositoryProgramasVm.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(ProgramasVm t) throws Exception {
        repositoryProgramasVm.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(ProgramasVm t) throws Exception {
        repositoryProgramasVm.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProgramasVm> findAll() throws Exception {
        return repositoryProgramasVm.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProgramasVm> findAllWithoutClose() throws Exception {
        return repositoryProgramasVm.findAllWithoutClose();
    }

    @Override
    public ProgramasVm findByCod(int cod) throws Exception {
        return (ProgramasVm) repositoryProgramasVm.findByCod(cod);
    }

}
