package br.com.tbsa.service;

import br.com.tbsa.entity.Cluster;
import br.com.tbsa.repository.ClusterRepository;
import br.com.tbsa.repository.IRepositoryHibernate;
import java.util.List;

public class ClusterService implements IService<Cluster> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryCluster;

    private ClusterService() {
    }

    public static ClusterService getInstancia() {
        if (instancia == null) {
            repositoryCluster = new ClusterRepository();
            instancia = new ClusterService();
        }
        return (ClusterService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Cluster t) throws Exception {
        repositoryCluster.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Cluster t) throws Exception {
        repositoryCluster.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Cluster t) throws Exception {
        repositoryCluster.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Cluster> findAll() throws Exception {
        return repositoryCluster.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Cluster> findAllWithoutClose() throws Exception {
        return repositoryCluster.findAllWithoutClose();
    }

    @Override
    public Cluster findByCod(int cod) throws Exception {
        return (Cluster) repositoryCluster.findByCod(cod);
    }

}
