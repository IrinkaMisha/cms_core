package rw.ktc.cms.isitnecessary.menu;

/**
 * Created by miha on 12.05.2015.
 */
@Deprecated
public class MenuItem {
    private String name;
    private String href;
    private MenuItem parent;

    public MenuItem(String name, String href) {
        this(name,href,null);
    }

    public MenuItem(String name, String href, MenuItem parent) {
        this.name = name;
        this.href = href;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public MenuItem getParent() {
        return parent;
    }

    public void setParent(MenuItem parent) {
        this.parent = parent;
    }
}
