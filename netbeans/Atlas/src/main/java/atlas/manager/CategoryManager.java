package atlas.manager;

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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller for category.xhtml component.
 * Provides content for browsing categories. 
 *
 * @author Michal
 */
@ViewScoped
@Named("categoryManager")
public class CategoryManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // current session's LanguageManager to get current language
    @Inject
    LanguageManager languageManager;
    
    // current session's LoginManager to verify the right to edit
    @Inject
    LoginManager loginManager;
    
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
     */
    public void init() {
        // reload all - only for testing - comment out later
        //em.getEntityManagerFactory().getCache().evictAll();
        
        // get category ("entity", not "view")
        Category currentCategoryEntity = categoryService.find(categoryId);
        
        // get CategoryView if entity is set, otherwise default
        if (currentCategoryEntity != null) {
            currentCategory = categoryService.getCategoryView(
                    currentCategoryEntity, languageManager.getCurrentLanguage());
        } else {
            currentCategory = CategoryView.getDefaultCategoryView();
        }
        
        // get root categories
        rootCategories = new ArrayList<>();
        // add root CategoryViews to list
        for (Category entity : categoryService.getRootCategories()) {
            rootCategories.add(categoryService.getCategoryView(
                    entity, languageManager.getCurrentLanguage()));
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
                    childCategories.add(categoryService.getCategoryView(
                    entity, languageManager.getCurrentLanguage()));
                }
            }
        }
        
        // get pages
        pages = new ArrayList<>();
        // add child PageViews to list if entity is set, otherwise remain empty
        if (currentCategoryEntity != null) {
            PageView pv;
            for (Page entity : currentCategoryEntity.getPageList()) {
                pv = pageService.getPageView(
                        entity, languageManager.getCurrentLanguage());
                // add if published OR add all if editor is logged
                if(loginManager.isEditor() || pv.getPublished()) {
                    pages.add(pv);
                }
            }
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
     * Gets pages, which contains basic localized information
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
