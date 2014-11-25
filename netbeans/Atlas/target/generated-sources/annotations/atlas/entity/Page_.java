package atlas.entity;

import atlas.entity.Category;
import atlas.entity.PageContent;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-25T14:56:03")
@StaticMetamodel(Page.class)
public class Page_ { 

    public static volatile ListAttribute<Page, PageContent> pageContentList;
    public static volatile SingularAttribute<Page, Integer> id;
    public static volatile SingularAttribute<Page, String> latin;
    public static volatile SingularAttribute<Page, Category> category;

}