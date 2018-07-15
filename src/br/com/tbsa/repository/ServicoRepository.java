/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tbsa.repository;

import br.com.tbsa.entity.Servico;
import java.io.Serializable;

/**
 *
 * @author lg
 */
public class ServicoRepository extends RepositoryHibernate<Servico, Serializable> implements IRepositoryHibernate<Servico> {

    public ServicoRepository() {
        super(new Servico());
    }

}
