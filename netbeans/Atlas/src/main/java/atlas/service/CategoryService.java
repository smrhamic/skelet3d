package atlas.service;

import atlas.entity.Category;
import atlas.entity.CategoryInfo;
import atlas.entity.Language;
import atlas.entity.Page;
import atlas.entity.PageContent;
import atlas.entity.view.CategoryView;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Provides CRUD persistence and other methods for Category entity.
 * This is an EJB Stateless session bean.
 * It also provides methods to get CategoryViews for a Category, get root Categories
 * and count number of pages in a Category.
 *
 * @author Michal Smrha
 * @see atlas.entity.Category
 * @see atlas.service.BasicService
 * @see atlas.entity.view.CategoryView
 */
@Stateless
public class CategoryService extends BasicService<Category, Integer> {

    /**
     * Constructs a CategoryService.
     */
    public CategoryService() {
        super(Category.class);
    }
    
    /**
     * Creates a localized CategoryView for given Category.
     * This is done by reading persisted information about
     * Category and related CategoryInfo.
     * Also, number of pages in Category is calculated.
     *
     * @param cat Category to get a view for
     * @param lang Language to get the view in
     * @return View of the Category in the Language
     * @see #countNumberOfPages(atlas.entity.Category, atlas.entity.Language, boolean)
     */
    public CategoryView createCategoryView(Category cat, Language lang) {
        // find info for category in language: localized name
        String nameTemp;
        try {
            TypedQuery<CategoryInfo> query = em.createQuery(
                    "SELECT i FROM CategoryInfo i "
                            + "WHERE i.category1 = :cat AND i.language1 = :lang",
                    CategoryInfo.class);
            nameTemp = query.setParameter("cat", cat).setParameter("lang", lang)
                    .getSingleResult().getName();
        } catch (NoResultException e) {
            nameTemp = "missing language variant";
        }
        // count visible models for this language variant and create         
        return new CategoryView(cat, nameTemp,
                countNumberOfPages(cat, lang, true));
    }
    
    /**
     * Finds all root Category entities, that is categories with null parent.
     *
     * @return All root Categories
     */
    public List<Category> findRootCategories() {
        // find categories with no parents
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c "
                        + "WHERE c.parentCategory IS NULL",
                Category.class);
        return query.getResultList();
    }
    
    /**
     * Counts the number of pages in given Category (and given Language).
     * This is a recursive call that might be expensive in larger trees.
     * Thankfully, the category tree in Atlas is expected to be small
     * (less than 50 categories).
     *
     * @param cat Category in which to count the pages.
     * @param lang Language in which to count the pages.
     * @param publishedOnly True if only published pages should be counted.
     * @return Number of pages in given Category and its subcategories in given language.
     */
    public int countNumberOfPages(Category cat, Language lang, boolean publishedOnly) {
        int numPages = 0;
        // +1 for each page, check if published
        for (Page page : cat.getPageList()) {
            for (PageContent pc : page.getPageContentList()) {
                if (pc.getLanguage1().equals(lang) && (!publishedOnly || pc.getPublished())) {
                    numPages++;
                }
            }
        }
        // run recursively for child categories
        for (Category child : cat.getCategoryList()) {
            numPages += countNumberOfPages(child, lang, publishedOnly);
        }
        return numPages;
    }
}
