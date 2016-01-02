package rw.ktc.cms.isitnecessary.menu;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 10.12.13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class Menu{
    private String title;
    private String description;
    private String jsonMenu;
    private Long id;
    private MenuItem menuItem;

    public Menu() {
    }

    public Menu(MenuItem menuItem) {
        this(menuItem,null);
    }

    public Menu(MenuItem menuItem, String title) {
        this(menuItem,null,null);
    }

    public Menu(MenuItem menuItem, String title, String description) {
        this.menuItem = menuItem;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJsonMenu() {
        return jsonMenu;
    }

    public void setJsonMenu(String jsonMenu) {
        this.jsonMenu = jsonMenu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
}
