package rw.ktc.cms.web.vo;

import rw.ktc.cms.nodedata.NodeProperty;
import rw.ktc.cms.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleForm {
    public Long id;

    public String name;

    public List<String> permissions=new ArrayList<>();

    public RoleForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static RoleForm getInstanceFromRole(Role role){
        RoleForm form = new RoleForm();
        form.id = role.getId();
        form.name = role.getName();
        for(NodeProperty np:role.getNodeProperties()){
            if (np.getKeyt().equals(Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY)){
                form.permissions.add(np.getValue());
            }
        }
        return form;
    }

    public Role getInstance(){
        Role newRole=new Role();
        newRole.setName(this.name);
        if(permissions==null){
            return newRole;
        }
        for(String permission:permissions){
            newRole.getNodeProperties().add(new NodeProperty(Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY, permission));
        }
        return newRole;
    }

    // todo remove
    public Role changeRole(Role role){
        role.setName(this.getName());
        List<NodeProperty> props=role.getNodeProperties();
        List<NodeProperty> removeProps=new ArrayList<NodeProperty>();
        for(NodeProperty prop:props){
            if(prop.getKeyt().equals(Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY)){
                removeProps.add(prop);
            }
        }
        props.removeAll(removeProps);
        for(String cred:this.getPermissions()){
            props.add(new NodeProperty(Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY, cred));
        }
        return role;
    }

    @Override
    public String toString() {
        return "RoleForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
