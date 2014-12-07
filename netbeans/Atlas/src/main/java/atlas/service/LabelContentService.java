package atlas.service;

import atlas.entity.LabelContent;
import atlas.entity.LabelContentPK;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Provides CRUD persistence for LabelContent entity.
 * This is an EJB Stateless session bean.
 * Behaves like BasicService typed for LabelContent, but escapes strings.
 *
 * @author Michal Smrha
 * @see atlas.entity.LabelContent
 * @see atlas.service.BasicService
 */
@Stateless
public class LabelContentService extends BasicService<LabelContent, LabelContentPK> {

    /**
     * Constructs a LabelContentService.
     */
    public LabelContentService() {
        super(LabelContent.class);
    }
    
    @Override
    public void save(LabelContent labelContent) {
        // escape strings for JS
        labelContent.setTitle(
                StringEscapeUtils.escapeJavaScript(labelContent.getTitle()));
        labelContent.setText(
                StringEscapeUtils.escapeJavaScript(labelContent.getText()));
        // save
        super.save(labelContent);
    }
    
    @Override
    public void update(LabelContent labelContent) {
        // escape strings for JS
        labelContent.setTitle(
                StringEscapeUtils.escapeJavaScript(labelContent.getTitle()));
        labelContent.setText(
                StringEscapeUtils.escapeJavaScript(labelContent.getText()));
        // update
        super.update(labelContent);
    }
    
}