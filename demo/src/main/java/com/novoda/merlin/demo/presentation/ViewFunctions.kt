package com.novoda.merlin.demo.presentation

import android.view.View

fun View.onClick(f: () -> Unit) =
        this.setOnClickListener({ f() })
