package by.imix.cms.remove;

import by.imix.cms.isitnecessary.menu.MenuItem;
import by.imix.cms.nodedata.HistoryNode;
import by.imix.cms.nodedata.HistoryNodeImpl;
import by.imix.cms.nodedata.Node;
import by.imix.cms.nodedata.NodeProperty;
import by.imix.cms.web.security.UserWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import by.imix.cms.isitnecessary.menu.Menu;
import by.imix.cms.isitnecessary.menu.MenuNodeService;
import by.imix.cms.nodedata.service.HistoryNodeService;
import by.imix.cms.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 10.12.13
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
@Transactional(readOnly = true)
@Deprecated
public final class MenuNodeServiceImpl implements MenuNodeService{
    private static final Logger logger = LoggerFactory.getLogger(MenuNodeServiceImpl.class);

    @Autowired
    @Qualifier("nodedefaulthistservice")
    private HistoryNodeService nodehibservice;

    public MenuNodeServiceImpl(){}

    public MenuNodeServiceImpl(HistoryNodeService nodehibservice) {
        this.nodehibservice = nodehibservice;
    }

    public List<Menu> getAllMenu(){
        List<Node> lmndh= nodehibservice.getAllNodeFromType("menusight");
        List<Menu> lm=new ArrayList<Menu>();
        if(lmndh.size()>0){
            for(Node ndh:lmndh){
                Menu  m=null;
                try {
                    m=new Menu(buildMenu(nodehibservice.getSinglePropertysValue(ndh, "content").getValue()));
                    try {
                        m.setTitle(nodehibservice.getSinglePropertysValue(ndh, "title").getValue());
                    } catch (NullPointerException e) {
                        logger.warn("Error property title in NodeHist " + e.getMessage());
                    }
                    try {
                        m.setDescription(nodehibservice.getSinglePropertysValue(ndh, "description").getValue());
                    } catch (NullPointerException e) {
                        logger.warn("Error property description in NodeHist " + e.getMessage());
                    }
                    m.setId(ndh.getId());
                    lm.add(m);
                } catch (NullPointerException e) {
                    logger.error("Error property content in NodeHist " + e.getMessage());
                }
            }
        }
        return lm;
    }

    //todo дописать
    @Transactional
    public void createMenu(Node node, String name){
        Menu m=new Menu(buildMenu(""));
        HistoryNode ndh=new HistoryNodeImpl(node);
    }

    @Transactional
    public void saveMenu(Long node_id, Menu menuForm) {
        saveMenu(nodehibservice.getNodeById(User.class, node_id),menuForm);
    }

    @Transactional
    public void saveMenu(Node node, Menu menuForm) {
        HistoryNode ndh=null;
        if(menuForm.getId()!=null){
            ndh= (HistoryNode) nodehibservice.getNodeById(HistoryNode.class, menuForm.getId());
            if(ndh==null || nodehibservice.getPropertysValue(ndh, "title").size()==0){
                return;//это не меню пользователь кинуть нас хочет
            }
            ((NodeProperty) nodehibservice.getPropertysValue(ndh, "title").get(0)).setValue(menuForm.getTitle());
            ((NodeProperty) nodehibservice.getPropertysValue(ndh, "content").get(0)).setValue(menuForm.getDescription());
        }else{
            ndh=new HistoryNodeImpl(node);
            ndh.getNodeProperties().add(new NodeProperty("type","menusight"));
            ndh.getNodeProperties().add(new NodeProperty("title",menuForm.getTitle()));
            ndh.getNodeProperties().add(new NodeProperty("content",menuForm.getDescription()));
        }

        nodehibservice.saveOrUpdateNode(ndh, node);
    }

    public Menu getMenuById(Long id) {
        Node ndh= (Node) nodehibservice.getById(id, false);
        return getMenuByNode(ndh);
    }

    public Menu getMenuByNode(Node ndh) {
        if(ndh==null || nodehibservice.getSinglePropertysValue(ndh, "title")==null){
            return null; //такого меню нету.
        }

        Menu m=null;
        try {
            m=new Menu(buildMenu(nodehibservice.getSinglePropertysValue(ndh, "content").getValue()));
            try {
                m.setTitle(nodehibservice.getSinglePropertysValue(ndh, "title").getValue());
            } catch (NullPointerException e) {
                logger.warn("Error property title in NodeHist " + e.getMessage());
            }
            try {
                m.setDescription(nodehibservice.getSinglePropertysValue(ndh, "description").getValue());
            } catch (NullPointerException e) {
                logger.warn("Error property description in NodeHist " + e.getMessage());
            }
            m.setId(ndh.getId());
        } catch (NullPointerException e) {
            logger.error("Error property content in NodeHist " + e.getMessage());
        }
        return m;
    }

    private MenuItem buildMenu(String json) {
        return null;
    }

    @Transactional
    public boolean removeMenu(Long idMenu) {
        HistoryNode ndh=(HistoryNode) nodehibservice.getNodeById(HistoryNode.class, idMenu);
        if (!((NodeProperty) nodehibservice.getPropertysValue(ndh, "type").get(0)).getValue().equals("menusight")) {
            return false; //такого меню нету либо это не меню нас кидают.
        } else if (ndh == null || nodehibservice.getPropertysValue(ndh, "type").size() == 0) {
            return false; //такого меню нету либо это не меню нас кидают.
        }
        nodehibservice.removeNode(ndh);
        return true;
    }

    //todo new Long(2)
    public User getWebUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object user=auth.getPrincipal();
        if(user instanceof String){
            return (User) nodehibservice.getNodeById(HistoryNode.class, new Long(2));
        }
        return ((UserWeb) auth.getPrincipal()).getUser();
    }
}
