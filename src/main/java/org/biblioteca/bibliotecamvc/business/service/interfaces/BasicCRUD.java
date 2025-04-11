package org.biblioteca.bibliotecamvc.business.service.interfaces;

import java.util.List;

public interface BasicCRUD<T,K> {
    List<T> findAll();
    T findById(K id);
    T save(T t);
    T update(T t, K id);
    void delete(K id);
}
