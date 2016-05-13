package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.IDatabase;

import javax.inject.Inject;
import java.util.ArrayList;

public class PersistenceService implements IPersistence {
    public IDatabase database;

    @Override public ArrayList<PersistenceObject> select(
            PersistenceObject criteriaObject) {
        return null;
    }

    @Override public ArrayList<PersistenceObject> selectAll(
            Class<? extends PersistenceObject> targetMap) {
        return null;
    }

    @Override public ArrayList<PersistenceObject> selectManyWhere(
            Class<? extends PersistenceObject> targetMap, String property,
            Object value) {
        return null;
    }

    @Override public boolean insert(PersistenceObject object) {
        return false;
    }

    @Override
    public boolean delete(Class<? extends PersistenceObject> targetMap,
            PersistenceObject object) {
        return false;
    }
}
