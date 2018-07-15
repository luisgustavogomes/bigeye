package br.com.tbsa.service;

import br.com.tbsa.entity.Usuario;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.repository.UsuarioRepository;
import br.com.tbsa.utl.HelperString;
import java.util.List;

public class UsuarioService implements IService<Usuario> {

    public static List<PerfilRepositoryHelper> list;
    public static Usuario userLogin;
    private static IService instancia;
    private static IRepositoryHibernate repositoryUsuario;

    private UsuarioService() {
    }

    public static UsuarioService getInstancia() {
        if (instancia == null) {
            repositoryUsuario = new UsuarioRepository();
            instancia = new UsuarioService();
        }
        return (UsuarioService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Usuario t) throws Exception {
        repositoryUsuario.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Usuario t) throws Exception {
        repositoryUsuario.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Usuario t) throws Exception {
        repositoryUsuario.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Usuario> findAll() throws Exception {
        return repositoryUsuario.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Usuario> findAllWithoutClose() throws Exception {
        return repositoryUsuario.findAllWithoutClose();
    }

    @Override
    public Usuario findByCod(int cod) throws Exception {
        return (Usuario) repositoryUsuario.findByCod(cod);
    }

    public Boolean validationUser(String user, String pass) throws Exception {
        Boolean retorno = false;
        List<Usuario> usuarios = this.findAllWithoutClose();
        for (Usuario usuario : usuarios) {
            if ((usuario.getNome().equals(user) && HelperString.decript(usuario.getSenha()).equals(pass))) {
                userLogin = usuario;
                PerfilService perfilService = PerfilService.getInstancia();
                list = perfilService.setCheckBox(UsuarioService.userLogin.getPerfil().getTextoMenu());
                retorno = true;
            }
        }
        return retorno;
    }

}
