package br.com.tbsa.service;

import br.com.tbsa.entity.Perfil;
import br.com.tbsa.repository.IRepositoryHibernate;
import br.com.tbsa.repository.PerfilRepository;
import br.com.tbsa.repository.PerfilRepositoryHelper;
import br.com.tbsa.utl.HelperString;
import com.jfoenix.controls.JFXCheckBox;
import java.util.ArrayList;
import java.util.List;

public class PerfilService implements IService<Perfil> {

    private static IService instancia;
    private static IRepositoryHibernate repositoryPerfil;
    private static PerfilRepositoryHelper repositoryPerfilHelper;

    private PerfilService() {
    }

    public static PerfilService getInstancia() {
        if (instancia == null) {
            repositoryPerfil = new PerfilRepository();
            repositoryPerfilHelper = new PerfilRepositoryHelper();
            instancia = new PerfilService();
        }
        return (PerfilService) instancia;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Perfil t) throws Exception {
        repositoryPerfil.save(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Perfil t) throws Exception {
        repositoryPerfil.update(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Perfil t) throws Exception {
        repositoryPerfil.delete(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Perfil> findAll() throws Exception {
        return repositoryPerfil.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Perfil> findAllWithoutClose() throws Exception {
        return repositoryPerfil.findAllWithoutClose();
    }

    @Override
    public Perfil findByCod(int cod) throws Exception {
        return (Perfil) repositoryPerfil.findByCod(cod);
    }

    public PerfilRepositoryHelper getCheckBox(JFXCheckBox checkBox) {
        return new PerfilRepositoryHelper(checkBox.getId(), checkBox.isSelected());
    }

    public String getTextoMenu(List<PerfilRepositoryHelper> lista) {
        String msg = "";
        msg = lista.stream().map((l) -> l.toString()).reduce(msg, String::concat);
        return msg;
    }

    public List<PerfilRepositoryHelper> setCheckBox(String msg) {
        List<PerfilRepositoryHelper> arr = new ArrayList<>();
        String[] quedra = msg.split("#");
        for (String quedra1 : quedra) {
            String[] item = quedra1.split(";");
            arr.add(new PerfilRepositoryHelper(item[0], HelperString.is(item[1])));
        }
        return arr;
    }

}
