package atlas.service;

import atlas.entity.HeadlineComponent;
import atlas.entity.ImageComponent;
import atlas.entity.ModelComponent;
import atlas.entity.PageComponent;
import atlas.entity.PageContent;
import atlas.entity.PageContentPK;
import atlas.entity.TextComponent;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Provides CRUD persistence for PageContent entity.
 * This is an EJB Stateless session bean.
 * It also updates the PageComponents related to the PageContent.
 *
 * @author Michal Smrha
 * @see atlas.entity.PageContent
 * @see atlas.service.BasicService
 */
@Stateless
public class PageContentService extends BasicService<PageContent, PageContentPK> {

    /**
     * Construct PageContentService
     */
    public PageContentService() {
        super(PageContent.class);
    }
    
    /**
     * Updates a PageContent including its PageComponents.
     * Components are replaced by the list passed to this method.
     *
     * @param pageContent PageContent to be updated.
     * @param components PageComponents to be replace the old ones.
     */
    public void updateWithComponents(
            PageContent pageContent, List<PageComponent> components) {

        // clear old components (would be more efficient to actually update
        // but clearing is much easier and impact should be low)
        pageContent.getHeadlineComponentList().clear();
        pageContent.getTextComponentList().clear();
        pageContent.getImageComponentList().clear();
        pageContent.getModelComponentList().clear();
        // fill in new ones
        for (PageComponent comp:components) {
            switch (comp.getComponentType()) {
                case "headline":
                    HeadlineComponent hc = (HeadlineComponent)comp;
                    //em.persist(tc); // NOPE, cascading magic is actually happening
                    pageContent.getHeadlineComponentList().add(hc);
                    break;
                case "text":
                    TextComponent tc = (TextComponent)comp;
                    pageContent.getTextComponentList().add(tc);
                    break;
                case "image":
                    ImageComponent ic = (ImageComponent)comp;
                    // don't try to add null images
                    if(ic.getImage() == null) {
                        break;
                    }
                    pageContent.getImageComponentList().add(ic);
                    break;
                case "model":
                    ModelComponent mc = (ModelComponent)comp;
                    // don't try to add null models
                    if(mc.getModel() == null) {
                        break;
                    }
                    pageContent.getModelComponentList().add(mc);
                    break;
            }
        }
        // persist changes
        update(pageContent);
    }
}