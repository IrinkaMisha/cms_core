package rw.ktc.cms.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rw.ktc.cms.nodedata.NodeProperty;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;
import rw.ktc.cms.web.security.UserWeb;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebFilter(value = "/*", description = "Фильтр добавления чего-то!")
public class AddMenuForAdminFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AddMenuForAdminFilter.class);
    public static final String USERADMIN = "key_user"; // for use in jsp
    public static final String ADMININCLUDE = "/WEB-INF/views/templates/dynamictemplates/admininclude.jsp"; // for use in jsp
    public static final String BACKLIGHT = "/WEB-INF/views/templates/dynamictemplates/backlight.jsp"; // for use in jsp

    private List<String> listDenyPath = new ArrayList<String>();

    public void init(FilterConfig filterConfig) throws ServletException {
        // для данных урлов не будет добавлен admininclude.jsp - т.е. меню админа
        listDenyPath.add("template/show"); // в темплейты
        listDenyPath.add("resources");// в статические ресурсы
        listDenyPath.add("webjars");// в статические ресурсы
        listDenyPath.add("editdc.html");// проблемма со страницей
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        CharResponseWrapper wrappedResponse = new CharResponseWrapper(
//                (HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, servletResponse); // далеее фильтр для response
        ServletContext context = servletRequest.getServletContext();
        RequestDispatcher rd;

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request != null) {
            if (request.getServletPath().contains("template/show")) { // включение подсветки для шаблонов
                rd = context.getRequestDispatcher(BACKLIGHT); // include jsp
                rd.include(servletRequest, servletResponse);
            }
            if (!denyPath(request.getServletPath())) { // включение меню админа
                String ajaxStr = request.getHeader("X-Requested-With");
                boolean ajax = "XMLHttpRequest".equals(ajaxStr);
                HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
                if (session != null && !ajax) { //  и не запрос аякс
                    boolean userIsAdmin = isAdmin();
                    String contentType = ((HttpServletResponse) servletResponse).getContentType();
//                if (null == contentType) {
//                    logger.warn("тип контента не определен для пути:{}", request.getServletPath());
//                }
                    //  null == contentType означает ресурсы (js, css) , поэтому тоже не добавляем
                    if (userIsAdmin && null != contentType && contentType.contains(MediaType.TEXT_HTML_VALUE)) {
                        servletRequest.setAttribute(USERADMIN, getUser()); // enable import code in jsp page
                        logger.debug("Добавление контента меню для пользователя:{}, типа контента:{}, путь:{}", request.getUserPrincipal().getName(), MediaType.TEXT_HTML_VALUE, request.getServletPath());
                        try {
                            rd = context.getRequestDispatcher(ADMININCLUDE); // include jsp
                            rd.include(servletRequest, servletResponse);
                        } catch (Exception e) {
                            logger.error("Ошибка добавления меню на страницу", e);
                        }

                    }
//                    byte[] bytes = wrappedResponse.getByteArray();
//
//                    if (wrappedResponse != null && wrappedResponse.getContentType() != null && wrappedResponse.getContentType().contains("text/html")) {
//                        String out = new String(bytes);
//                        // DO YOUR REPLACEMENTS HERE
////                        out = out.replace("</head>", "<script type='text/javascript' src='/resources/script/injeditor.js'><script/></head>");
////                    servletResponse.getOutputStream().write(out.getBytes());
//                    }
                }
            }
        }


    }

    private boolean denyPath(String path) {
        for (String deny : listDenyPath) {
            if (path.contains(deny)) {
                return true;
            }
        }
        return false;
    }

    public void destroy() {

    }

    private Boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserWeb) {
            User user = ((UserWeb) auth.getPrincipal()).getUser();
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                List<NodeProperty> props = role.getNodeProperties();
                for (NodeProperty prop : props) {
                    if (prop.getKeyt().equals(Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY) && prop.getValue().equals("ROLE_ADMIN_SETTINGS")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserWeb) {
            User user = ((UserWeb) auth.getPrincipal()).getUser();
            return user;
        }
        return null;
    }


//    private static class ByteArrayServletStream extends ServletOutputStream {
//        ByteArrayOutputStream baos;
//
//        ByteArrayServletStream(ByteArrayOutputStream baos) {
//            this.baos = baos;
//        }
//
//        public void write(int param) throws IOException {
//            baos.write(param);
//        }
//
//        @Override
//        public boolean isReady() {
//            return false;
//        }
//
//        @Override
//        public void setWriteListener(WriteListener writeListener) {
//
//        }
//    }
//
//    private static class ByteArrayPrintWriter {
//
//        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        private PrintWriter pw = new PrintWriter(baos);
//
//        private ServletOutputStream sos = new ByteArrayServletStream(baos);
//
//        public PrintWriter getWriter() {
//            return pw;
//        }
//
//        public ServletOutputStream getStream() {
//            return sos;
//        }
//
//        byte[] toByteArray() {
//            return baos.toByteArray();
//        }
//    }
//
//    public class CharResponseWrapper extends HttpServletResponseWrapper {
//        private ByteArrayPrintWriter output;
//        private boolean usingWriter;
//
//        public CharResponseWrapper(HttpServletResponse response) {
//            super(response);
//            usingWriter = false;
//            output = new ByteArrayPrintWriter();
//        }
//
//        public byte[] getByteArray() {
//            return output.toByteArray();
//        }
//
//        @Override
//        public ServletOutputStream getOutputStream() throws IOException {
//            // will error out, if in use
//            if (usingWriter) {
//                super.getOutputStream();
//            }
//            usingWriter = true;
//            return output.getStream();
//        }
//
//        @Override
//        public PrintWriter getWriter() throws IOException {
//            // will error out, if in use
//            if (usingWriter) {
//                super.getWriter();
//            }
//            usingWriter = true;
//            return output.getWriter();
//        }
//
//        public String toString() {
//            return output.toString();
//        }
//    }
}
