package br.com.tbsa.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProgramasVm.class)
public abstract class ProgramasVm_ {

	public static volatile SetAttribute<ProgramasVm, ServicosVm> servicosVms;
	public static volatile SingularAttribute<ProgramasVm, Vm> vm;
	public static volatile SingularAttribute<ProgramasVm, Programa> programa;
	public static volatile SetAttribute<ProgramasVm, ProgramasVmHst> programasVmHsts;
	public static volatile SingularAttribute<ProgramasVm, Date> dataInstalacao;
	public static volatile SingularAttribute<ProgramasVm, Integer> idProgramasVm;

}

