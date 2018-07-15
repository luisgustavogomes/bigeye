package br.com.tbsa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Vm.class)
public abstract class Vm_ {

	public static volatile SingularAttribute<Vm, Cluster> cluster;
	public static volatile SingularAttribute<Vm, Integer> idVm;
	public static volatile SingularAttribute<Vm, Boolean> servidorBd;
	public static volatile SingularAttribute<Vm, String> nomeVm;
	public static volatile SingularAttribute<Vm, Ambiente> ambiente;
	public static volatile SetAttribute<Vm, ProgramasVm> programasVms;
	public static volatile SingularAttribute<Vm, So> so;
	public static volatile SingularAttribute<Vm, String> ipVm;
	public static volatile SingularAttribute<Vm, String> descricao;

}

