package atlas.entity;

import atlas.entity.AtlasUser;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-17T17:53:28")
@StaticMetamodel(UserRole.class)
public class UserRole_ { 

    public static volatile ListAttribute<UserRole, AtlasUser> atlasUserList;
    public static volatile SingularAttribute<UserRole, String> role;
    public static volatile SingularAttribute<UserRole, Integer> id;

}