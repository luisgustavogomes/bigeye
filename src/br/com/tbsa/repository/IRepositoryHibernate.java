package br.com.tbsa.repository;

import java.util.List;

public interface IRepositoryHibernate<T> {

    void save(T t) throws Exception;

    void update(T t) throws Exception;

    void delete(T t) throws Exception;

    List<T> findAll() throws Exception;

    List<T> findAllWithoutClose() throws Exception;

    T findByCod(int cod) throws Exception;
}
