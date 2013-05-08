Merlin
======

###"An ok name for a library"


Merlin aims to simplify network monitoring by providing 3 registerable callbacks. ``onConnect`` ``onDisconnect`` and ``onMerlinBind``

    onConnect()

What :
This is triggered when the network state changes from disconnected to connected and a successful host ping has completed.

Why :
You have aquired a valid network connection, time to update stale data!

    onDisconnect()

This is triggered when the network state changes from connected to disconnected.

    onMerlinBind(NetworkStatus networkStatus)

This is triggered as soon as the MerlinService has binded. The provided ``NetworkStatus`` does not take host pinging into account, unless a host pinged networkStatus has already been retrieved.


##Setup

You'll need to add a few things to your manifest :

These permissions (if you don't already have them)

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

this service

    <service android:exported="false" android:name="com.novoda.merlin.service.MerlinService" />

and this receiver

    <receiver android:name="com.novoda.merlin.receiver.ConnectivityReceiver">
      <intent-filter>
        <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
      </intent-filter>
    </receiver>

##Simple usage

Create Merlin (using Merlin.Builder())

    merlin = new Merlin.Builder().withConnectableCallbacks().build(context);

Bind and unbind the service in your activity

    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        merlin.unbind();
    }

Register for callbacks

    merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                // Do something!
            }
    });
    
The [MerlinActivity](https://github.com/novoda/merlin/blob/master/demo/src/com/novoda/demo/presentation/base/MerlinActivity.java) within the demo shows a simple way to declutter Merlin from your main application code.    
