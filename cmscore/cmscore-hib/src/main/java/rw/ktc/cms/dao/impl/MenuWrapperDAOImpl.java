package rw.ktc.cms.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import rw.ktc.cms.entity.MenuWrapper;
import rw.ktc.cms.dao.IMenuWrapperDAO;
import rw.ktc.cms.nodedata.service.hib.NodeServiceGeneric;

/**
 * Created by dima on 07.05.2015.
 */
@Service
public class MenuWrapperDAOImpl extends NodeServiceGeneric<MenuWrapper,Long> implements IMenuWrapperDAO<MenuWrapper,Long> {



    @Override
    public MenuWrapper getByName(String name) {
        Criteria cr = getSession().createCriteria(MenuWrapper.class).add(Restrictions.eq("name", name));
        return  (MenuWrapper)cr.uniqueResult();
    }

}
