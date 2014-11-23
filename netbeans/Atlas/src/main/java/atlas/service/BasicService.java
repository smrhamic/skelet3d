package atlas.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Michal
 * @param <E> Entity class
 * @param <PK> PrimaryKey class
 */
public class BasicService<E, PK> {
    
    @PersistenceContext
    protected EntityManager em;
    
    // generic entity type
    private final Class<E> type;
    
    public BasicService(Class<E> entityType) {
        this.type = entityType;
    }
    
    public E find(PK key) {
        return em.find(type, key);
    }
    
    public List<E> findAll() {
        String table = type.getSimpleName();
        String queryString = "SELECT x FROM " + table + " x";
        TypedQuery<E> query = em.createQuery(
                queryString, type);
        return query.getResultList();
    }
    
    public void save(E entity) {
        em.persist(entity);
    }
    
    public void update(E entity) {
        em.merge(entity);
    }
    
    public void delete(E entity) {
        // merge to make it managed
        E toKill = em.merge(entity);
        // remove
        em.remove(toKill);
    }
}
