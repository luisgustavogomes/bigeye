package br.com.tbsa.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProgramasVmHst.class)
public abstract class ProgramasVmHst_ {

	public static volatile SingularAttribute<ProgramasVmHst, String> observacao;
	public static volatile SingularAttribute<ProgramasVmHst, Date> dtatualizacao;
	public static volatile SingularAttribute<ProgramasVmHst, Integer> idHts;
	public static volatile SingularAttribute<ProgramasVmHst, String> versao;
	public static volatile SingularAttribute<ProgramasVmHst, ProgramasVm> programasVm;

}

