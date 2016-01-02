package rw.ktc.cms.model;

import rw.ktc.cms.web.dynamiccontent.DynamicContent;
import rw.ktc.cms.nodedata.Node;

import java.util.List;
import java.util.Map;

public interface IDynamicContentService {

    void setPathToWebFolder(String path);

    Node saveChanges(DynamicContent dynamicContent, Node whoGenerated);

    DynamicContent load(Long id);

    DynamicContent createNew(DynamicContent dc, Node whoGenerated);

    /**
     * Метод получения имен переменных из шаблона контейнера. Под этими именами будут вставлены новые блоки(контейнеры)
     */
    List<String> getNamesBlocks(Long idCont);

    String getViewNameTemplate(Node node);
    String getViewNameTemplate(String idTemplate);

    List<DynamicContent> getAllDynamicContent();

    List<DynamicContent> getAllDynamicContentForType(String type);

//    List<DynamicContent> getAllDynamicContentForType(String type);

    List<String> namesTemplates();

    List<Map> getAllTemplatesInfo();

}
