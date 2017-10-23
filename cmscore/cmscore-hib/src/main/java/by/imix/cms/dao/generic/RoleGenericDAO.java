package by.imix.cms.dao.generic;

import by.imix.cms.nodedata.service.hib.NodeServiceGeneric;
import by.imix.cms.web.vo.RoleForm;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import by.imix.cms.dao.IRoleDAO;
import by.imix.cms.entity.Role;
import by.imix.cms.nodedata.Node;

@Transactional
public class RoleGenericDAO<T extends Role, ID extends Number> extends NodeServiceGeneric<T, ID> implements IRoleDAO<T, ID> {
    private static final Logger log = LoggerFactory.getLogger(RoleGenericDAO.class);

    public T getFullRoleByName(String name) {
        T t = (T) getSession().createQuery("FROM " + getPersistentClass().getName() + " WHERE name=:name").setString("name", name).uniqueResult();
        if (null != t ) loadFullObject(t);
        return t;
    }

    public T getFullRoleById(ID id) {
//        T t = getById(id, false);
        T t = (T) getSession().load(getPersistentClass(), id);
        if (null != t ) loadFullObject(t);
        return t;
    }

    @Override
    public void deleteRoleById(ID id_role) {
        try {
            getSession().createSQLQuery("DELETE FROM user_role WHERE roles_id=:id_role").setLong("id_role", (Long) id_role).executeUpdate();
            getSession().delete(getFullRoleById(id_role));
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public T saveOrUpdateRole(Role roleR, Node whoChanged) {
        return saveOrUpdateNode((T) roleR, whoChanged);
    }

    @Override
    public ID createOrUpdateRole(RoleForm form, Node whoChanged) {
        if (null != form.id && form.id > 0) { // update
            T role = null;
            role = getFullRoleById((ID) form.id);
            role = replaceProperties(role, Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY, form.permissions);
            saveOrUpdateNode(role, whoChanged);
            return (ID) role.getId();
        } else {
            if ("".equals(form.name)) {
                return null;
            }
            T r = (T) new Role();
            r.setName(form.name);
            r = addProperties(r, Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY, form.permissions);
            ID id = createNode(r, whoChanged);
            return id;
        }
    }

}
