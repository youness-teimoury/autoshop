package youness.automotive.controller.bean;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Contains the generic metadata available to view's page
 */
public class PageMetaData {

    private final String title;

    public PageMetaData(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}