package rw.ktc.cms.dao;

import rw.ktc.cms.entity.MenuWrapper;
import rw.ktc.cms.nodedata.service.NodeService;

/**
 * Created by dima on 19.05.2015.
 */
public interface IMenuWrapperDAO<T extends MenuWrapper,ID extends Long> extends NodeService<T,ID> {
//    MenuWrapper save(MenuWrapper menuWrapper, Node node);
    MenuWrapper getByName(String name);

}
