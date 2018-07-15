package br.com.tbsa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Programa.class)
public abstract class Programa_ {

	public static volatile SingularAttribute<Programa, Integer> idPrograma;
	public static volatile SingularAttribute<Programa, LinhaSistema> linhaSistema;
	public static volatile SetAttribute<Programa, ProgramasVm> programasVms;
	public static volatile SingularAttribute<Programa, String> nome;

}

