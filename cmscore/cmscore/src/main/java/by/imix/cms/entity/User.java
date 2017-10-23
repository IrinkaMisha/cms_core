package by.imix.cms.entity;

import by.imix.cms.nodedata.*;
import by.imix.cms.nodedata.json.ViewFlag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
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
@Table(name = "users")
@AttributeOverrides({
        @AttributeOverride(name = "id_usercreator", column = @Column(name = "id_usercreator")),
        @AttributeOverride(name = "id_usercorrector", column = @Column(name = "id_usercorrector")),
        @AttributeOverride(name = "datecreate", column = @Column(name = "datecreate")),
        @AttributeOverride(name = "datecorrect", column = @Column(name = "datecorrect")),
        @AttributeOverride(name = "id_hystPremParent", column = @Column(name = "id_hystPremParent")),
        @AttributeOverride(name = "id_hystParent", column = @Column(name = "id_hystParent")),
        @AttributeOverride(name = "historical", column = @Column(name = "historical"))
})

public class User extends HistoryNodeImpl implements Serializable {
    @Column(name = "name")
    @JsonView(ViewFlag.UserBriefly.class)
    private String name;
    @JsonIgnore
    private String password;

    @JsonView(ViewFlag.UserBriefly.class)
    private Boolean active = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role")
    @JsonView(ViewFlag.UserWithRoles.class)
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String name, String password) {
        super(null);
        this.name = name;
        this.password = password;
    }

    public User(Node nodechanger, HistoryNode historyNode, String name, String password) {
        super(nodechanger, historyNode);
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;
        if (active != user.active) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (getRoles().size() != user.getRoles().size()) return false;
        for (Role role : user.getRoles()) {
            if (!isExistRole(role)) return false;
        }
        return true;
    }

    private boolean isExistRole(Role role) {
        for (Role r : getRoles()) {
            if (r.equals(role)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        User user = (User) super.clone();
        user.name = name;
        user.password = password;
        Set<Role> ListRoles = new HashSet<>();
        for (Role r : getRoles()) {
            Role r2 = (Role) r.clone();
            ListRoles.add(r2);
        }
        user.setRoles(ListRoles);
        return user;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "id='" + super.getId() + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    @JsonView(ViewFlag.UserFull.class)
    public NodeImpl getNodeCreator() {
        return super.getNodeCreator();
    }

    @Override
    @JsonView(ViewFlag.UserFull.class)
    public Node getNodeCorrector() {
        return super.getNodeCorrector();
    }

    @Override
    @JsonView(ViewFlag.UserWithRoles.class)
    public List<NodeProperty> getNodeProperties() {
        return super.getNodeProperties();
    }


}

