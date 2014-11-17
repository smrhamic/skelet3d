package atlas.services;

import atlas.entity.Label;
import atlas.entity.LabelContent;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Michal
 */
@Stateless
public class LabelService {
    
    @PersistenceContext
    private EntityManager em;
    
    public Label find(int labelId) {
        return em.find(Label.class, labelId);
    }
    
    public int save(Label label) {
        em.persist(label);
        return label.getId();
    }
    
    public void update(Label label) {
        em.merge(label);
    }
    
    public void delete(Label label) {
        // delete content first
        for (LabelContent lc : label.getLabelContentList()) {
            // must merge to make it managed or whatever...
            LabelContent lcToKill = em.merge(lc);
            em.remove(lcToKill);
        }
        label.setLabelContentList(null);
        // then label itself
        Label lToKill = em.merge(label);
        em.remove(lToKill);
    }
    
}
