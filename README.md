# 🛑 THIS REPOSITORY IS OFFICIALLY NO LONGER UNDER MAINTENANCE since 10/02/2022 🛑

![Merlin header image](https://github.com/novoda/merlin/blob/release/header.png)

# Merlin [![Build status](https://ci.novoda.com/buildStatus/icon?job=merlin)](https://ci.novoda.com/job/merlin/lastBuild/console) [![Download](https://api.bintray.com/packages/novoda/maven/merlin/images/download.svg)](https://bintray.com/novoda/maven/merlin/_latestVersion) [![License](https://img.shields.io/github/license/novoda/merlin.svg)](https://github.com/novoda/merlin/blob/release/LICENSE.txt)

Merlin aims to simplify network monitoring. Providing 3 registerable callbacks for network connectivity changes.
`onConnect()` , `onDisconnect()` and `onBind(NetworkStatus networkStatus)`.

## Adding to your project

To start using Merlin, add these lines to your module's `build.gradle`:

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'com.novoda:merlin:1.2.0'
}
```

### Optional steps

**Note:** these steps should _not_ be necessary as the Manifest Merger should be taking care of this for you!

If for some reason your app's manifest doesn't end up containing the required entries, and you encounter issues, you might need to manually add a few things to your `AndroidManifest.xml`:

 1. These permissions:

    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    ```

 2. This service:

    ```xml
    <service
      android:exported="false"
      android:name="com.novoda.merlin.MerlinService" />
    ```

## Sample usage

Create Merlin:

```java
merlin = new Merlin.Builder().withConnectableCallbacks().build(context);
```

Bind and unbind the service in your activity:

```java
@Override
protected void onResume() {
    super.onResume();
    merlin.bind();
}

@Override
protected void onPause() {
    merlin.unbind();
    super.onPause();
}
```

Register for callbacks:

```java
merlin.registerConnectable(new Connectable() {
    @Override
    public void onConnect() {
        // Do something you haz internet!
    }
});
```

For further details you can check [the wiki](https://github.com/novoda/merlin/wiki/Simple-Api-Usage).

## Migrating from pre-v1 versions

Version 1 of Merlin introduced [several breaking changes](https://github.com/novoda/merlin/wiki/Migration-guide#notable-changes-for-migration) in the implementation and the APIs, to account for the latest changes in Android N+. Please follow the instructions [in the wiki](https://github.com/novoda/merlin/wiki/Migration-guide) to make the upgrade as painless as possible.

## Migrating from 1.1.7

In version `1.1.8` some public API changes were made. According to our tests auto importing should be able to take care of these changes. 

### RxJava support in v1.0+

Starting in version 1.0.0, the RxJava support is no longer built into the library but it has been split out into a separate artifact. You'll need to add one of these two dependencies, depending on the version of RxJava you use:

```groovy
// For RxJava 1.x
implementation 'com.novoda:merlin-rxjava:[version_number]'

// For RxJava 2.x
implementation 'com.novoda:merlin-rxjava2:[version_number]'
```

## Links

Here are a list of useful links:

 * We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md)
 * If you have a problem check the [Issues Page](https://github.com/novoda/merlin/issues) first to see if we are working on it
 * For further usage or to delve more deeply checkout the [Project Wiki](https://github.com/novoda/merlin/wiki)
 * Looking for community help, browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-merlin) or use the tag: `support-merlin` when posting a new question
