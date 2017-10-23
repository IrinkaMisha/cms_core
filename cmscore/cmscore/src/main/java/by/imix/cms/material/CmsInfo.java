package by.imix.cms.material;

import by.imix.cms.material.parsetonode.NodeExt;
import by.imix.cms.nodedata.Node;
import by.imix.cms.nodedata.json.ViewFlag;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;

public class CmsInfo extends NodeExt implements Serializable {
    public static final String VIRT_CLASS_NAME = "clazzCmsInfo";

    private boolean adminCreated; // если админ уже был создан, больше он не будет создаваться
    private boolean rolesCreated; // для создания ролей по-умолчанию

    public CmsInfo(Node node) {
        super(node);
    }

    @Override
    public String getType() {
        return VIRT_CLASS_NAME;
    }
//    public static final String NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY = "credential";

    private String name="CmsSystem";

    @JsonView(ViewFlag.NodeBriefly.class)
    public String getName() {
        return name;
    }


    public boolean isAdminCreated() {
        return adminCreated;
    }

    public void setAdminCreated(boolean adminCreated) {
        this.adminCreated = adminCreated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRolesCreated() {
        return rolesCreated;
    }

    public void setRolesCreated(boolean rolesCreated) {
        this.rolesCreated = rolesCreated;
    }

    @Override
    public String toString() {
        return "CmsInfo{" +
                "adminCreated=" + adminCreated +
                ", rolesCreated=" + rolesCreated +
                ", name='" + name + '\'' +
                '}';
    }
}
