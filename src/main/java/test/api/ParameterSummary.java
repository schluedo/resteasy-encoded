package test.api;

public class ParameterSummary {

    private String queryParam;
    private String encodedQueryParam;
    private String pathParam;

    public ParameterSummary(String queryParam, String encodedQueryParam, String pathParam) {
        super();
        this.queryParam = queryParam;
        this.encodedQueryParam = encodedQueryParam;
        this.pathParam = pathParam;
    }

    public ParameterSummary() {
        super();
    }

    public String getQueryParam() {
        return queryParam;
    }
    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }
    public String getEncodedQueryParam() {
        return encodedQueryParam;
    }
    public void setEncodedQueryParam(String encodedQueryParam) {
        this.encodedQueryParam = encodedQueryParam;
    }
    public String getPathParam() {
        return pathParam;
    }
    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ParameterSummary [queryParam=").append(queryParam)
                .append(", encodedQueryParam=").append(encodedQueryParam).append(", pathParam=")
                .append(pathParam).append("]");
        return builder.toString();
    }

}
