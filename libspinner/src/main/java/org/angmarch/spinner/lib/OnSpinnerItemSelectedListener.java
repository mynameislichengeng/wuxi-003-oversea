package org.angmarch.spinner.lib;

import android.view.View;

public interface OnSpinnerItemSelectedListener {
    void onItemSelected(NiceSpinner parent, View view, int position, long id);
}
