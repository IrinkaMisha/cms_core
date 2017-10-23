package by.imix.cms.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by dima on 26.11.2014.
 */
@Entity
@Table( name = "cmsmenuitem")
public class CmsMenuItem implements Serializable{
    @Id
    @GeneratedValue
    @OrderColumn
    @Column(unique = true, nullable = false)
    private Integer id;

    private String name;

//  url length ->  http://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
    @Column(length = 2000)
    private String url; // relative

    @Column(length = 2000)
    private String urlSystem; //  - http://askbd-srv:8085/usogdp/

    @OneToMany( mappedBy = "parent",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CmsMenuItem> children;

    @ManyToOne(fetch=FetchType.LAZY)
    private CmsMenuItem parent;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CmsMenuItem getParent() {
        return parent;
    }

    public void setParent(CmsMenuItem parent) {
        this.parent = parent;
    }

    public List<CmsMenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<CmsMenuItem> children) {
        this.children = children;
    }

    public String getUrlSystem() {
        return urlSystem;
    }

    public void setUrlSystem(String urlSystem) {
        this.urlSystem = urlSystem;
    }

    public CmsMenuItem() {
    }

    public CmsMenuItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public CmsMenuItem(String name, String url, CmsMenuItem parent) {
        this.name = name;
        this.url = url;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "CmsMenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
//                "   , parent=" + parent +
                ", children=" + children +
                "}\n";
    }

//    @Override
//    public int compareTo(Object o) {
//        if ( !(o instanceof Menu) ) return -1;
//        if (null == ((Menu)o).getId()) return -1;
//        if ( (getId() - ((Menu)o).getId()) > 0){
//            return 1;
//        }
//        if ( (getId() - ((Menu)o).getId()) < 0){
//            return -1;
//        }
//        return 0;
//    }
}
