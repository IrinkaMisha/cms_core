package by.imix.cms.web.dynamiccontent;

public class ErrorForResponse {
    public String message;

    public ErrorForResponse() {
    }

    public ErrorForResponse(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "message:" + message +
                '}';
    }
}
