package com.schoolthing.project2.ui.slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.schoolthing.project2.ItemClass;
import com.schoolthing.project2.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ItemlistAdapter extends ArrayAdapter<ItemClass> {

    private boolean[] selected;
    private Context contexts;
    int Resources;
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            //Toast.makeText(contexts, "Checked", Toast.LENGTH_SHORT).show();
        }
    };

    public ItemlistAdapter(Context context, int resource, ArrayList<ItemClass> objects) {
        super(context,resource,objects);
        contexts = context;
        Resources = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String item = getItem(position).getItem();
        Float cost = getItem(position).getCost();

        ItemClass itemstuff = new ItemClass(item,cost);

        LayoutInflater inflater = LayoutInflater.from(contexts);
        convertView = inflater.inflate(Resources,parent,false);

        TextView tv_itemName = convertView.findViewById(R.id.tv_itemname);
        TextView tv_itemCost = convertView.findViewById(R.id.tv_itemCost);
        CheckBox cb_Deletion = convertView.findViewById(R.id.cbDeletion);

        cb_Deletion.setOnCheckedChangeListener(listener);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        //format.setCurrency(Currency.getInstance("US"));
        tv_itemName.setText(item);
        tv_itemCost.setText(format.format(cost).toString());
        return convertView;
       // return super.getView(position, convertView, parent);

    }

    @Nullable
    @Override
    public ItemClass getItem(int position) {
        return super.getItem(position);
    }
}
