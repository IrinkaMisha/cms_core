package rw.ktc.cms.web.controller.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import rw.ktc.cms.dao.IRoleDAO;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;
import rw.ktc.cms.model.ICmsInfoDAO;
import rw.ktc.cms.nodedata.Node;
import rw.ktc.cms.web.form.ConteinerForSession;
import rw.ktc.cms.web.form.NewUserForm;
import rw.ktc.cms.web.security.CurrentUserWeb;
import rw.ktc.cms.web.security.UserWeb;
import rw.ktc.cms.web.image.FileUploaderIface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 19.11.13
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
@Controller("LoginController")
@SessionAttributes
@Transactional(readOnly = true)
public class LoginController{
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    //путь к вару на сервере
    private static String webRootPath;
    //путь к файлу аватарке
    private static String avatarPath;

    public static String getWebRootPath() {
        return webRootPath;
    }

    public static void setWebRootPath(String webRootPath) {
        LoginController.webRootPath = webRootPath;
    }

    public static String getAvatarPath() {
        return avatarPath;
    }

    public static void setAvatarPath(String avatarPath) {
        LoginController.avatarPath = avatarPath;
    }

    @Autowired
    private IUserDAO daoUser;
    @Autowired
    private IRoleDAO daoRole;

    @Autowired
    ICmsInfoDAO cmsInfoDAO;

    private ApplicationContext context;
    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @RequestMapping({"/cms.html"})
    public String index() {
//        uiModel.addAttribute("messageSource", messageSource);
//        if(httpSession.getAttribute("context")==null){
//            httpSession.setAttribute("context", context);
//        }
//        if(webRootPath==null){
//            webRootPath=httpSession.getServletContext().getRealPath("");
//            avatarPath=webRootPath+File.separator+"resources"+File.separator+"avataruser";
//        }
        return "startPage";
    }

//    @RequestMapping(value = "login.html")
//    public String login() throws IOException {
//        return "login";
//    }

//    @RequestMapping(value = "usogdp.html")
//    public String usogdp() throws IOException {
//        return "../../views/usogdpInterfaceStart";
//    }
//
//    @RequestMapping(value = "usermanager/authentication.html")
//    //public ModelAndView index(@CookieValue(required = false, value = "identity") String identity, Model uiModel) throws IOException {
//    public ModelAndView authentication(@RequestParam(value = "login", required = false) String login, @RequestParam(value = "password", required = false) String password, HttpSession httpSession) throws IOException {
//        logger.info("Логинимся");
//
//        User userS = null;
//
//        //по коду индентификации получаем id пользователя
//        if (login != null && password!=null) {
//            userS = daoUser.getUserByNameAndLogin(login, password);
//        }
//
//        if(userS!=null){
//            ConteinerForSession contS=new ConteinerForSession();
//            contS.setUser(userS);
//            contS.setAvatarPath(getFileNameAvatar(avatarPath,userS.getName()));
//            httpSession.setAttribute("userA", userS);
//            httpSession.setAttribute("contS", contS);
//        }
//
//        return new ModelAndView("startPage");
//    }

//    @RequestMapping(value = "usermanager/logout.html")
//    public ModelAndView logout(HttpSession httpSession) throws IOException {
//        httpSession.setAttribute("contS", null);
//        httpSession.setAttribute("userA", null);
//        return new ModelAndView("startPage");
//    }

//    @Secured("ROLE_AUTH_DATA")
//    @RequestMapping(value = "usermanager/gousercabinet.html")
//    public ModelAndView gousercabinet(NewUserForm userForm) {
//        ModelAndView mav=new ModelAndView("cabinetUser");
//        mav.addObject("userForm",userForm);
//        mav.addObject("userForm",userForm);
//        return mav;
//    }


    @RequestMapping(value = "usermanager/gousercabinet.html")
    public ModelAndView gousercabinet() {
        ModelAndView mav=new ModelAndView("cabinetUser");
        Authentication a= null;
        User us=null;
        try {
            a = SecurityContextHolder.getContext().getAuthentication();
            us=((UserWeb)a.getPrincipal()).getUser();
            mav.addObject("user", us);
            mav.addObject("avatarPath", getFileNameAvatar(avatarPath, us.getName()));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return mav;
    }

    @Qualifier("imageUploadForAvatar")
    @Autowired
    FileUploaderIface fileUploaderIface;

    @RequestMapping(value = "usermanager/saveimage.html",method=RequestMethod.POST) // Прием файла
    @ResponseBody
    public String saveImage(NewUserForm userForm,@RequestParam(value="userImage", required=false) MultipartFile image, HttpSession httpSession) {
        try {
            String fileName="";

            fileUploaderIface.validateImage(image); // Проверить изображение
            String userName=SecurityContextHolder.getContext().getAuthentication().getName();
            File fOld= null;
            try {
                fOld = fileUploaderIface.getListFileByName(avatarPath,userName)[0];
            } catch (Exception e) {
                //если список пустой - можно конечно проверку на ноль на пустой сделать , но мне лень :)
            }
            if(fOld!=null){
                //удаляем предыдущую аватарку в дальнейшем можно сделать кеширование (ну мало ли кто голую бабу разместит), что бы можно было отчитаться что эт не наш косяк.
                fOld.delete();
            }
            String imOFN= image.getOriginalFilename();
            fileName=userName +  imOFN.substring(imOFN.indexOf("."), imOFN.length());
            fileUploaderIface.saveFile(avatarPath,fileName, image); // Сохранить файл
            ((ConteinerForSession)httpSession.getAttribute("contS")).setAvatarPath(getFileNameAvatar(avatarPath,((ConteinerForSession)httpSession.getAttribute("contS")).getUser().getName()));
            return "<html><head><script>parent.usF.imageLoad(true,'"+fileName+"',null);</script></head><body>Дарова</body></html>";
        } catch (RuntimeException e) {
            //bindingResult.reject(e.getMessage());
            return "<html><head><script>parent.usF.imageLoad(false,null,'"+e.getMessage()+"');</script></head><body>Дарова</body></html>";
        }
    }

    @RequestMapping(value = "usermanager/createnewuser.html")
    public String createNewUser(NewUserForm newUserForm){
        return "userRegistration";
    }

   // @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "usermanager/createnewuser.html", method = RequestMethod.POST)
    public String saveNewUser(@Valid NewUserForm newUserForm, BindingResult bindingResult, HttpServletRequest request, @CurrentUserWeb UserWeb userWeb) {
        if(bindingResult.hasErrors()) {
            return "userRegistration";
        }
        Node current = userWeb.getUser();
        if (current == null && "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName()) ) {
            current = cmsInfoDAO.getInstanceFromDataBase().getNode();
        } else {
            return "userRegistration";
        }
        Role r = daoRole.getFullRoleByName("registeruser");

        User us= daoUser.createUser(newUserForm.createUserFromThis(null), current);
//        Authentication auth = new UsernamePasswordAuthenticationToken(us.getName(), us.getPassword());
//        SecurityContextHolder.getContext().setAuthentication(auth);

//        authenticationManager.authenticate(auth)
//        if(us!=null){
//            ConteinerForSession contS=new ConteinerForSession();
//            contS.setUser(us);
//            contS.setAvatarPath(getFileNameAvatar(avatarPath, us.getName()));
//            httpSession.setAttribute("userA", us);
//            httpSession.setAttribute("contS", contS);
//        }
        return "redirect:/usermanager/gousercabinet.html";
    }

    //получаем название файла по имени пользователя
    private String getFileNameAvatar(String folder, String userName){
        File f=null;
        try {
            f=fileUploaderIface.getListFileByName(folder, userName)[0];
        } catch (Exception e) {
            //или проверку на null
        }
        if(f!=null){
            return f.getName();
        }else{
            return"user0000.gif";
        }
    }

    public IUserDAO getDaoUser() {
        return daoUser;
    }

    public void setDaoUser(IUserDAO daoUser) {
        this.daoUser = daoUser;
    }

    public IRoleDAO getDaoRole() {
        return daoRole;
    }

    public void setDaoRole(IRoleDAO daoRole) {
        this.daoRole = daoRole;
    }
}
