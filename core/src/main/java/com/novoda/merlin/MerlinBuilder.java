package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.logger.Logger;
import com.novoda.merlin.logger.MerlinBackwardsCompatibleLog;
import com.novoda.merlin.registerable.Register;
import com.novoda.merlin.registerable.Registrar;
import com.novoda.merlin.registerable.bind.BindCallbackManager;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.ConnectCallbackManager;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.DisconnectCallbackManager;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.service.MerlinServiceBinder;
import com.novoda.merlin.service.ResponseCodeValidator;

import static com.novoda.merlin.service.ResponseCodeValidator.DefaultEndpointResponseCodeValidator;

public class MerlinBuilder {

    private BindCallbackManager merlinOnBinder;
    private ConnectCallbackManager merlinConnector;
    private DisconnectCallbackManager merlinDisconnector;

    private Register<Connectable> connectables;
    private Register<Disconnectable> disconnectables;
    private Register<Bindable> bindables;

    private Endpoint endpoint = Endpoint.defaultEndpoint();
    private ResponseCodeValidator responseCodeValidator = new DefaultEndpointResponseCodeValidator();

    MerlinBuilder() {
    }

    /**
     * Enables Merlin to provide connectable callbacks, without calling this, Merlin.registerConnectable will throw a MerlinException.
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withConnectableCallbacks() {
        connectables = new Register<>();
        this.merlinConnector = new ConnectCallbackManager(connectables);
        return this;
    }

    /**
     * Enables Merlin to provide disconnectable callbacks, without calling this, Merlin.registerDisconnectable will throw a MerlinException.
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withDisconnectableCallbacks() {
        disconnectables = new Register<>();
        this.merlinDisconnector = new DisconnectCallbackManager(disconnectables);
        return this;
    }

    /**
     * Enables Merlin to provide bindable callbacks, without calling this, Merlin.registerBindable will throw a MerlinException.
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withBindableCallbacks() {
        bindables = new Register<>();
        this.merlinOnBinder = new BindCallbackManager(bindables);
        return this;
    }

    /**
     * Enables Merlin to provide connectable, disconnectable and bindable callbacks, without calling this, Merlin.registerConconnectable,
     * Merlin.registerDisconnectable, Merlin.registerBindable and Merlin.getConnectionStatusObservable will throw a MerlinException.
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withAllCallbacks() {
        return withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks();
    }

    /**
     * Deprecated, use directly {@link Logger} instead. To provide backwards compatibility
     * this method will attach a new {@link MerlinBackwardsCompatibleLog}. If using multiple instances
     * of {@link Merlin} the most recent call to `withLogging` will affect all other instances of {@link Merlin}.
     * <p>
     * Example:
     * MerlinInstanceOne.withLogging(true)
     * MerlinInstanceTwo.withLogging(false)
     * == no logs written.
     *
     * @param withLogging by default logging is disabled. withLogging = true will attach the default {@link MerlinBackwardsCompatibleLog}
     * @return MerlinBuilder
     */
    @Deprecated
    public MerlinBuilder withLogging(boolean withLogging) {
        Logger.detach(MerlinBackwardsCompatibleLog.getInstance());
        if (withLogging) {
            Logger.attach(MerlinBackwardsCompatibleLog.getInstance());
        }

        return this;
    }

    /**
     * Sets custom endpoint.
     *
     * @param endpoint by default "http://connectivitycheck.android.com/generate_204".
     * @return MerlinBuilder.
     */
    public MerlinBuilder withEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Sets custom endpoint.
     *
     * @param responseCodeValidator A validator implementation used for checking that the response code is what you expect.
     *                              The default endpoint returns a 204 No Content response, so the default validator checks for that.
     * @return MerlinBuilder.
     */
    public MerlinBuilder withResponseCodeValidator(ResponseCodeValidator responseCodeValidator) {
        this.responseCodeValidator = responseCodeValidator;
        return this;
    }

    /**
     * Creates Merlin with the specified builder options.
     *
     * @param context Used to create the MerlinServiceBinder and start a Service.
     * @return Merlin instance.
     */
    public Merlin build(Context context) {
        MerlinServiceBinder merlinServiceBinder = new MerlinServiceBinder(
                context,
                merlinConnector,
                merlinDisconnector,
                merlinOnBinder,
                endpoint,
                responseCodeValidator
        );

        Registrar merlinRegistrar = new Registrar(connectables, disconnectables, bindables);
        return new Merlin(merlinServiceBinder, merlinRegistrar);
    }

}
