package br.com.tbsa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ambiente.class)
public abstract class Ambiente_ {

	public static volatile SingularAttribute<Ambiente, Integer> idAmbiente;
	public static volatile SetAttribute<Ambiente, Vm> vms;
	public static volatile SingularAttribute<Ambiente, String> descricao;

}

