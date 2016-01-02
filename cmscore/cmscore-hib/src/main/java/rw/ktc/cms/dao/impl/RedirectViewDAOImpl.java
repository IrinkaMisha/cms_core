package rw.ktc.cms.dao.impl;

import rw.ktc.cms.redirect.RedirectView;
import rw.ktc.cms.redirect.RedirectViewService;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ktc.cms.nodedata.Node;
import rw.ktc.cms.nodedata.service.hib.NodeServiceGeneric;

import java.util.List;

/**
 * Created by miha on 31.10.2014.
 */
@Service("redirectviewservicehib")
@Transactional(readOnly = true)
public class RedirectViewDAOImpl extends NodeServiceGeneric<Node,Long> implements RedirectViewService {
    private static final Logger logger = LoggerFactory.getLogger(RedirectViewDAOImpl.class);
    private List<RedirectView> redirectViewList;

    public List<RedirectView> getRedirectAll() {
        logger.debug("Вызов getRedirectAll");
        List<RedirectView> list = getSession().createSQLQuery("SELECT id_node as id_nodeView, value as url FROM node_property WHERE keyt LIKE '%redirectUrl%'").
                    addScalar("url").
                    addScalar("id_nodeView", StandardBasicTypes.LONG).
                    setResultTransformer(Transformers.aliasToBean(RedirectView.class)).list();
        return list;
    }


    public RedirectView checkRedirect(String urlR) {
        logger.debug("Вызов checkRedirect");
        if(redirectViewList==null){
            updateRedirectList();
        }
        RedirectView rw=null;
        if(redirectViewList!=null && redirectViewList.size()>0) {
            for (RedirectView rwch : redirectViewList) {
                if (rwch.checkURL(urlR)) {
                    rw = rwch;
                    break;
                }
            }
        }
        return rw;
    }

    public void updateRedirectList() {
        redirectViewList=getRedirectAll();
    }
}
