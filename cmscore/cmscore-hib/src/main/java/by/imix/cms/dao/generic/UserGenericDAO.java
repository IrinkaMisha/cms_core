package by.imix.cms.dao.generic;

import by.imix.cms.dao.IUserDAO;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import by.imix.cms.dao.IRoleDAO;
import by.imix.cms.entity.Role;
import by.imix.cms.entity.User;
import by.imix.cms.nodedata.Node;
import by.imix.cms.nodedata.service.hib.HistNodeServiceGeneric;

@Transactional(readOnly = true)
public class UserGenericDAO<T extends User, ID extends Number> extends HistNodeServiceGeneric<T, ID> implements IUserDAO<T, ID> {
    private static final Logger log = LoggerFactory.getLogger(UserGenericDAO.class);


    @Autowired
    private IRoleDAO daoRoleManagerService;

    @Override
    public T getById(ID id) {
        return getById(id, false);
    }

    public T getUserByNameAndLogin(String login, String password) {
        Query q = getSession().createQuery("FROM " + getPersistentClass().getName() + " WHERE name=:name AND password=:password").setString("name", login).setString("password", password);
        return (T) q.uniqueResult();
    }

    @Transactional
    public T createUser(T user, Node whoCreated) {
        return saveOrUpdateNode(user, whoCreated);
    }

    public T getUserByName(String name) {
        Criterion rest1 = Restrictions.eq("historical", false);
        Criterion rest2 = Restrictions.eq("removed", false);
        Criterion rest3 = Restrictions.eq("name", name);
        Criteria cr = getSession().createCriteria(getPersistentClass()).add(Restrictions.and(rest1, rest2, rest3));
        T user = (T) cr.uniqueResult();
//        T us = (T) getSession().createQuery("FROM " + .getName() + " u WHERE u.name=:name AND u.hystory=false AND u.removed=false").setString("name", name).uniqueResult();
        if (user != null) {
            Hibernate.initialize(user.getRoles());
            Hibernate.initialize(user.getNodeProperties());
        }
        return user;
    }

    @Override
    public T loadFullObject(T user) {
        super.loadFullObject(user);
        Hibernate.initialize(user.getRoles());
        Hibernate.initialize(user.getHystParent());
        Hibernate.initialize(user.getHystPremParent());
        for (Role role : user.getRoles()) {
            daoRoleManagerService.loadFullObject(role);
        }
        return user;
    }

    @Override
    @Transactional
    public T saveOrUpdateNode(T node, Node generatingNode) {
        if (node.getId() == null || node.getId() == 0){
            User check = getUserByName(node.getName());
            if (null != check){
                throw new IllegalArgumentException("Юзер с таким именем уже существует");
            }
        }
        return super.saveOrUpdateNode(node, generatingNode);
    }

    @Transactional
    public void removeUser(T userToDelete, Node whoRemoves) {
        userToDelete.setRemoved(true);
        saveOrUpdateNode(userToDelete, whoRemoves);
    }

}
