package by.imix.cms.dao;


import by.imix.cms.entity.Role;
import by.imix.cms.nodedata.Node;
import by.imix.cms.nodedata.service.NodeService;
import by.imix.cms.web.vo.RoleForm;

public interface IRoleDAO<T extends Role,ID extends Number> extends NodeService<T,ID> {

    T getFullRoleById(ID id); // по хорошему нах не нужен так как есть getById
    T getFullRoleByName(String name);
    T loadFullObject(T role);
//    List<T> getAll();
    T saveOrUpdateRole(T role, Node whoChanged);
//    T updateRole(T role, Node whoChanged);
    void deleteRoleById(ID id_role);

// нах они ?
//    List<T> getRoleByExample(Role role);
//    List<T> getRoleByExample(Role role, String nameOrder, Boolean asc, String... excludeProperty);

    ID createOrUpdateRole(RoleForm form, Node whoChanged);
}