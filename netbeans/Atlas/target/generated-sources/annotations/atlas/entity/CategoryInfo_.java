package atlas.entity;

import atlas.entity.Category;
import atlas.entity.CategoryInfoPK;
import atlas.entity.Language;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-10-27T19:30:47")
@StaticMetamodel(CategoryInfo.class)
public class CategoryInfo_ { 

    public static volatile SingularAttribute<CategoryInfo, CategoryInfoPK> categoryInfoPK;
    public static volatile SingularAttribute<CategoryInfo, Language> language1;
    public static volatile SingularAttribute<CategoryInfo, Category> category1;
    public static volatile SingularAttribute<CategoryInfo, String> name;

}