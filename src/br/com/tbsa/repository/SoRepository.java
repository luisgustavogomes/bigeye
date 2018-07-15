/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tbsa.repository;

import br.com.tbsa.entity.So;
import java.io.Serializable;

/**
 *
 * @author lg
 */
public class SoRepository extends RepositoryHibernate<So, Serializable> implements IRepositoryHibernate<So> {

    public SoRepository() {
        super(new So());
    }

}
