package youness.automotive.controller.bean;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Represents action on view. It can be a button on a form or a menu on the table
 */
public class MenuAction {
    private String caption;

    private String relativeUrlPath;

    public MenuAction(String caption, String relativeUrlPath) {
        this.caption = caption;
        this.relativeUrlPath = relativeUrlPath;
    }

    public String getCaption() {
        return caption;
    }

    public String getRelativeUrlPath() {
        return relativeUrlPath;
    }
}
