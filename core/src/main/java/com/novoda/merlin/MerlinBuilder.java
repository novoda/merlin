package com.novoda.merlin;

import android.content.Context;

import com.novoda.merlin.registerable.MerlinRegisterer;
import com.novoda.merlin.registerable.Registerer;
import com.novoda.merlin.registerable.bind.BindListener;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.bind.OnBinder;
import com.novoda.merlin.registerable.connection.ConnectListener;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.connection.Connector;
import com.novoda.merlin.registerable.disconnection.DisconnectListener;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.registerable.disconnection.Disconnector;
import com.novoda.merlin.service.MerlinService;
import com.novoda.merlin.service.MerlinServiceBinder;

public class MerlinBuilder {

    private BindListener merlinOnBinder;
    private ConnectListener merlinConnector;
    private DisconnectListener merlinDisconnector;

    private MerlinRegisterer<Connectable> connectableRegisterer;
    private MerlinRegisterer<Disconnectable> disconnectableRegisterer;
    private MerlinRegisterer<Bindable> bindableRegisterer;

    private String endPoint = Merlin.DEFAULT_ENDPOINT;

    MerlinBuilder() {
    }

    /**
     * Enables Merlin to provide connectable callbacks, without calling this, Merlin.registerConnectable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withConnectableCallbacks() {
        connectableRegisterer = new MerlinRegisterer<Connectable>();
        this.merlinConnector = new Connector(connectableRegisterer);
        return this;
    }

    /**
     * Enables Merlin to provide disconnectable callbacks, without calling this, Merlin.registerDisconnectable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withDisconnectableCallbacks() {
        disconnectableRegisterer = new MerlinRegisterer<Disconnectable>();
        this.merlinDisconnector = new Disconnector(disconnectableRegisterer);
        return this;
    }

    /**
     * Enables Merlin to provide bindable callbacks, without calling this, Merlin.registerBindable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withBindableCallbacks() {
        bindableRegisterer = new MerlinRegisterer<Bindable>();
        this.merlinOnBinder = new OnBinder(bindableRegisterer);
        return this;
    }

    /**
     * Enables Merlin to provide connectable, disconnectable & bindable callbacks, without calling this, Merlin.registerConconnectable, Merlin.registerDisconnectable & Merlin.registerBindable will throw a MerlinException
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder withAllCallbacks() {
        return withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks();
    }

    /**
     * Sets the internal Merlin Logging state. Uses the TAG : Merlin
     *
     * @param withLogging by default Logging is disabled. withLogging = true will enable logging, withLogging = false will disable.
     * @return MerlinBuilder.
     */
    public MerlinBuilder withLogging(boolean withLogging) {
        MerlinLog.LOGGING = withLogging;
        return this;
    }

    /**
     * Disables using the method disableComponentEnabledSetting() to enable and disable the connectivity receiver -
     * This may be needed if you are experiencing the Android Runtime restarting after uninstalling your application - This appears to be an android bug, the issue is here http://code.google.com/p/android/issues/detail?id=55781&can=4&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars
     * The repercussions of this are that whenever a connectivity changed event is triggered, the application will be created but killed after realising the MerlinService has not been bound
     * (assuming Application.onCreate() doesn't bind to the MerlinService).
     *
     * @return MerlinBuilder.
     */
    public MerlinBuilder disableComponentEnabledSetting() {
        MerlinService.USE_COMPONENT_ENABLED_SETTING = false;
        return this;
    }


    /**
     * Sets custom endpoint
     *
     * @param endPoint by default "http://www.android.com".
     * @return MerlinBuilder.
     */
    public MerlinBuilder setEndPoint(String endPoint){
        this.endPoint = endPoint;
        return this;
    }

    /**
     * Creates Merlin with the specified builder options
     *
     * @return Merlin.
     */
    public Merlin build(Context context) {
        MerlinServiceBinder merlinServiceBinder = new MerlinServiceBinder(context, merlinConnector, merlinDisconnector, merlinOnBinder, endPoint);
        Registerer merlinRegisterer = new Registerer(connectableRegisterer, disconnectableRegisterer, bindableRegisterer);
        return new Merlin(merlinServiceBinder, merlinRegisterer);
    }

}
