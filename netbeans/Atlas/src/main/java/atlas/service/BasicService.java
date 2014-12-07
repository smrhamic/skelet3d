package atlas.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Provides CRUD persistence for a given entity class.
 * It needs the class of the entity and the class of its primary key to operate.
 * While it is possible to use this class directly, it will more often be
 * a parent for other services to inherit from.
 *
 * @author Michal Smrha
 * @param <E> Entity class
 * @param <PK> PrimaryKey class
 */
public class BasicService<E, PK> {
    
    /**
     * Persistence context which takes care of persistence and transactions.
     */
    @PersistenceContext
    protected EntityManager em;
    

    /**
     * Path to uploaded files.
     * TO DO: Not have this hardwired
     */
    protected String uploadFolder = "/var/atlas-webapp/uploads/";
    
    // generic entity type
    private final Class<E> type;
    
    /**
     * Constructs a BasicService for a particular entity class.
     *
     * @param entityType Entity class
     */
    public BasicService(Class<E> entityType) {
        this.type = entityType;
    }
    
    /**
     * Finds a persisted entity object with matching primary key.
     *
     * @param key Primary key to match.
     * @return Matching object or null.
     */
    public E find(PK key) {
        return em.find(type, key);
    }
    
    /**
     * Finds all persisted objects of this service's entity class.
     *
     * @return All persisted objects of given type.
     */
    public List<E> findAll() {
        String table = type.getSimpleName();
        String queryString = "SELECT x FROM " + table + " x";
        TypedQuery<E> query = em.createQuery(
                queryString, type);
        return query.getResultList();
    }
    
    /**
     * Persists a previously unpersisted entity.
     *
     * @param entity Entity to persist
     */
    public void save(E entity) {
        em.persist(entity);
    }
    
    /**
     * Persists an entity or updates its previous values.
     *
     * @param entity Entity to persist
     */
    public void update(E entity) {
        em.merge(entity);
    }
    
    /**
     * Removes an entity from persistence
     *
     * @param entity Entity to remove
     */
    public void delete(E entity) {
        // merge to make it managed
        E toKill = em.merge(entity);
        // remove
        em.remove(toKill);
    }
}
