package ru.nazimov.BankAccounts.mapper;

import java.util.List;

public interface Mapper<T, U> {

    T toEntity(U dto);

    U toDto(T entity);

    List<U> toListDto(List<T> entities);

}
