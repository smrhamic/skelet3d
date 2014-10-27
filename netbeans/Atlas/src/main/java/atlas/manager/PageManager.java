package atlas.manager;

import atlas.entity.ModelComponent;
import atlas.entity.PageComponent;
import atlas.entity.Page;
import atlas.entity.PageContent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Controller of content_page.xhtml component.
 * Provides content for the page.
 *
 * @author Michal
 */
@ViewScoped
@Named("pageManager")
public class PageManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // current session's LanguageManager to get current language
    @Inject
    LanguageManager languageManager;
    
    // EntityManager responsible for model persistence
    @PersistenceContext
    private EntityManager em;
    
    // bound properties
    private int pageId;
    private Page page;
    private String name;
    private PageContent pageContent;
    private List<PageComponent> components;

    /**
     * Initializes current Page and content based on bound "pageId".
     */
    public void init() {
        page = Page.getPageById(em, pageId);
        // fetch content
        pageContent = PageContent.getPageContentByPageAndLanguage(
                em, page, languageManager.getCurrentLanguage());
    }

    /**
     * @return ID of currently active page.
     */
    public int getPageId() {
        return pageId;
    }

    /**
     * @param pageId ID of currently active page.
     */
    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    /**
     * Gets components, which contains localized content of current page.
     *
     * @return List of this page's components. Empty list if pageContent is null.
     */
    public List<PageComponent> getComponents() {
        // return old value if already set
        if (components != null) {
            return components;
        }
        
        components = new ArrayList<>();        
        // retuen empty list if pageContent is null
        if (pageContent == null) {
            return components;
        }
        // otherwise add all types of components
        components.addAll(pageContent.getHeadlineComponentList());
        components.addAll(pageContent.getTextComponentList());
        components.addAll(pageContent.getImageComponentList());
        components.addAll(pageContent.getModelComponentList());
        
        // TO REMOVE model component for testing
        ModelComponent model1 = new ModelComponent();
        model1.setCompOrder(0);
        model1.setDescription("3D Model");
        components.add(model1);
        
        // sort components by compOrder
        components.sort(null);
        
        return components;
    }

    /**
     * @return Current page entity.
     */
    public Page getPage() {
        return page;
    }

    /**
     * @return Localized name of current page.
     */
    public String getName() {
        return pageContent.getName();
    }

}
