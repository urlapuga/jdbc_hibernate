package Dao;

import java.util.List;

/**
 * Created by Андрей on 26.06.2017.
 */
public interface DAO<T> {

    List<T> getAll();

    boolean create(T t);

    T read(int id);

    boolean update(T t);

    boolean delete(int id);
}
