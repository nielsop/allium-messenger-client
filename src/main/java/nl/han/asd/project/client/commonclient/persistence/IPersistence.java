package nl.han.asd.project.client.commonclient.persistence;

import java.util.ArrayList;

public interface IPersistence {
    ArrayList<PersistenceObject> select(PersistenceObject criteriaObject);
    ArrayList<PersistenceObject> selectAll(Class<? extends PersistenceObject> targetMap);
    ArrayList<PersistenceObject> selectManyWhere(Class<? extends PersistenceObject> targetMap, String property, Object value);

    boolean insert(PersistenceObject object);

    boolean delete(Class<? extends PersistenceObject> targetMap, PersistenceObject object);


}
