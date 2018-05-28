package youness.automotive.controller.bean;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * This is the response to Ajax call to link two entities through DataLinkRequestBean
 */
public class DataLinkResponseBean {
    /**
     * true if an error happened
     */
    private boolean error;

    /**
     * The confirmation message
     */
    private String message;

    /**
     * The child entity name that is added
     */
    private String entity;

    /**
     * The response might have a redirection relative link
     */
    private String redirectionRelativeLink;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getRedirectionRelativeLink() {
        return redirectionRelativeLink;
    }

    public void setRedirectionRelativeLink(String redirectionRelativeLink) {
        this.redirectionRelativeLink = redirectionRelativeLink;
    }
}
