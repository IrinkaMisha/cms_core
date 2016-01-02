package rw.ktc.cms.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rw.ktc.cms.model.ICmsInfoDAO;
import rw.ktc.cms.material.CmsInfo;
import rw.ktc.cms.nodedata.Node;
import rw.ktc.cms.nodedata.NodeImpl;
import rw.ktc.cms.nodedata.service.NodeService;

/**
 * Created by dima on 06.08.2015.
 */
@Component
@Transactional
public final class CmsInfoDaoImpl implements ICmsInfoDAO {

    private static final String KEY_VIRT_CLASS_NAME = "virtClassName";
    private static int COUNT_INIT = 0;

    @Autowired
    @Qualifier("nodeServiceImpl")
    private NodeService nodeService;

    @Autowired
    @Qualifier("cmsSessionFactory")
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }


    private CmsInfo cmsInfo;

    private void createNew() {
        Node node = new NodeImpl();
        node.addOnlyOneProperty(KEY_VIRT_CLASS_NAME, CmsInfo.VIRT_CLASS_NAME);
        nodeService.saveOrUpdate(node);
//        getSession().saveOrUpdate(node);
    }

    @Override
    public CmsInfo getInstanceFromDataBase() {
        if (null == cmsInfo) {
            COUNT_INIT++;
            Criteria crit = getSession().createCriteria(NodeImpl.class);
            Criteria property = crit.createCriteria("nodeProperties");
            Criterion key = Restrictions.eq("keyt", KEY_VIRT_CLASS_NAME);
            Criterion value = Restrictions.eq("value", CmsInfo.VIRT_CLASS_NAME);
            property.add(Restrictions.and(key, value));
            crit.setMaxResults(1);
            Node node = (Node) crit.uniqueResult();
//            node = nodeService.loadFullObject(node);
            if (null != node) {
                cmsInfo = new CmsInfo(node);
            } else {
                if (COUNT_INIT > 2) throw new ExceptionInInitializerError("ошибка инициализации");
                createNew();
                return getInstanceFromDataBase();
            }
        }
        return cmsInfo;
    }

    @Override
    public void update() {
        cmsInfo.fillObject();
        nodeService.saveOrUpdate(cmsInfo.getNode());
//        getSession().saveOrUpdate(cmsInfo.getNode());
    }


    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

}
