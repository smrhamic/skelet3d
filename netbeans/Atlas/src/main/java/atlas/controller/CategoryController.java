package atlas.controller;

import atlas.entity.Category;
import atlas.entity.Page;
import atlas.entity.view.CategoryView;
import atlas.entity.view.PageView;
import atlas.service.CategoryService;
import atlas.service.PageService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Provides content for browsing categories.
 * ViewScoped managed bean, controller for category.xhtml component.
 * Should be initialized by calling the init() method.
 *
 * @author Michal Smrha
 */
@ViewScoped
@Named("categoryController")
public class CategoryController extends BasicController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // current session's LanguageController to get current language
    @Inject
    LanguageController languageController;
    
    // current session's LoginController to verify the right to edit
    @Inject
    LoginController loginController;
    
    // persistence services
    @EJB
    CategoryService categoryService;
    @EJB
    PageService pageService;
    
    // bound properties
    private int categoryId = -1;
    private CategoryView currentCategory;
    private List<CategoryView> childCategories;
    private List<PageView> pages;
    private List<CategoryView> rootCategories;

  
    /**
     * Initializes current Category based on bound "categoryId".
     * Creates localized views of current Category, its subcategories
     * and pages. Also creates views of root categories.
     */
    public void init() {
        // reload all - only for testing - comment out later
        //em.getEntityManagerFactory().getCache().evictAll();
        
        // get category ("entity", not "view")
        Category currentCategoryEntity = categoryService.find(categoryId);
        
        // get CategoryView if entity is set, otherwise default
        if (currentCategoryEntity != null) {
            currentCategory = categoryService.createCategoryView(
                    currentCategoryEntity, languageController.getCurrentLanguage());
        } else {
            CategoryView emptyCV = new CategoryView();
            emptyCV.setId(0);
            emptyCV.setName("");
            emptyCV.setLatin("");
            currentCategory = emptyCV;
        }
        
        // get root categories
        rootCategories = new ArrayList<>();
        // add root CategoryViews to list
        for (Category entity : categoryService.findRootCategories()) {
            rootCategories.add(categoryService.createCategoryView(
                    entity, languageController.getCurrentLanguage()));
        }
        
        // get child categories
        // if no ID is set, get root categories instead
        if (categoryId == -1) {
            childCategories = rootCategories;
        } else {
            // otherwise proceed to get children
            childCategories = new ArrayList<>();
            // add child CategoryViews to list if entity is set, otherwise remain empty
            if (currentCategoryEntity != null) {
                for ( Category entity : currentCategoryEntity.getCategoryList() ) {
                    childCategories.add(categoryService.createCategoryView(
                    entity, languageController.getCurrentLanguage()));
                }
            }
        }
        
        // get pages
        pages = new ArrayList<>();
        // add child PageViews to list if entity is set, otherwise remain empty
        if (currentCategoryEntity != null) {
            PageView pv;
            for (Page entity : currentCategoryEntity.getPageList()) {
                pv = pageService.createPageView(
                        entity, languageController.getCurrentLanguage());
                // add if published OR add all if editor is logged
                if(loginController.isEditor() || pv.getPublished()) {
                    pages.add(pv);
                }
            }
        }
    }
    
    /**
     * Creates new blank page in current category and sets FacesMessage.
     * Checks if editor is logged.
     * If this check fails, nothing is created and warning is set.
     * If all checks pass, page is created and info is set.
     *
     * @param ajax True if called by ajax, false if refresh is needed.
     * @return Redirection string, null if ajax.
     */
    public String addNewPage(boolean ajax) {
        // check edit rights
        if (!loginController.isEditor()) {
            // add localized message if lacking edit rights
            showWarning("#{messages.noRights}");
            return null;
        }
        
        // create new page and set suitable message
        pageService.createNewPage(categoryId);
        showInfo("#{messages.pageAdded}");
        
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
     * Deletes Page entity of matching ID.
     * Checks if editor is logged.
     * If this check fails, nothing is deleted and warning is set.
     * If all checks pass, page is deleted and info is set.
     *
     * @param pageId ID of the page to be deleted.
     * @param ajax True if called by ajax, false if refresh is needed.
     * @return Redirection string, null if ajax.
     */
    public String deletePage(int pageId, boolean ajax) {
        // check edit rights
        if (!loginController.isEditor()) {
            // add localized message if lacking edit rights
            showWarning("#{messages.noRights}");
            return null;
        }
        
        // save name for msg
        String pageName = pageService.createPageView(
                pageService.find(pageId), languageController.getCurrentLanguage())
                .getName();
        
        // delete page
        pageService.delete(pageService.find(pageId));        
        // show message
        showInfo("#{messages.pageDeleted} " + pageName + ".");
        
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
     * Redirects to page editor of a page with matching ID.
     * Checks if editor is logged.
     * If this check fails, redirect is null.
     * If all checks pass, redirect points to editor.
     * 
     * @param pageId ID of page to edit.
     * @return Redirection string.
     */
    public String goEditPage(int pageId) {
        // go to edit page if editor is logged, else stay
        if (loginController.isEditor()) {
            return "edit_page.xhtml?id=" + pageId + "&faces-redirect=true&includeViewParams=true";
        } else {
            return null;
        }
    }
    
    /**
     * Gets currentCategory, which contains basic localized information
     * about current category in the form of CategoryView.
     * 
     * @return Currently active category.  Default info if current entity is null.
     */
    public CategoryView getCurrentCategory() {
        return currentCategory;
    }

    /**
     * Gets childCategories, which contains basic localized information
     * about subcategories of current category in the form of a list of
     * CategoryViews.
     * 
     * @return List of subcategories of currently active category. Empty list if current entity is null.
     */
    public List<CategoryView> getChildCategories() {
        return childCategories;
    }

    /**
     * Gets rootCategories, which contains basic localized information
     * about top level categories (categories without a parent) in the form
     * of a list of CategoryViews.
     * 
     * @return List of top level categories. Empty list if current entity is null.
     */
    public List<CategoryView> getRootCategories() {
        return rootCategories;
    }
    
    /**
     * Gets "pages", which contains basic localized information
     * about pages directly nested in current category in the form
     * of a list of PageViews.
     * 
     * @return List of pages in active category. Empty list if current entity is null.
     */
    public List<PageView> getPages() {
        return pages;
    }

    /**
     * @return ID of currently active category.
     */
    public int getCategoryId() {
        return categoryId;
    }
    
    /**
     * @param categoryId ID of currently active category.
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
