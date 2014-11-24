package atlas.entity;

import atlas.entity.LabelContent;
import atlas.entity.Model;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-24T16:59:41")
@StaticMetamodel(Label.class)
public class Label_ { 

    public static volatile SingularAttribute<Label, Double> markZ;
    public static volatile SingularAttribute<Label, Double> markY;
    public static volatile SingularAttribute<Label, Double> labelX;
    public static volatile SingularAttribute<Label, Double> labelY;
    public static volatile SingularAttribute<Label, Double> markX;
    public static volatile SingularAttribute<Label, Double> labelZ;
    public static volatile ListAttribute<Label, LabelContent> labelContentList;
    public static volatile SingularAttribute<Label, Model> model;
    public static volatile SingularAttribute<Label, Integer> id;

}