package test.server;

import test.api.ParameterSummary;
import test.api.RestServiceApi;

public class RestService implements RestServiceApi {

    @Override
    public ParameterSummary methodEncoded(String pp, String qp, String eqp) {
        return new ParameterSummary(qp, eqp, pp);
    }

    @Override
    public ParameterSummary method(String pp, String qp, String eqp) {
        return new ParameterSummary(qp, eqp, pp);
    }

}
