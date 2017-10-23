package by.imix.cms.model;


import by.imix.cms.material.CmsInfo;

public interface ICmsInfoDAO {

    CmsInfo getInstanceFromDataBase();

    void update();

}