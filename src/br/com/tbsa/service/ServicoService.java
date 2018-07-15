package br.com.tbsa.service;

import br.com.tbsa.entity.Servico;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.ServicoRepository;
import java.util.List;

public class ServicoService implements IService<Servico> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryServico;

    private ServicoService() {
    }

    public static ServicoService getInstancia() {
        if (instancia == null) {
            repositoryServico = new ServicoRepository();
            instancia = new ServicoService();
        }
        return (ServicoService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Servico t) throws Exception {
        repositoryServico.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Servico t) throws Exception {
        repositoryServico.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Servico t) throws Exception {
        repositoryServico.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Servico> findAll() throws Exception {
        return repositoryServico.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Servico> findAllWithoutClose() throws Exception {
        return repositoryServico.findAllWithoutClose();
    }

    @Override
    public Servico findByCod(int cod) throws Exception {
        return (Servico) repositoryServico.findByCod(cod);
    }

}
