package atlas.service;

import atlas.entity.Category;
import atlas.entity.Language;
import atlas.entity.Page;
import atlas.entity.PageContent;
import atlas.entity.PageContentPK;
import atlas.entity.view.PageView;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Provides CRUD persistence for Page entity.
 * This is an EJB Stateless session bean.
 * It can also create PageViews.
 *
 * @author Michal Smrha
 * @see atlas.entity.Page
 * @see atlas.service.BasicService
 */
@Stateless
public class PageService extends BasicService<Page, Integer> {
    
    @EJB
    private CategoryService categoryService;
    @EJB
    private LanguageService languageService;
    @EJB
    private PageContentService pageContentService;
    
    /**
     * Constructs a PageService.
     */
    public PageService() {
        super(Page.class);
    }

    /**
     * Creates a new Page entity in given Category.
     * Also generates a blank unpublished PageContent for each available language.
     * Default name is "NEW PAGE", other fields are blank.
     *
     * @param categoryId ID of the parent Category.
     * @return Created blank Page.
     */
    public Page createNewPage(int categoryId) {
        Page page = new Page();
        // populate main fields
        page.setCategory(categoryService.find(categoryId));
        page.setLatin("");
        page.setId(0); // will be generated
        // persist to generate ID
        save(page);
        // populate info for all languages
        for(Language anyLang:languageService.findAll()) {
            PageContent pc = new PageContent();
            pc.setPageContentPK(new PageContentPK(page.getId(), anyLang.getId()));
            pc.setPage1(page);
            pc.setLanguage1(anyLang);
            pc.setName("NEW PAGE");
            pc.setPublished(false);
            pageContentService.save(pc);
        }
        // update to make sure children were added
        update(page);
        categoryService.update(categoryService.find(categoryId));
        
        return page;
    }
    
    /**
     * Creates a localized PageView for given Page.
     * This is done by reading persisted information about
     * Page and related PageContent.
     *
     * @param page Page to get a view for.
     * @param lang Language to get a view in.
     * @return View of the Page in the Language.
     */
    public PageView createPageView(Page page, Language lang) {
        // find info for page in language
        String name;
        Boolean published;
        PageContent pc;
        try {
            String queryString = "SELECT c FROM PageContent c "
                            + "WHERE c.page1 = :page AND c.language1 = :lang";
            TypedQuery<PageContent> query = em.createQuery(
                    queryString, PageContent.class);
            pc = query.setParameter("page", page).setParameter("lang", lang)
                    .getSingleResult();
            name = pc.getName();
            published = pc.getPublished();
        } catch (NoResultException e) {
            name = "missing language variant";
            published = false;
        }
        return new PageView(page, name, published);
    }
    
    @Override
    public void delete(Page page) {           
        // remove from category collection (why doesn't JPA do it for me?)
        Category cat = page.getCategory(); 
        cat.getPageList().remove(page);
        // delete page
        super.delete(page);
        // update parent category (why doesn't JPA do it for me?!)
        categoryService.update(cat);
    }
        
}
