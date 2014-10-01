package com.kameti.kameti;

import android.view.View;

public abstract class OnRowClickListener implements View.OnClickListener {
    protected Object[] args;
    OnRowClickListener(Object[] args) {
        this.args = args;
    }
}