package by.imix.cms.dao.impl;

import by.imix.cms.nodedata.service.hib.NodeServiceGeneric;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import by.imix.cms.entity.MenuWrapper;
import by.imix.cms.dao.IMenuWrapperDAO;

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
