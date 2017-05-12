package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.MerlinRegisterer;
import com.novoda.merlin.registerable.Registerer;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.bind.Binder;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.connection.Connector;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.registerable.disconnection.Disconnector;
import com.novoda.merlin.service.MerlinServiceBinder;
import com.novoda.merlin.service.ResponseCodeValidator;
import com.novoda.support.Logger;

import static com.novoda.merlin.service.ResponseCodeValidator.DefaultEndpointResponseCodeValidator;

public class MerlinBuilder {

    private BindListener merlinOnBinder;
    private ConnectListener merlinConnector;
    private DisconnectListener merlinDisconnector;

    private MerlinRegisterer<Connectable> connectableRegisterer;
    private MerlinRegisterer<Disconnectable> disconnectableRegisterer;
    private MerlinRegisterer<Bindable> bindableRegisterer;

    private String endPoint = Merlin.DEFAULT_ENDPOINT;
    private ResponseCodeValidator responseCodeValidator = new DefaultEndpointResponseCodeValidator();

    private boolean withLogging;

    MerlinBuilder() {
    }

    /**
     * Enables Merlin to provide connectable callbacks, without calling this, Merlin.registerConnectable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withConnectableCallbacks() {
        connectableRegisterer = new MerlinRegisterer<>();
        this.merlinConnector = new Connector(connectableRegisterer);
        return this;
    }

    /**
     * Enables Merlin to provide disconnectable callbacks, without calling this, Merlin.registerDisconnectable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withDisconnectableCallbacks() {
        disconnectableRegisterer = new MerlinRegisterer<>();
        this.merlinDisconnector = new Disconnector(disconnectableRegisterer);
        return this;
    }

    /**
     * Enables Merlin to provide bindable callbacks, without calling this, Merlin.registerBindable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withBindableCallbacks() {
        bindableRegisterer = new MerlinRegisterer<>();
        this.merlinOnBinder = new Binder(bindableRegisterer);
        return this;
    }

    /**
     * Enables Merlin to provide connectable, disconnectable & bindable callbacks, without calling this, Merlin.registerConconnectable, Merlin.registerDisconnectable, Merlin.registerBindable & Merlin.getConnectionStatusObservable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withAllCallbacks() {
        return withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks();
    }

    /**
     * Sets the internal Merlin logging state to report to a given LogHandle.
     *
     * @param logHandle that should be used when logging.
     * @return MerlinBuilder.
     */
    public MerlinBuilder withLogging(Logger.LogHandle logHandle) {
        Logger.attach(logHandle);
        return this;
    }

    /**
     * Removes all log handles from the Logger.
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withoutLogging() {
        Logger.detachAll();
        return this;
    }

    /**
     * Sets custom endpoint
     *
     * @param endPoint by default "http://connectivitycheck.android.com/generate_204".
     * @return MerlinBuilder.
     */
    public MerlinBuilder setEndPoint(String endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    /**
     * Sets custom endpoint
     *
     * @param responseCodeValidator A validator implementation used for checking that the response code is what you expect.
     *                              The default endpoint returns a 204 No Content response, so the default validator checks for that.
     * @return MerlinBuilder.
     */
    public MerlinBuilder setResponseCodeValidator(ResponseCodeValidator responseCodeValidator) {
        this.responseCodeValidator = responseCodeValidator;
        return this;
    }

    /**
     * Creates Merlin with the specified builder options
     *
     * @return Merlin.
     */
    public Merlin build(Context context) {
        MerlinServiceBinder merlinServiceBinder = new MerlinServiceBinder(
                context,
                merlinConnector,
                merlinDisconnector,
                merlinOnBinder,
                endPoint,
                responseCodeValidator
        );

        Registerer merlinRegisterer = new Registerer(connectableRegisterer, disconnectableRegisterer, bindableRegisterer);
        return new Merlin(merlinServiceBinder, merlinRegisterer);
    }

}
