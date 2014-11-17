package atlas.services;

import atlas.entity.LabelContent;
import atlas.entity.LabelContentPK;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Michal
 */
@Stateless
public class LabelContentService {
    
    @PersistenceContext
    private EntityManager em;
    
    public LabelContent find(int labelId, int langId) {
        return em.find(LabelContent.class, new LabelContentPK(labelId, langId));
    }
    
    public void save(LabelContent labelContent) {
        labelContent.setTitle(
                StringEscapeUtils.escapeJavaScript(labelContent.getTitle()));
        labelContent.setText(
                StringEscapeUtils.escapeJavaScript(labelContent.getText()));
        em.persist(labelContent);
    }
    
    public void update(LabelContent labelContent) {
        labelContent.setTitle(
                StringEscapeUtils.escapeJavaScript(labelContent.getTitle()));
        labelContent.setText(
                StringEscapeUtils.escapeJavaScript(labelContent.getText()));
        em.merge(labelContent);
    }
    
    public void delete(LabelContent labelContent) {
        em.remove(labelContent);
    }
    
}