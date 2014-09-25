package com.kameti.kameti;

import android.view.View;

public abstract class OnRowClickListener implements View.OnClickListener {
    protected long rowId;
    OnRowClickListener(long rowId) {
        this.rowId = rowId;
    }
}