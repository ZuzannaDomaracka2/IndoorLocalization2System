package com.pracowniatmib.indoorlocalizationsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class AlgorithmMenuAdapter extends ArrayAdapter<AlgorithmOption> {

    private boolean flag = false;
    private final List<AlgorithmOption> algorithmOptionList;

    AlgorithmMenuAdapter(Context context, List<AlgorithmOption> objects) {
        super(context, R.layout.algorithm_menu_item, R.id.itemText, objects);
        algorithmOptionList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        final String currentItem = getItem(position).getName();
        final AlgorithmMenuItemViewHolder holder;
        if(convertView == null) {
            holder = new AlgorithmMenuItemViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.algorithm_menu_item, parent, false);
            holder.setItemImageView(convertView.findViewById(R.id.itemImage));
            switch (currentItem) {
                case "Dead reckoning":
                    holder.setItemImage(R.drawable.dead_reckoning_icon);
                    break;
                case "Trilateration":
                    holder.setItemImage(R.drawable.trilateration_icon);
                    break;
                case "Fingerprinting":
                    holder.setItemImage(R.drawable.fingerprinting_icon);
                    break;
                default:
                    break;
            }
            holder.setItemTextView(convertView.findViewById(R.id.itemText));
            holder.setItemText(currentItem);
            holder.setItemCheckBox(convertView.findViewById(R.id.itemCheckBox));
            holder.setCheckBoxOnCheckedListener(null);
            holder.setCheckBoxChecked(algorithmOptionList.get(position).isEnabled());
            holder.setCheckBoxOnCheckedListener((buttonView, isChecked) -> {
                holder.setCheckBoxChecked(isChecked);
                setAlgorithm(position, holder.isCheckBoxChecked());
                setFlag(true);
            });
            convertView.setTag(holder);
        }

        return  convertView;
    }

    public void setAlgorithm(int position, boolean chosen) {
        this.algorithmOptionList.get(position).setEnabled(chosen);
    }

    public void setFlag (boolean flag) {
        this.flag = flag;
    }

    public List<AlgorithmOption> getAlgorithmOptionList() {
        return this.algorithmOptionList;
    }

    public boolean isFlagSet() {
        return this.flag;
    }

    public int getOptionCount()
    {
        return algorithmOptionList.size();
    }
}
