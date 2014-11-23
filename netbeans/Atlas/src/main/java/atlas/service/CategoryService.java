package atlas.service;

import atlas.entity.Category;
import atlas.entity.CategoryInfo;
import atlas.entity.Language;
import atlas.entity.view.CategoryView;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Michal
 */
@Stateless
public class CategoryService extends BasicService<Category, Integer> {

    public CategoryService() {
        super(Category.class);
    }
    
    public CategoryView getCategoryView(Category cat, Language lang) {
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
        return new CategoryView(cat, nameTemp, cat.getNumberOfPages(lang, true));
    }
    
    public List<Category> getRootCategories() {
        // find categories with no parents
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c "
                        + "WHERE c.parentCategory IS NULL",
                Category.class);
        return query.getResultList();
    }
}
