Merlin
======

####*"An ok name for a library"*


Merlin aims to simplify network monitoring by providing 3 registerable callbacks. 
`onConnect()` , `onDisconnect()` and `onBind(NetworkStatus networkStatus)`.

##Adding to your project##

If you're using gradle all you need is this : 

```groovy
dependencies {
    compile "com.novoda.merlin:dependency:1.0"
}
``` 

####[if you're not using gradle yet... (maven and jar setup)](https://github.com/novoda/merlin/wiki/adding-via-jar-and-maven)####


##Usecases##

####`onConnect()`####

![](https://raw.github.com/novoda/merlin/master/releases/res/on_connect.jpg)

**When** the network state changes from disconnected to connected and a successful host ping has completed.

**Because** you have just aquired a valid network connection, time to update stale data!

####`onDisconnect()`####

![](https://raw.github.com/novoda/merlin/master/releases/res/on_disconnect.jpg)

**When** the network state changes from connected to disconnected.

**Because** you probably want to tell the user they're now offline! or disable certain functionality until a reliable connection is available again.

####`onBind(NetworkStatus networkStatus)`####

**When** the `MerlinService` has binded, the current `NetworkStatus` is provided, although this is without pinging a host. 

**Because** you may need to know the current state of the network before a network change occurs. 

##Simple usage

Create Merlin (using `Merlin.Builder()`)

```java
merlin = new Merlin.Builder().withConnectableCallbacks().build(context);
```

Bind and unbind the service in your activity

```java
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
```

Register for callbacks

```java
merlin.registerConnectable(new Connectable() {
        @Override
        public void onConnect() {
            // Do something!
        }
});
```
    
The [`MerlinActivity`](https://github.com/novoda/merlin/blob/master/demo/src/com/novoda/merlin/demo/presentation/base/MerlinActivity.java) within the demo shows a simple way to declutter Merlin from your main application code.    

##Changelog

###0.4.1###
  - added java doc to the MerlinBuilder
  - added sources to the maven release
  - added apache 2.0 license

###0.4###
  - workaround for issue #4 which causes the Android Runtime to restart after uninstalling
  - code tidying

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

