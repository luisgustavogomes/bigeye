package br.com.tbsa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Perfil.class)
public abstract class Perfil_ {

	public static volatile SingularAttribute<Perfil, Integer> idPerfil;
	public static volatile SingularAttribute<Perfil, String> textoMenu;
	public static volatile SetAttribute<Perfil, Usuario> usuarios;
	public static volatile SingularAttribute<Perfil, String> codperfil;

}

