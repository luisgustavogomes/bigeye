package br.com.tbsa.repository;

import br.com.tbsa.entity.Cluster;
import java.io.Serializable;

public class ClusterRepository extends RepositoryHibernate<Cluster, Serializable> implements IRepositoryHibernate<Cluster> {

    public ClusterRepository() {
        super(new Cluster());
    }

}
