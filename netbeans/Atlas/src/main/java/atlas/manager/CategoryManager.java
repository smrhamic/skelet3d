package atlas.manager;

import atlas.entity.Category;
import atlas.entity.Page;
import atlas.entity.view.CategoryView;
import atlas.entity.view.PageView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    // EntityManager responsible for model persistence
    @PersistenceContext
    private EntityManager em;
    
    // current Category Entity
    private Category currentCategoryEntity;
    // bound properties
    private int categoryId;
    private CategoryView currentCategory;
    private List<CategoryView> childCategories;
    private List<PageView> pages;
    private List<CategoryView> rootCategories;

  
    /**
     * Initializes current Category based on bound "categoryId".
     */
    public void init() {
        currentCategoryEntity = Category.getCategoryById(em, categoryId);
        // only for testing - comment out later
        em.getEntityManagerFactory().getCache().evictAll();
    }
    
    /**
     * Gets currentCategory, which contains basic localized information
     * about current category in the form of CategoryView.
     * 
     * @return Currently active category.  Default info if current entity is null.
     */
    public CategoryView getCurrentCategory() {
        // return old value if already set
        if (currentCategory != null) {
            return currentCategory;
        }
        // get CategoryView if entity is set, otherwise default
        if (currentCategoryEntity != null) {
            currentCategory = CategoryView.getCategoryView(
                em, currentCategoryEntity, languageManager.getCurrentLanguage());
        } else {
            currentCategory = CategoryView.getDefaultCategoryView();
        }
        
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
        // return old value if already set
        if (childCategories != null) {
            return childCategories;
        }
        
        childCategories = new ArrayList<>();
        // add child CategoryViews to list if entity is set, otherwise remain empty
        if (currentCategoryEntity != null) {
            for ( Category entity : currentCategoryEntity.getCategoryList() ) {
                childCategories.add(CategoryView.getCategoryView(
                        em, entity, languageManager.getCurrentLanguage()));
            }
        }
        
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
        // return old value if already set
        if (rootCategories != null) {
            return rootCategories;
        }
        
        rootCategories = new ArrayList<>();
        // add root CategoryViews to list
        for ( Category entity : Category.getRootCategories(em) ) {
            rootCategories.add(CategoryView.getCategoryView(
                    em, entity, languageManager.getCurrentLanguage()));
        }
        
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
        // return old value if already set
        if (pages != null) {
            return pages;
        }
        
        pages = new ArrayList<>();
        // add child PageViews to list if entity is set, otherwise remain empty
        if (currentCategoryEntity != null) {
            for ( Page entity : currentCategoryEntity.getPageList() ) {
                pages.add(PageView.getPageView(
                        em, entity, languageManager.getCurrentLanguage()));
            }
        }
        
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
