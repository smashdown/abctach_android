package com.abctech.blogtalking.repository;

import java.util.List;

public interface BaseRepository<T> {
    long count();

    void save(final T item);

    void save(final Iterable<T> items);

    void update(final T item);

    void delete(final T item);

    void deleteAll();

    List<T> findAll();
}