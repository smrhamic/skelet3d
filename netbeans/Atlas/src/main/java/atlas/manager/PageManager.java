package atlas.manager;

import atlas.entity.Label;
import atlas.entity.LabelContent;
import atlas.entity.LabelContentPK;
import atlas.entity.Language;
import atlas.entity.Model;
import atlas.entity.PageComponent;
import atlas.entity.Page;
import atlas.entity.PageContent;
import atlas.entity.view.LabelView;
import atlas.services.LabelContentService;
import atlas.services.LabelService;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
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
    // TO BE REPLACED BY SERVICES
    @PersistenceContext
    private EntityManager em;
    
    // persistence services
    @EJB
    LabelService labelService;
    @EJB
    LabelContentService labelContentService;
    
    // 3D model used on the page if any
    private Model model;
    
    // bound properties
    private int pageId;
    private Page page;
    private String name;
    private PageContent pageContent;
    private List<PageComponent> components;
    private List<LabelView> labels;
    private String labelUpdates;

    /**
     * Initializes current Page and content based on bound "pageId".
     */
    public void init() {
        page = Page.getPageById(em, pageId);
        // fetch content
        pageContent = PageContent.getPageContentByPageAndLanguage(
                em, page, languageManager.getCurrentLanguage());
        
        // get components
        components = new ArrayList<>();
        // only get components if content exists
        if(pageContent != null) {
            components.addAll(pageContent.getHeadlineComponentList());
            components.addAll(pageContent.getTextComponentList());
            components.addAll(pageContent.getImageComponentList());
            // only add one model component, multiples not supported yet
            if (!pageContent.getModelComponentList().isEmpty()) {
                components.add(pageContent.getModelComponentList().get(0));
                model = pageContent.getModelComponentList().get(0).getModel();
                labels = LabelView.getLabelViews(em, model,
                        languageManager.getCurrentLanguage());
            }
            // sort components by compOrder
            components.sort(null);
        }
    }
    
    /**
     * Update and persist model, then refresh the page.
     * 
     * @return URL string to current view including parameters.
     */
    public String updateLabels() {
        // refreshing url
        String url = FacesContext.getCurrentInstance().getViewRoot().getViewId()
                + "?faces-redirect=true&includeViewParams=true";

        // don't do anything if 3D model is unset
        if (model == null) return url;
        Language lang = languageManager.getCurrentLanguage();
        
        System.out.println("Got JSON: " + labelUpdates);
        
        // parse JSON
        Gson gson = new Gson();
        LabelView[] updates = gson.fromJson(labelUpdates, LabelView[].class);
        
        // treat each change according to "action"
        for(LabelView update:updates) {
            switch (update.getAction()) {
                // create a new label + contents
                case "create":
                case "update":
                    // populate label
                    Label label = new Label();
                    label.setLabelX(update.getLabelX());
                    label.setLabelY(update.getLabelY());
                    label.setLabelZ(update.getLabelZ());
                    label.setMarkX(update.getMarkX());
                    label.setMarkY(update.getMarkY());
                    label.setMarkZ(update.getMarkZ());
                    label.setModel(model);
                    // here it splits for create and update
                    if (update.getAction().equals("create")) {
                        // add id and persist
                        label.setId(0); // will be generated
                        labelService.save(label);
                        // populate content for all languages
                        for(Language anyLang:languageManager.getSupportedLanguages()) {
                            LabelContent lc = new LabelContent();
                            lc.setLabelContentPK(
                                    new LabelContentPK(label.getId(), anyLang.getId()));
                            lc.setLabel1(label);
                            lc.setLanguage1(anyLang);
                            if(lang.equals(anyLang)) {
                                lc.setTitle(update.getTitle());
                                lc.setText(update.getText());
                            } else {
                                lc.setTitle("[" + lang.getShort1() + "] " + update.getTitle());
                                lc.setText("[" + lang.getShort1() + "] " + update.getText());
                            }
                            labelContentService.save(lc);
                        }
                    } else {
                        // copy id and persist
                        label.setId(update.getId());
                        labelService.update(label);
                        // populate content for current language
                        LabelContent lc = labelContentService.find(
                                label.getId(), lang.getId());
                        
                        lc.setTitle(update.getTitle());
                        lc.setText(update.getText());
                        // persist
                        labelContentService.update(lc);
                    }
                    break;
                case "delete":
                    labelService.delete(labelService.find(update.getId()));
            }
   
        }
        
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
     * @return Localized name of current page.
     */
    public String getName() {
        return pageContent.getName();
    }

}
