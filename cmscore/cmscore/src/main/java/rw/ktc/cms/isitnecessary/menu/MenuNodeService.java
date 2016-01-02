package rw.ktc.cms.isitnecessary.menu;

import rw.ktc.cms.nodedata.Node;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 25.04.14
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public interface MenuNodeService {
    List<Menu> getAllMenu();
    void createMenu(Node node, String name);
    void saveMenu(Node node, Menu menuForm);
    void saveMenu(Long idNode, Menu menuForm);
    Menu getMenuById(Long id);
    Menu getMenuByNode(Node node);
    boolean removeMenu(Long idMenu);
}
