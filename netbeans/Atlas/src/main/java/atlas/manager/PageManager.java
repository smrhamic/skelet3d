package atlas.manager;

import atlas.entity.Language;
import atlas.entity.Model;
import atlas.entity.PageComponent;
import atlas.entity.Page;
import atlas.entity.PageContent;
import atlas.entity.PageContentPK;
import atlas.entity.TextComponent;
import atlas.entity.view.LabelView;
import atlas.service.CategoryService;
import atlas.service.LabelContentService;
import atlas.service.LabelService;
import atlas.service.PageContentService;
import atlas.service.PageService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    
    // current session's LoginManager to verify the right to edit
    @Inject
    LoginManager loginManager;
    
    // persistence services
    @EJB
    LabelService labelService;
    @EJB
    LabelContentService labelContentService;
    @EJB
    CategoryService categoryService;
    @EJB
    PageService pageService;
    @EJB
    PageContentService pageContentService;
    
    // 3D model used on the page if any
    private Model model;
    
    // bound properties
    private int pageId;
    private Page page;
    private PageContent pageContent;
    private List<PageComponent> components;
    private List<LabelView> labels;
    private String labelUpdates;

    /**
     * Initializes current Page and content based on bound "pageId".
     */
    public void init() {
        page = pageService.find(pageId);
        // null check the page
        if (page == null) {
            pageContent = null;
        } else {
            // fetch content
            pageContent = pageContentService.find(new PageContentPK(
                page.getId(), languageManager.getCurrentLanguage().getId()));
        }    
        
        // get components
        components = new ArrayList<>(); // empty by default
        // only get content if it exists
        if(pageContent != null) {
            // components
            components.addAll(pageContent.getHeadlineComponentList());
            components.addAll(pageContent.getTextComponentList());
            components.addAll(pageContent.getImageComponentList());
            // only add one model component, multiples not supported
            if (!pageContent.getModelComponentList().isEmpty()) {
                components.add(pageContent.getModelComponentList().get(0));
                model = pageContent.getModelComponentList().get(0).getModel();
                labels = labelService.getLabelViews(
                        model, languageManager.getCurrentLanguage());
            }
            // sort components by compOrder and replace order values by 0, 1, 2..
            components.sort(null);
            for(int i = 0; i < components.size(); i++) {
                components.get(i).setCompOrder(i);
            }
        }
    }
    
    public String goEditPage(int pageId) {
        // go to edit page if editor is logged, else stay (refresh)
        if (loginManager.isEditor()) {
            return "edit_page.xhtml?id=" + pageId + "&faces-redirect=true&includeViewParams=true";
        } else {
            return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
        }
    }
    
    public String addNewPage(int categoryId) {
        // if page is to be created and editor not logged, abort
        // this should not happen other than very rare session timeouts
        if (!loginManager.isEditor()) {
            return "/index.xhtml?faces-redirect=true";
        }
        
        // create new page
        pageService.createNewPage(categoryId);
        
        // just refresh after
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    public String deletePage(int pageId) {
        // if page is to be deleted and editor not logged, abort
        // this should not happen other than very rare session timeouts
        if (!loginManager.isEditor()) {
            return "/index.xhtml?faces-redirect=true";
        }
        
        // delete page
        pageService.delete(pageService.find(pageId));
        
        // just refresh after
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    public String updatePage() {
        // if page is to be changed and editor not logged, abort
        // this should not happen other than very rare session timeouts
        if (!loginManager.isEditor()) {
            return "/index.xhtml?faces-redirect=true";
        }
        
        // update page
        pageService.update(page);
        // update page content
        pageContentService.updateWithComponents(pageContent, components);
        
        // just refresh after
        return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
    }
    
    public String addComponent() {

        TextComponent comp = new TextComponent();
        comp.setCompOrder(components.size());
        comp.setPageContent(pageContent);
        comp.setId(0); // will be generated
        comp.setText("...");
        components.add(comp);
        
        // do not refresh, this is done by ajax
        return null;
    }
    
    public String bumpComponentUp(PageComponent component) {
        int i = components.indexOf(component);
        // swap with previous, not much to do if component is at the top
        if(i > 0) {
            components.get(i).setCompOrder(i-1);
            components.get(i-1).setCompOrder(i);
            Collections.swap(components, i, i-1);
        }        
        // do not refresh, this is done by ajax
        return null;
    }
    
    public String bumpComponentDown(PageComponent component) {
        int i = components.indexOf(component);
        // swap with next, not much to do if component is at the top
        if(i < components.size()-1) {
            components.get(i).setCompOrder(i+1);
            components.get(i+1).setCompOrder(i);
            Collections.swap(components, i, i+1);
        }        
        // do not refresh, this is done by ajax
        return null;
    }
    
    public String removeComponent(PageComponent component) {
        components.remove(component);
        // do not refresh, this is done by ajax
        return null;
    }
    
    /**
     * Update and persist model, then refresh the page.
     * 
     * @return URL string to current view including parameters.
     */
    public String updateLabels() {
        // if changes are commited and editor not logged, abort
        // this should not happen other than very rare session timeouts during edit
        if (!loginManager.isEditor()) {
            return "/index.xhtml?faces-redirect=true";
        }
        // refreshing url
        String url = FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";

        // don't do anything if 3D model is unset
        if (model == null) return url;
        Language lang = languageManager.getCurrentLanguage();
        
        labelService.updateLabelsFromJSON(labelUpdates, model, lang);
        
        return url;
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
     * @return Label Update JSON.
     */
    public String getLabelUpdates() {
        //System.out.println("Asked for JSON: " + labelUpdates);
        return labelUpdates;
    }

    /**
     * Set a JSON String of label updates.
     *
     * @param labelUpdates JSON String from label editor.
     */
    public void setLabelUpdates(String labelUpdates) {
        this.labelUpdates = labelUpdates;
    }

    /**
     * @return Current page entity.
     */
    public Page getPage() {
        return page;
    }

    /**
     * @return Localized content of current page.
     */
    public PageContent getPageContent() {
        return pageContent;
    }

}
