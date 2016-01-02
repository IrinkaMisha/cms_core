package rw.ktc.cms.entity;

import rw.ktc.cms.nodedata.Node;
import rw.ktc.cms.nodedata.NodeImpl;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "menuwrapper")
@AttributeOverrides({
        @AttributeOverride(name="id_usercreator", column=@Column(name="id_usercreator")),
        @AttributeOverride(name="id_usercorrector", column=@Column(name="id_usercorrector")),
        @AttributeOverride(name="datecreate", column=@Column(name="datecreate")),
        @AttributeOverride(name="datecorrect", column=@Column(name="datecorrect"))
})
public class MenuWrapper extends NodeImpl implements Node{
    private String name;

    @OneToMany(targetEntity=CmsMenuItem.class, fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CmsMenuItem> menuItems= new ArrayList<>();

    public MenuWrapper(){
    }

    public List<CmsMenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<CmsMenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public MenuWrapper(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MenuWrapper{" +
                "name='" + name + '\'' +
                ", menuItems=" + menuItems +
                '}';
    }
}
