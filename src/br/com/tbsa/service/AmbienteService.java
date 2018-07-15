package br.com.tbsa.service;

import br.com.tbsa.entity.Ambiente;
import br.com.tbsa.repository.AmbienteRepository;
import br.com.tbsa.repository.IRepositoryHibernate;
import java.util.List;

public class AmbienteService implements IService<Ambiente> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryAmbiente;

    private AmbienteService() {
    }

    public static AmbienteService getInstancia() {
        if (instancia == null) {
            repositoryAmbiente = new AmbienteRepository();
            instancia = new AmbienteService();
        }
        return (AmbienteService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Ambiente t) throws Exception {
        repositoryAmbiente.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Ambiente t) throws Exception {
        repositoryAmbiente.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Ambiente t) throws Exception {
        repositoryAmbiente.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ambiente> findAll() throws Exception {
        return repositoryAmbiente.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ambiente> findAllWithoutClose() throws Exception {
        return repositoryAmbiente.findAllWithoutClose();
    }

    @Override
    public Ambiente findByCod(int cod) throws Exception {
        return (Ambiente) repositoryAmbiente.findByCod(cod);
    }

}
