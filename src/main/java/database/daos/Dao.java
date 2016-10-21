package database.daos;

import java.util.Collection;

/**
 * Created by vrettos on 14.10.2016.
 */
public interface Dao<ID, TYPE> {
    void persist(TYPE entity);
    void delete(ID id);
    TYPE get(ID id);
    Collection<TYPE> list();
}
