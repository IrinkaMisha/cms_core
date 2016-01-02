package rw.ktc.cms.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import rw.ktc.cms.Const;
import rw.ktc.cms.web.dynamiccontent.ErrorForResponse;
import rw.ktc.cms.nodedata.service.NodeService;
import rw.ktc.cms.redirect.RedirectViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rw.ktc.cms.web.dynamiccontent.Container;
import rw.ktc.cms.web.dynamiccontent.DynamicContent;
import rw.ktc.cms.nodedata.Node;
import rw.ktc.cms.nodedata.NodeImpl;
import rw.ktc.cms.nodedata.NodeProperty;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DynamicContentService implements IDynamicContentService {
    private static final Logger logger = LoggerFactory.getLogger(DynamicContentService.class);

    public final static String PREFIX_TEMPLATE_FILE = "template_id_";
    public final static String PATH_TO_TEMPLATE = "templates" + File.separator + "dynamictemplates" + File.separator + PREFIX_TEMPLATE_FILE;
    public final static String PATH_TO_DIR_TEMPLATE = "templates" + File.separator + "dynamictemplates";
    public final static String WEB_RELATIVE_PATH_TO_DIR_TEMPLATE = "WEB-INF" + File.separator + "views" + File.separator + PATH_TO_DIR_TEMPLATE;
    public final static String NAME_DEFAULT_TEMPLATE_FOR_PAGE = "root";
    public final static String NAME_DEFAULT_TEMPLATE_FOR_CONTAINER = "7050";

    private String pathToWebFolder;

    @Autowired
    private RedirectViewService redirectViewService;

    @Autowired
    @Qualifier("nodeServiceImpl")
    private NodeService<Node, Long> nodeService ;

    private List<String> filesNames;
    private List<String> templatesNames;

    @Override
    public void setPathToWebFolder(String path) {
        this.pathToWebFolder = path;
        filesNames = filesNames(path + WEB_RELATIVE_PATH_TO_DIR_TEMPLATE, PREFIX_TEMPLATE_FILE + "*.jsp");
    }

    /**
     * Метод возвращает полный путь на диске к шаблону
     *
     * @return
     */
    public String getFullPathTemplate(String nameTemplate) {
        return getPathToWebFolder() + File.separator + "WEB-INF" + File.separator + Const.PATH_TO_JSP_VIEWS + File.separator + PATH_TO_TEMPLATE + nameTemplate + ".jsp";
    }

    /**
     * Получение файла в виде строки
     *
     * @param fullPathToFile
     * @return
     */
    private String getFileTemplate(String fullPathToFile) {
        Path filePath = Paths.get(fullPathToFile);
        if (Files.exists(filePath)) {
            try {
                byte[] bytes = Files.readAllBytes(filePath);
                String text = new String(bytes, StandardCharsets.UTF_8);
                return text;
            } catch (IOException e) {
                logger.error("Ошибка открытия шаблона в файловой системе:{}", e);
            }
        }
        return null;
    }

    /**
     * Метод возвращает информацию о всех шаблонах. Предназначен для использования в javaScript-е
     * - поэтому и возвращает список карт - так как при json формате они выглядят как обычные объекты
     *
     * @return
     */
    public List<Map> getAllTemplatesInfo() {
        List<String> namesTemplates = namesTemplates();
        List<Map> templates = new ArrayList<>();
        for (String name : namesTemplates) {
            Map<String, Object> template = new HashMap<>();
            template.put("name", name);
            String fullPath = getFullPathTemplate(name);
            String fileContent = getFileTemplate(fullPath);
            List<String> blocks = getConteinerFromString(fileContent);
            template.put("blocks", blocks);
            String type = getTypeConteiner(fileContent);
            template.put("type", type);
            templates.add(template);
        }
        return templates;
    }
//    /**
//     * String typet - "page" или "container"
//     * см выше
//     * @param typet
//     * @return
//     */
//    @Override
//    public List<NodeProperty> getAllTemplates(String typet) {
//        List<Node> listNode = contentService.getAllNodeFromPrKey("typeTemplate", typet);
//        List<NodeProperty> listTName = new ArrayList<NodeProperty>();
//        for (Node n : listNode) {
//            n = contentService.loadFullObject(n);
//            listTName.addAll(n.getPropertysValue(n, "name"));
//        }
//        return listTName;
//    }

    /**
     * получение имен(ид) шаблонов
     *
     * @return
     */
    public List<String> namesTemplates() {
        String path = getPathToWebFolder() + WEB_RELATIVE_PATH_TO_DIR_TEMPLATE;
        List<String> namesTemplates = new ArrayList<>();
        for (String nameTemplate : filesNames(path, PREFIX_TEMPLATE_FILE + "*.jsp")) {
            nameTemplate = nameTemplate.replace(PREFIX_TEMPLATE_FILE, "");
            nameTemplate = nameTemplate.replace(".jsp", "");
            namesTemplates.add(nameTemplate);
        }
        return namesTemplates;
    }

    /**
     * метод возвращает имена файлов в указанной директории согласно патерну для поиска
     *
     * @param directory
     * @param pattern
     * @return
     */
    public List<String> filesNames(String directory, String pattern) {
        List<Path> files = filesList(directory, pattern);
        List<String> fileNames = new ArrayList<>();
        for (Path path : files) {
            fileNames.add(path.getFileName().toString());
        }
        return fileNames;
    }

    /**
     * метод возвращает все файлы в указанной директории согласно патерну для поиска
     *
     * @param directory
     * @param pattern
     * @return
     */
    public List<Path> filesList(String directory, String pattern) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        List<Path> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                if (path != null && matcher.matches(path.getFileName())) {
                    fileNames.add(path);
                }
            }
        } catch (IOException ex) {

        }
        return fileNames;
    }

    public String getPathToWebFolder() {
        return pathToWebFolder;
    }

    public Node saveChanges(DynamicContent dynamicContent, Node whoGenerated) {
        try {
            if (!dynamicContent.isClone()) {
                if (dynamicContent.getId() != null) {
                    Node node = nodeService.getFullNodeById(dynamicContent.getNode().getId());
                    dynamicContent.setNode(node);
                }
                dynamicContent.fillObject();//заполняем ноду новыми данными хз что за данные


                //Удаляем все свойства начинающиеся на  cont[ , чтобы далеее заполнить свойства из cont
                List<NodeProperty> lnpRem = new ArrayList<NodeProperty>();
                for (NodeProperty np : dynamicContent.getNode().getNodeProperties()) {
                    if (np.getKeyt().indexOf("cont[") == 0) {
                        lnpRem.add(np);
                    }
                    if (np.getKeyt().indexOf("redirectUrl[") == 0) {
                        lnpRem.add(np);
                    }
                }
                List<NodeProperty> lnpNew = dynamicContent.getNode().getNodeProperties();
                lnpNew.removeAll(lnpRem);
                dynamicContent.getNode().setNodeProperties(lnpNew);
            } else {
                ((NodeImpl) dynamicContent.getNode()).setId(null);
                dynamicContent.fillObject();//заполняем ноду новыми данными
            }
            //Добавляем новые свойства контейнеров
            if (dynamicContent.getCont() != null) {
                for (int i = 0; i < dynamicContent.getCont().size(); i++) {
                    Container cont = dynamicContent.getCont().get(i);
                    if (null != cont && null != cont.getContent() && !"".equals(cont.getContent().trim()) && !("container".equals(cont.getType()) && !isLong(cont.getContent())) ) {
                        String namecont = "cont[" + i + "].";
                        dynamicContent.getNode().addOnlyOneProperty(namecont + "name", cont.getName());
                        dynamicContent.getNode().addOnlyOneProperty(namecont + "type", cont.getType());
                        dynamicContent.getNode().addOnlyOneProperty(namecont + "content", cont.getContent());
                    }
                }
            }
            if (dynamicContent.getRedirectUrl() != null) {
                for (int i = 0; i < dynamicContent.getRedirectUrl().size(); i++) {
                    String url = dynamicContent.getRedirectUrl().get(i);
                    String nameRU = "redirectUrl[" + i + "]";
                    dynamicContent.getNode().addOnlyOneProperty(nameRU, url);
                }
            }
            Node node = nodeService.saveOrUpdateNode(dynamicContent.getNode(), whoGenerated);
            redirectViewService.updateRedirectList();
            return node;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
//            e.printStackTrace();
        }
        return null;
    }

    public DynamicContent load(Long id) {
        Node node = nodeService.getFullNodeById(id);
        if (node == null) {
            return null;
        }
        DynamicContent dynContent = new DynamicContent(node);
        addConteiners(dynContent);
        return dynContent;
    }

    @Override
    public DynamicContent createNew(DynamicContent dc, Node whoGenerated) {
        dc.setId(null);
        if (null == dc.getTypeDynCont() || "".equals(dc.getTypeDynCont())) {
            dc.setTypeDynCont("container");
        }
        if (null == dc.getShablonName() || "".equals(dc.getShablonName())) {
            if ("container".equals(dc.getTypeDynCont())) {
                dc.setShablonName(NAME_DEFAULT_TEMPLATE_FOR_CONTAINER);
            }
            if ("page".equals(dc.getTypeDynCont())) {
                dc.setShablonName(NAME_DEFAULT_TEMPLATE_FOR_PAGE);
            }
        }
        Node node = saveChanges(dc, whoGenerated);
        dc = load(node.getId());
        return dc;
    }

    //метод для добавления иерархии контейнеров
    private void addConteiners(DynamicContent page) {
        invertNodePToCont(page);
        addPodContainers(page);
    }

    //метод для добавления рекурсивного добавления контейнеров в контейнеры
    private void addPodContainers(DynamicContent page) {
        Long id;
        for (Container c : page.getCont()) {
            if (c.getType().equals("container")) {
                id = getIdContainerFromString(c.getContent());
//                id = new Long(c.getContent());
                Node nodeB = nodeService.getFullNodeById(id);
                DynamicContent pageCh = new DynamicContent(nodeB);
                pageCh.setContent(PATH_TO_TEMPLATE + pageCh.getShablonName());
                addConteiners(pageCh);
                page.addDynamicContent(pageCh);

            }
        }
    }

    private static Long getIdContainerFromString(String string) {
        Long l = null;
        if (null == string) return null;
        try {
            l = Long.parseLong(string);
        } catch (NumberFormatException e) {
            logger.error(e.getLocalizedMessage());
        }
        return l;
    }

    private void invertNodePToCont(DynamicContent page) {
        Map<String, Container> mapCont = getMapNodeName(page.getNode());
        for (NodeProperty np : page.getNode().getNodeProperties()) {
            if (np.getKeyt().startsWith("cont[")) {
                String numCont = np.getKeyt().substring(np.getKeyt().indexOf("[") + 1, np.getKeyt().indexOf("]"));
                Container cont = mapCont.get(numCont);
                String propN = np.getKeyt().substring(np.getKeyt().lastIndexOf(".") + 1, np.getKeyt().length());
                if (propN.equals("name")) {
                    cont.setName(np.getValue()); // зачем ??
                } else {
                    if (propN.equals("type")) {
                        cont.setType(np.getValue());
                    } else {
                        if (propN.equals("content")) {
                            cont.setContent(np.getValue());
                        }
                    }
                }
            }
        }
        List<Container> containers = new ArrayList<Container>(mapCont.values());
        page.setCont(containers);
    }

    private Map<String, Container> getMapNodeName(Node node) {
        Map<String, Container> lNP = new HashMap<String, Container>();
        for (NodeProperty np : node.getNodeProperties()) {
            if (np.getKeyt().startsWith("cont[") && np.getKeyt().endsWith(".name")) {
                Container c = new Container();
                c.setName(np.getValue());
                String num = np.getKeyt().substring(np.getKeyt().indexOf("[") + 1, np.getKeyt().indexOf("]"));
                lNP.put(num, c);
            }
        }
        return lNP;
    }

    public List<String> getNamesBlocks(Long idCont) {
        Node node = nodeService.getFullNodeById(idCont);
        ErrorForResponse error = new ErrorForResponse();
        if (null != node) {
            String nameTemplate = getNameTemplateByShablonNameNode(node);
            String fullPath = getFullPathTemplate(nameTemplate);
            String file = getFileTemplate(fullPath);
            // вариант 1
            if (null != file) {
                return getConteinerFromString(file);
            }
            // вариант 2
            List<NodeProperty> properties = node.getPropertysValue(node, "file");// имя поля из сущности Node
            if (properties.size() != 0 && null != properties.get(0).getValue()) {
                return getConteinerFromString(properties.get(0).getValue()); // почему берем именно первый ??
            }
        }
        error.message = "Контейнера с данным ид не найдено";
        return Arrays.asList(error.toString());
    }

    /**
     * Метод возвращает имя используемое в ModelAndView
     *
     * @param node
     * @return
     */
    public String getViewNameTemplate(Node node) {
        String name = getNameTemplateByShablonNameNode(node);
        return getViewNameTemplate(name);
    }

    public String getViewNameTemplate(String idTemplate) {
        return PATH_TO_TEMPLATE + idTemplate;
    }

    public String getNameTemplateByShablonNameNode(Node node) {
        List<NodeProperty> properties = node.getPropertysValue(node, "shablonName"); // имя поля из сущности DynamicContent
        if (properties.size() == 0 || null == properties.get(0).getValue() || properties.get(0).getValue().length() == 0) {
            return NAME_DEFAULT_TEMPLATE_FOR_PAGE;
        } else {
            String shablonName = properties.get(0).getValue();
            return shablonName;
        }
    }

    private static boolean isLong(String numberStr) {
        try {
            Long i = Long.parseLong(numberStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private List<String> getConteinerFromString(String strTextFile) {
        Pattern p = Pattern.compile("<\\!--#begin.+#-->", Pattern.MULTILINE);
        Matcher m = p.matcher(strTextFile);
        List<String> str = new ArrayList<String>();
        while (m.find()) {
            String stl = m.group();
            str.add(stl.substring(10, stl.length() - 4).trim());
        }
        return str;
    }

    /**
     * Если в файле (строке) находятся блоки html и body
     *
     * @param strTextFile
     * @return
     */
    private String getTypeConteiner(String strTextFile) {
        Matcher m = Pattern.compile("<head|<body").matcher(strTextFile);
        if (m.find()) {
            return "page";
        } else {
            return "container";
        }
    }

    @Override
    public List<DynamicContent> getAllDynamicContent() {
        List<Node> listNode = nodeService.getAllNodeFromPrKey("type", DynamicContent.TYPEDC);
        List<DynamicContent> ldynCont = new ArrayList();
        for (Node n : listNode) {
            n = nodeService.loadFullObject(n);
            DynamicContent dc = new DynamicContent(n);
//            invertNodePToCont(dc);
            ldynCont.add(dc);
        }
        return ldynCont;
    }

    /**
     * String typet - "page" или "container"
     *
     * @param type
     * @return
     */
    @Override
    public List<DynamicContent> getAllDynamicContentForType(String type) {
        if (!checkSupportedTypeContent(type)) {
            return new ArrayList<>();
        }
        List<DynamicContent> ldynCont = getAllDynamicContent();
        List<DynamicContent> list = new ArrayList<>();
        for (DynamicContent dc : ldynCont) {
            if (dc.getTypeDynCont().equals(type)) {
                list.add(dc);
            }
        }
        return list;
    }



    boolean checkSupportedTypeContent(String type) {
        for (String entry : (new DynamicContent()).getTypeAtribute().values()) { // берем типы
            if (entry.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public RedirectViewService getRedirectViewService() {
        return redirectViewService;
    }

    public void setRedirectViewService(RedirectViewService redirectViewService) {
        this.redirectViewService = redirectViewService;
    }

    public NodeService<Node, Long> getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService<Node, Long> nodeService) {
        this.nodeService = nodeService;
    }

    public List<String> getFilesNames() {
        return filesNames;
    }

    public void setFilesNames(List<String> filesNames) {
        this.filesNames = filesNames;
    }

    public List<String> getTemplatesNames() {
        return templatesNames;
    }

    public void setTemplatesNames(List<String> templatesNames) {
        this.templatesNames = templatesNames;
    }
}
