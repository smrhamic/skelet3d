package atlas.service;

import atlas.entity.LabelContent;
import atlas.entity.LabelContentPK;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Michal
 */
@Stateless
public class LabelContentService extends BasicService<LabelContent, LabelContentPK> {

    public LabelContentService() {
        super(LabelContent.class);
    }
    
    @Override
    public void save(LabelContent labelContent) {
        // escape strings
        labelContent.setTitle(
                StringEscapeUtils.escapeJavaScript(labelContent.getTitle()));
        labelContent.setText(
                StringEscapeUtils.escapeJavaScript(labelContent.getText()));
        // save
        em.persist(labelContent);
    }
    
    @Override
    public void update(LabelContent labelContent) {
        labelContent.setTitle(
                StringEscapeUtils.escapeJavaScript(labelContent.getTitle()));
        labelContent.setText(
                StringEscapeUtils.escapeJavaScript(labelContent.getText()));
        em.merge(labelContent);
    }
    
}