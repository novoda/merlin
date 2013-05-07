``Simple usage :``

Add the service to the manifest

    <service android:exported="false" android:name=".com.novoda.demo.connectivity.merlin.MerlinService" />

Create Merlin

        merlin = new Merlin(context, new MerlinConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected() {
            }
        });

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

enjoy the network state callbacks
