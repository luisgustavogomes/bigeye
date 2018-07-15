package br.com.tbsa.service;

import br.com.tbsa.entity.Programa;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.ProgramaRepository;
import java.util.List;

public class ProgramaService implements IService<Programa> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryPrograma;

    private ProgramaService() {
    }

    public static ProgramaService getInstancia() {
        if (instancia == null) {
            repositoryPrograma = new ProgramaRepository();
            instancia = new ProgramaService();
        }
        return (ProgramaService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Programa t) throws Exception {
        repositoryPrograma.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Programa t) throws Exception {
        repositoryPrograma.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Programa t) throws Exception {
        repositoryPrograma.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Programa> findAll() throws Exception {
        return repositoryPrograma.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Programa> findAllWithoutClose() throws Exception {
        return repositoryPrograma.findAllWithoutClose();
    }

    @Override
    public Programa findByCod(int cod) throws Exception {
        return (Programa) repositoryPrograma.findByCod(cod);
    }

}
