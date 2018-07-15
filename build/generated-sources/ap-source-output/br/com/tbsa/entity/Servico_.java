package br.com.tbsa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Servico.class)
public abstract class Servico_ {

	public static volatile SetAttribute<Servico, ServicosVm> servicosVms;
	public static volatile SingularAttribute<Servico, Integer> idServico;
	public static volatile SingularAttribute<Servico, String> executavel;
	public static volatile SingularAttribute<Servico, String> nome;
	public static volatile SingularAttribute<Servico, Boolean> status;
	public static volatile SingularAttribute<Servico, String> descricao;

}

