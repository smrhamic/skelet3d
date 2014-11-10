package atlas.manager;

import atlas.entity.ModelComponent;
import atlas.entity.PageComponent;
import atlas.entity.Page;
import atlas.entity.PageContent;
import atlas.entity.view.LabelView;
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
    private List<LabelView> labels;

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
        // only add one model component, multiples not supported yet
        if (!pageContent.getModelComponentList().isEmpty()) {
            components.add(pageContent.getModelComponentList().get(0));
            labels = LabelView.getLabelViews(em,
                    pageContent.getModelComponentList().get(0).getModel(),
                    languageManager.getCurrentLanguage());
        }
        
        // sort components by compOrder
        components.sort(null);
        
        return components;
    }
    
    /**
     * Returns localized model labels if there is a model component present.
     * Returns null if there is no model component or if called prior to calling
     * getComponents().
     * 
     * @return Labels for current model or null.
     */
    public List<LabelView> getLabels() {
        return labels;
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
