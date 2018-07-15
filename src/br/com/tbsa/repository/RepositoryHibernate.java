/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tbsa.repository;

import br.com.tbsa.utl.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author lg
 */
public abstract class RepositoryHibernate<T, ID extends Serializable> implements Serializable, IRepositoryHibernate<T> {

    private static final long serialVersionUID = 1L;
    Session s;
    Transaction t;
    Query q;
    Criteria c;

    T entity;

    public RepositoryHibernate(T entity) {
        this.entity = entity;
    }

    public void save(T e) throws Exception {
        if (!s.isOpen() || s == null) {
            s = HibernateUtil.getSessionFactory().openSession();
        }
        t = s.beginTransaction();
        s.flush(); //implementado 
        s.clear(); //implementado
        s.save(e);
        t.commit();
        s.close();

    }

    public void update(T e) throws Exception {
        if (!s.isOpen() || s == null) {
            s = HibernateUtil.getSessionFactory().openSession();
        }
        t = s.beginTransaction();
        s.flush(); //implementado 
        s.clear(); //implementado
        s.update(e);
        t.commit();

        s.close();

    }

    public void delete(T e) throws Exception {

        if (!s.isOpen() || s == null) {
            s = HibernateUtil.getSessionFactory().openSession();
        }
        t = s.beginTransaction();
        s.flush(); //implementado 
        s.clear(); //implementado
        s.delete(e);
        t.commit();
        s.close();
    }

    public List<T> findAll() throws Exception {
        if (!s.isOpen() || s == null) {
            s = HibernateUtil.getSessionFactory().openSession();
        }
        @SuppressWarnings("unchecked")
        List<T> lista = s.createCriteria(entity.getClass()).list();
        s.close();
        return lista;
    }

    public List<T> findAllWithoutClose() throws Exception {
        s = HibernateUtil.getSessionFactory().openSession();
        List<T> lista = s.createCriteria(entity.getClass()).list();
        return lista;
    }

    public void closeSession() throws Exception {
        if (s.isOpen()) {
            s.close();
        }
    }

    public T findByCod(int cod) throws Exception {
        s = HibernateUtil.getSessionFactory().openSession();
        T e = (T) s.get(entity.getClass(), cod);
        s.close();
        return e;
    }

}
