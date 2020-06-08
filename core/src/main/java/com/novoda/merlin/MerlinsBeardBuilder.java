package com.novoda.merlin;

import android.content.Context;
import android.net.ConnectivityManager;

public class MerlinsBeardBuilder {

    private Endpoint endpoint = Endpoint.captivePortalEndpoint();
    private ResponseCodeValidator responseCodeValidator = new ResponseCodeValidator.CaptivePortalResponseCodeValidator();
    private RequestMaker requestMaker = new HttpRequestMaker();

    MerlinsBeardBuilder() {
        // Uses builder pattern.
    }

    /**
     * Sets a custom endpoint.
     *
     * @param endpoint to ping, by default {@link Endpoint#CAPTIVE_PORTAL_ENDPOINT}.
     * @return MerlinsBeardBuilder.
     */
    public MerlinsBeardBuilder withEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Validator used to check the response code when performing a ping.
     *
     * @param responseCodeValidator A validator implementation used for checking that the response code is what you expect.
     *                              The default endpoint returns a 204 No Content response, so the default validator checks for that.
     * @return MerlinsBeardBuilder.
     */
    public MerlinsBeardBuilder withResponseCodeValidator(ResponseCodeValidator responseCodeValidator) {
        this.responseCodeValidator = responseCodeValidator;
        return this;
    }

    /**
     * Sets custom request maker
     * @param requestMaker Request maker used to ping endpoint set by {@link #withEndpoint} method
     * @return MerlinBuilder
     */
    public MerlinsBeardBuilder withCustomRequestMaker(RequestMaker requestMaker) {
        this.requestMaker = requestMaker;
        return this;
    }

    public MerlinsBeard build(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        AndroidVersion androidVersion = new AndroidVersion();
        EndpointPinger captivePortalpinger = EndpointPinger.withCustomRequestMakerEndpointAndValidation(requestMaker, endpoint, responseCodeValidator);
        Ping captivePortalPing = new Ping(requestMaker, endpoint, new EndpointPinger.ResponseCodeFetcher(), responseCodeValidator);

        return new MerlinsBeard(connectivityManager, androidVersion, captivePortalpinger, captivePortalPing);
    }
}
