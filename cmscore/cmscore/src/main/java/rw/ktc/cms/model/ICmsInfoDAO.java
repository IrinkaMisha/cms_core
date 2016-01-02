package rw.ktc.cms.model;


import rw.ktc.cms.material.CmsInfo;

public interface ICmsInfoDAO {

    CmsInfo getInstanceFromDataBase();

    void update();

}