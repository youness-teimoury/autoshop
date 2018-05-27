package youness.automotive.controller.bean;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Contains the generic metadata available to view's page
 */
public class PageMetaData {

    private final String title;

    private final String rootViewName; //TODO remove

    public PageMetaData(String title, String rootViewName) {
        this.title = title;
        this.rootViewName = rootViewName;
    }

    public String getTitle() {
        return title;
    }

    public String getRootViewName() {
        return rootViewName;
    }
}