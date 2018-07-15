package br.com.tbsa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Cluster.class)
public abstract class Cluster_ {

	public static volatile SingularAttribute<Cluster, Integer> idCluster;
	public static volatile SingularAttribute<Cluster, String> ipCluster;
	public static volatile SingularAttribute<Cluster, So> so;
	public static volatile SetAttribute<Cluster, Vm> vms;
	public static volatile SingularAttribute<Cluster, String> descricao;

}

