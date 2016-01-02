package rw.ktc.cms.entity;

import com.fasterxml.jackson.annotation.JsonView;
import rw.ktc.cms.nodedata.Node;
import rw.ktc.cms.nodedata.NodeImpl;
import rw.ktc.cms.nodedata.NodeProperty;
import rw.ktc.cms.nodedata.NodeState;
import rw.ktc.cms.nodedata.json.ViewFlag;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 23.10.13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "role")
//@PrimaryKeyJoinColumn(name = "id_node", referencedColumnName = "id")
@AttributeOverrides({
        @AttributeOverride(name="id_usercreator", column=@Column(name="id_usercreator")),
        @AttributeOverride(name="id_usercorrector", column=@Column(name="id_usercorrector")),
        @AttributeOverride(name="datecreate", column=@Column(name="datecreate")),
        @AttributeOverride(name="datecorrect", column=@Column(name="datecorrect"))
})

public class Role extends NodeImpl implements Serializable {

    public static final String NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY = "credential";

    private String name;

    public Role(){}

    public Role(String name) {
        this(null, name);
    }

    protected Role(Node nodechanger){
        super(nodechanger);
    }

    public Role(Node nodechanger,String name) {
        super(nodechanger);
        this.name = name;
    }

    @JsonView(ViewFlag.NodeBriefly.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        role.setId(null);
        if (name != null ? !name.equals(role.name) : role.name != null) return false;
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Role role = (Role) super.clone();
        role.name = name;
        return role;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    @JsonView(ViewFlag.RoleFull.class)
    public NodeImpl getNodeCreator() {
        return super.getNodeCreator();
    }

    @Override
    @JsonView(ViewFlag.RoleFull.class)
    public Node getNodeCorrector() {
        return super.getNodeCorrector();
    }

    @Override
    @JsonView(ViewFlag.RoleFull.class)
    public List<NodeProperty> getNodeProperties() {
        return super.getNodeProperties();
    }

    @Override
    @JsonView(ViewFlag.RoleFull.class)
    public Set<NodeState> getListStates() {
        return super.getListStates();
    }

    @Override
    @JsonView(ViewFlag.RoleFull.class)
    public Date getDateCreate() {
        return super.getDateCreate();
    }

    @Override
    @JsonView(ViewFlag.RoleFull.class)
    public Date getDateCorrect() {
        return super.getDateCorrect();
    }
}
