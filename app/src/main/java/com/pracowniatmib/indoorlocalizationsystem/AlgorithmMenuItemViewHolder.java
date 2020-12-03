package com.pracowniatmib.indoorlocalizationsystem;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AlgorithmMenuItemViewHolder {

    private ImageView itemImageView;
    private TextView itemTextView;
    private CheckBox itemCheckBox;

    public AlgorithmMenuItemViewHolder() {}

    public void setItemImage(int resId) {
        this.itemImageView.setImageResource(resId);
    }

    public void setItemImageView(View view) {
        this.itemImageView = (ImageView) view;
    }

    public void setItemText(String text) {
        this.itemTextView.setText(text);
    }

    public void setItemTextView(View view) {
        this.itemTextView = (TextView) view;
    }

    public void setItemCheckBox(View view) {
        this.itemCheckBox = (CheckBox) view;
    }

    public void setCheckBoxChecked(boolean checked) {
        this.itemCheckBox.setChecked(checked);
    }

    public void setCheckBoxOnCheckedListener(CompoundButton.OnCheckedChangeListener listener) {
        this.itemCheckBox.setOnCheckedChangeListener(listener);
    }

    public void setItemAvailable(boolean available)
    {
        this.itemCheckBox.setEnabled(available);
        if(!available) this.itemImageView.setColorFilter(Color.argb(200, 100, 100, 100));
    }

    public boolean isCheckBoxChecked() {
        return this.itemCheckBox.isChecked();
    }

}
