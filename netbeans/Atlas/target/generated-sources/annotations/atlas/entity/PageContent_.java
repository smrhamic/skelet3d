package atlas.entity;

import atlas.entity.HeadlineComponent;
import atlas.entity.ImageComponent;
import atlas.entity.Language;
import atlas.entity.ModelComponent;
import atlas.entity.Page;
import atlas.entity.PageContentPK;
import atlas.entity.TextComponent;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-25T14:56:03")
@StaticMetamodel(PageContent.class)
public class PageContent_ { 

    public static volatile ListAttribute<PageContent, ModelComponent> modelComponentList;
    public static volatile ListAttribute<PageContent, TextComponent> textComponentList;
    public static volatile SingularAttribute<PageContent, Page> page1;
    public static volatile SingularAttribute<PageContent, Language> language1;
    public static volatile ListAttribute<PageContent, HeadlineComponent> headlineComponentList;
    public static volatile SingularAttribute<PageContent, String> name;
    public static volatile ListAttribute<PageContent, ImageComponent> imageComponentList;
    public static volatile SingularAttribute<PageContent, PageContentPK> pageContentPK;
    public static volatile SingularAttribute<PageContent, Boolean> published;

}