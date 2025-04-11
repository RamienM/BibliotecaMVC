package org.biblioteca.bibliotecamvc.business.mapper.interfaces;

public interface Mapper<T,K> {
    T toDTO(K entity);
    K toEntity(T dto);
}
