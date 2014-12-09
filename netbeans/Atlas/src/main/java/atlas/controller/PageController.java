package atlas.controller;

import atlas.entity.HeadlineComponent;
import atlas.entity.ImageComponent;
import atlas.entity.Language;
import atlas.entity.Model;
import atlas.entity.ModelComponent;
import atlas.entity.PageComponent;
import atlas.entity.Page;
import atlas.entity.PageContent;
import atlas.entity.PageContentPK;
import atlas.entity.TextComponent;
import atlas.entity.view.LabelView;
import atlas.service.LabelService;
import atlas.service.PageContentService;
import atlas.service.PageService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Provides content for the page.
 * ViewScoped managed bean, controller of content_page.xhtml component.
 * Should be initialized by calling the init() method.
 *
 * @author Michal Smrha
 */
@ViewScoped
@Named("pageController")
public class PageController extends BasicController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // current session's LanguageController to get current language
    @Inject
    LanguageController languageController;
    
    // current session's LoginController to verify the right to edit
    @Inject
    LoginController loginController;
    
    // persistence services
    @EJB
    LabelService labelService;
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
    private UIComponent saveButton;

    /**
     * Initializes current Page and content based on bound "pageId".
     * Doesn't include content if page is not published and user is not editor.
     * Limits model componenets to 1 (multiples not supported).
     */
    public void init() {
        page = pageService.find(pageId);
        // null check the page
        if (page == null) {
            pageContent = null;
        } else {
            // fetch content
            pageContent = pageContentService.find(new PageContentPK(
                page.getId(), languageController.getCurrentLanguage().getId()));
        }    
        
        // get components
        components = new ArrayList<>(); // empty by default
        // only get content if it exists
        if(pageContent != null) {
            // display content if published or for editor
            if(pageContent.getPublished() || loginController.isEditor()) {
                // components
                components.addAll(pageContent.getHeadlineComponentList());
                components.addAll(pageContent.getTextComponentList());
                components.addAll(pageContent.getImageComponentList());
                // only add one model component, multiples not supported
                if (!pageContent.getModelComponentList().isEmpty()) {
                    model = pageContent.getModelComponentList().get(0).getModel();
                    // don't want a null model
                    if (model != null) {
                        components.add(pageContent.getModelComponentList().get(0));
                        labels = labelService.createLabelViews(
                                model, languageController.getCurrentLanguage());
                    }
                }
                // sort components by compOrder and replace order values by 0, 1, 2..
                components.sort(null);
                for(int i = 0; i < components.size(); i++) {
                    components.get(i).setCompOrder(i);
                }
            } else {
                // if not published nor editor, tell them
                showWarning("#{messages.notPublished}");
            }
        }
    }
    
    /**
     * Updates Page and PageContent entities including components based on bound fields.
     * Page info and "components" collection are persisted.
     * Checks if editor is logged.
     * If this check fails, warning is set and changes not saved.
     * If all checks pass, page is updated and info is set.
     * 
     * @param ajax True if called by ajax, false if refresh is needed.
     * @return Redirection string, null if ajax.
     */
    public String updatePage(boolean ajax) {
        // check edit rights
        if (!loginController.isEditor()) {
            // add localized message if lacking edit rights
            showWarning("#{messages.noRights}");
            return null;
        }
        
        // update page
        pageService.update(page);
        // update page content
        pageContentService.updateWithComponents(pageContent, components);
        
        // set message
        showInfo(saveButton, "#{messages.changesSaved}");
        
        // refresh or not depending on ajax
        if (ajax) {
            // update content
            init();
            return null;
        } else {
            // refresh
            return FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";
        }
    }
    
    /**
     * Adds a new PageComponent of given type to bound collection "components".
     * Info message is set.
     *
     * @param type Type of component to add. Supported: text | headline | model | image
     * @return Null (Redirection string)
     */
    public String addComponent(String type) {

        switch (type) {
            case "text":
                TextComponent tc = new TextComponent();
                tc.setCompOrder(components.size());
                tc.setPageContent(pageContent);
                tc.setId(0); // will be generated
                tc.setText("...");
                components.add(tc);
                break;
            case "headline":
                HeadlineComponent hc = new HeadlineComponent();
                hc.setCompOrder(components.size());
                hc.setPageContent(pageContent);
                hc.setId(0); // will be generated
                hc.setText("...");
                components.add(hc);
                break;
            case "model":
                ModelComponent mc = new ModelComponent();
                mc.setCompOrder(components.size());
                mc.setPageContent(pageContent);
                mc.setId(0); // will be generated
                mc.setDescription("");
                mc.setModel(null);
                components.add(mc);
                break;
            case "image":
                ImageComponent ic = new ImageComponent();
                ic.setCompOrder(components.size());
                ic.setPageContent(pageContent);
                ic.setId(0); // will be generated
                ic.setDescription("");
                ic.setImage(null);
                components.add(ic);
                break;
        }
        
        // add message
        showInfo(saveButton, "#{messages.addedComponent}");
        
        // no way we would like to refresh and lose the component
        return null;
    }
    
    /**
     * Moves PageComponent one slot up.
     * Changes component order in bound collection "components"
     * to move a component up by one.
     *
     * @param component Component to move up.
     * @return Null (Redirection string)
     */
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
    
    /**
     * Moves PageComponent one slot down.
     * Changes component order in bound collection "components"
     * to move a component down by one.
     *
     * @param component Component to move down.
     * @return Null (Redirection string)
     */
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
    
    /**
     * Remove PageComponent from bound collection "components".
     *
     * @param component Component to remove.
     * @return Null (Redirection string)
     */
    public String removeComponent(PageComponent component) {
        components.remove(component);
        // do not refresh, this is done by ajax
        return null;
    }
    
    /**
     * Update and persist model's labels based on bound "labelUpdates".
     * "labelUpdates" is a JSON string representing changed LabelViews.
     * Checks if editor is logged.
     * If this check fails, FacesMessage is set and redirect is null.
     * If all checks pass, labels are updated and redirect reloads page.
     * 
     * @return Redirection string.
     */
    public String updateLabels() {
        // check edit rights
        if (!loginController.isEditor()) {
            // add localized message if lacking edit rights
            showWarning("#{messages.noRights}");
            return null;
        }
        // refreshing url
        String url = FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";

        // don't do anything if 3D model is unset
        if (model == null) return url;
        Language lang = languageController.getCurrentLanguage();
        
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
     * Set a JSON String representing LabelViews to update.
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

    /**
     * @return Save button component, target of certain messages.
     */
    public UIComponent getSaveButton() {
        return saveButton;
    }

    /**
     * @param saveButton Save button component, target of certain messages.
     */
    public void setSaveButton(UIComponent saveButton) {
        this.saveButton = saveButton;
    }
}
