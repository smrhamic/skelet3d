package atlas.entity;

import atlas.entity.Category;
import atlas.entity.CategoryInfo;
import atlas.entity.Page;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-24T16:59:41")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile ListAttribute<Category, CategoryInfo> categoryInfoList;
    public static volatile ListAttribute<Category, Category> categoryList;
    public static volatile SingularAttribute<Category, Category> parentCategory;
    public static volatile SingularAttribute<Category, Integer> id;
    public static volatile SingularAttribute<Category, String> latin;
    public static volatile ListAttribute<Category, Page> pageList;

}