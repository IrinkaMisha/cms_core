package by.imix.cms.dao;

import by.imix.cms.entity.User;
import by.imix.cms.nodedata.Node;
import by.imix.cms.nodedata.service.HistoryNodeService;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 19.11.13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public interface IUserDAO<T extends User,ID extends Number> extends HistoryNodeService<T, ID> {

    T getById(ID id);

    T getUserByNameAndLogin(String name,String login);

    T createUser(T user, Node whoCreated);

    T getUserByName(String name);

    T loadFullObject(T user);

    /**
     *
     * @param userToDelete - юзер который будет удален
     * @param whoRemoves - кто его удалил
     */
    void removeUser(T userToDelete, Node whoRemoves);

    @Override
    T saveOrUpdateNode(T node, Node generatingNode);
}