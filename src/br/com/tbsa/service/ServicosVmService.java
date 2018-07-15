package br.com.tbsa.service;

import br.com.tbsa.entity.ServicosVm;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.ServicosVmRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ServicosVmService implements IService<ServicosVm> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryServicosVm;

    private ServicosVmService() {
    }

    public static ServicosVmService getInstancia() {
        if (instancia == null) {
            repositoryServicosVm = new ServicosVmRepository();
            instancia = new ServicosVmService();
        }
        return (ServicosVmService) instancia;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void save(ServicosVm t) throws Exception {
        repositoryServicosVm.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(ServicosVm t) throws Exception {
        repositoryServicosVm.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(ServicosVm t) throws Exception {
        repositoryServicosVm.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ServicosVm> findAll() throws Exception {
        return repositoryServicosVm.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ServicosVm> findAllWithoutClose() throws Exception {
        return repositoryServicosVm.findAllWithoutClose();
    }

    @SuppressWarnings("unchecked")
    public List<ServicosVm> findAllWithoutClose(int idProgramaVm) throws Exception {
        return findAllWithoutClose().parallelStream().filter(f -> f.getProgramasVm().getIdProgramasVm() == idProgramaVm).collect(Collectors.toList());
    }

    @Override
    public ServicosVm findByCod(int cod) throws Exception {
        return (ServicosVm) repositoryServicosVm.findByCod(cod);
    }

}
