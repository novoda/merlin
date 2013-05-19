Merlin
======

####*"An ok name for a library"*


Merlin aims to simplify network monitoring by providing 3 registerable callbacks. 
`onConnect()` , `onDisconnect()` and `onBind(NetworkStatus networkStatus)`.

[Download the jar from here](https://github.com/novoda/merlin/raw/master/releases/merlin-core-v0.2.jar)

or if you're using maven

*The repository is needed until the project is released on maven central, sorry about that*

    <repositories>
        <repository>
          <id>public-mvn-repo-releases</id>
          <url>https://github.com/novoda/public-mvn-repo/raw/master/releases</url>
        </repository>
    </repositories>

    <dependency>
      <groupId>com.novoda.merlin</groupId>
      <artifactId>merlin-core</artifactId>
      <version>0.2</version>
    </dependency>
    
    
or if you're using gradle     
    compile 'com.novoda.merlin:core:0.3-SNAPSHOT'
    
gradle users can also skip the setup as the manifests are merged automatically!    

##Usecases##

####`onConnect()`####

**When** the network state changes from disconnected to connected and a successful host ping has completed.

**Because** you have just aquired a valid network connection, time to update stale data!

####`onDisconnect()`####

**When** the network state changes from connected to disconnected.

**Because** you probably want to tell the user they're now offline! or disable certain functionality until a reliable connection is available again.

####`onBind(NetworkStatus networkStatus)`####

**When** the MerlinService has binded, the current NetworkStatus is provided, although this is without pinging a host. 

**Because** you may need to know the current state of the network before a network change occurs. 


##Setup (If you're not using gradle)

You'll need to add a few things to your manifest :

These permissions (if you don't already have them)

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

this service

    <service android:exported="false" android:name="main.java.demo.com.novoda.merlin.service.MerlinService" />

and this receiver

    <receiver android:name="main.java.demo.com.novoda.merlin.receiver.ConnectivityReceiver">
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

##Changelog

###0.3-SNAPSHOT###
  - quick release to get bug fix out
  - [Fixed issue #2](https://github.com/novoda/merlin/issues/2)

###0.2###
  - Removed bindListener.
  - Added bindable to match connectable and disconnectable.  
  - Tidied code up.

###0.2-SNAPSHOT###
  - Initial release.


##Contributing!

If you would like to help out (and everyone should!!) please code against the **[develop branch](https://github.com/novoda/merlin/tree/develop)** 

*and be sure write tests where possible!*

