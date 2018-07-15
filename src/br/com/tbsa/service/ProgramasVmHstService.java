package br.com.tbsa.service;

import br.com.tbsa.entity.ProgramasVmHst;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.ProgramasVmHstRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramasVmHstService implements IService<ProgramasVmHst> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryProgramasVmHst;

    private ProgramasVmHstService() {
    }

    public static ProgramasVmHstService getInstancia() {
        if (instancia == null) {
            repositoryProgramasVmHst = new ProgramasVmHstRepository();
            instancia = new ProgramasVmHstService();
        }
        return (ProgramasVmHstService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(ProgramasVmHst t) throws Exception {
        repositoryProgramasVmHst.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(ProgramasVmHst t) throws Exception {
        repositoryProgramasVmHst.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(ProgramasVmHst t) throws Exception {
        repositoryProgramasVmHst.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProgramasVmHst> findAll() throws Exception {
        return repositoryProgramasVmHst.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProgramasVmHst> findAllWithoutClose() throws Exception {
        return repositoryProgramasVmHst.findAllWithoutClose();
    }

    @SuppressWarnings("unchecked")
    public List<ProgramasVmHst> findAllWithoutClose(int idProgramasVm) throws Exception {
        return findAllWithoutClose().parallelStream().filter(f -> f.getProgramasVm().getIdProgramasVm() == idProgramasVm).collect(Collectors.toList());
    }

    @Override
    public ProgramasVmHst findByCod(int cod) throws Exception {
        return (ProgramasVmHst) repositoryProgramasVmHst.findByCod(cod);
    }

}
