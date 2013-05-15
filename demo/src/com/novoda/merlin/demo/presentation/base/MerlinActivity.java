package com.novoda.merlin.demo.presentation.base;

import android.app.Activity;
import android.os.Bundle;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;
import com.novoda.merlin.registerable.connection.Connectable;

public abstract class MerlinActivity extends Activity {

    private Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merlin = createMerlin();
    }

    protected abstract Merlin createMerlin();

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

    protected void registerConnectable(Connectable connectable) {
        merlin.registerConnectable(connectable);
    }

    protected void registerDisconnectable(Disconnectable disconnectable) {
        merlin.registerDisconnectable(disconnectable);
    }

    protected void registerBindable(Bindable bindable) {
        merlin.registerBindable(bindable);
    }

}
