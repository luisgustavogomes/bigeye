package br.com.tbsa.repository;

import br.com.tbsa.entity.Usuario;
import java.io.Serializable;

public class UsuarioRepository extends RepositoryHibernate<Usuario, Serializable> implements IRepositoryHibernate<Usuario> {

    public UsuarioRepository() {
        super(new Usuario());
    }

}
