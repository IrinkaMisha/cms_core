package rw.ktc.cms.web.security;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 13.12.13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class CredentialBox {

    private static CredentialBox instance = new CredentialBox();

    private Set<String> permissions = new HashSet<>();

    private static CredentialBox getInstance() {
        return instance;
    }

    private CredentialBox() {
    }

    public static  Set<String> getAllPermissions() {
        return new HashSet<>(getInstance().getPermissions());
    }

    private Set<String> getPermissions() {
        return permissions;
    }

    public static boolean add(String s) {
        return getInstance().getPermissions().add(s);
    }

    public static void addAll(Collection<? extends String>... c) {
        for (Collection c1 : c) {
            getInstance().getPermissions().addAll(c1);
        }
    }
}
