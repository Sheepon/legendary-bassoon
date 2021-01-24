package com.schoolthing.project2.ui.slideshow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schoolthing.project2.Budget;
import com.schoolthing.project2.ItemClass;
import com.schoolthing.project2.R;
import com.schoolthing.project2.ui.gallery.ItemlistAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class    SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private TextView tvTesting;
    private EditText etItemName, etItemCost;
    private FirebaseAuth mAuth;
    private Button btAdd, btRemove;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String uid;
    private ListView listViewItem;
    private Float tmpBudget;
    private ConstraintLayout TopBar;
    ArrayList<ItemClass> itemList;
    ItemlistAdapter adapter;
    ItemClass BigMan;
    List<String> items;
    List<Float> costs;
    NumberFormat format;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btAdd:
                    String strName = etItemName.getText().toString();
                    BigMan = new ItemClass(strName, Float.parseFloat(etItemCost.getText().toString()));
                    ItemClass.addTotalCost(Float.parseFloat(etItemCost.getText().toString()));
                    itemList.add(BigMan);
                    //adapter = new ItemlistAdapter(getContext(), R.layout.list_item,itemList);
                    //listViewItem.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    updateBudget();
                    items.add(strName);
                    costs.add(Float.parseFloat(etItemCost.getText().toString()));
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Items (Weekly)").setValue(items);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Costs (Weekly)").setValue(costs);
                    break;
                case R.id.btRemove:
                    itemList.removeAll(itemList);
                    items.clear();
                    costs.clear();
                    adapter.notifyDataSetChanged();
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Items (Weekly)").removeValue();
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Costs (Weekly)").removeValue();
                    reference.child(uid).child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Budget budgetdata = snapshot.getValue(Budget.class);
                            tmpBudget = budgetdata.fBudget/4.0f;
                            tvTesting.setText(format.format(tmpBudget).toString());
                            ItemClass.setTotalCost(0.0f);
                            if(tmpBudget >= budgetdata.fltSavings/4.0f){
                                TopBar.setBackgroundColor(Color.parseColor("#6CCA52"));
                            }else{
                                TopBar.setBackgroundColor(Color.parseColor("#DC5656"));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Error in getting data", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //int pos = listViewItem.getCheckedItemPosition();
                    //Toast.makeText(getContext(), pos, Toast.LENGTH_SHORT).show();
                    break;

            }
            //tvTesting.setText("Easy");

            //ItemClass bigman = new ItemClass(etItemName.getText().toString(),Float.parseFloat(etItemCost.getText().toString()));
/*            String strName = etItemName.getText().toString();
            BigMan = new ItemClass(strName,Float.parseFloat(etItemCost.getText().toString()));
            ItemClass.addTotalCost(Float.parseFloat(etItemCost.getText().toString()));
            itemList.add(BigMan);
            adapter = new ItemlistAdapter(getContext(), R.layout.list_item,itemList);
            listViewItem.setAdapter(adapter);
            updateBudget();*/
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        format = NumberFormat.getCurrencyInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();
        etItemCost = root.findViewById(R.id.etItemCost);
        etItemName = root.findViewById(R.id.etItemName);
        tvTesting = root.findViewById(R.id.tvTesting_gallery);
        listViewItem = root.findViewById(R.id.listViewItem);
        btAdd = root.findViewById(R.id.btAdd);
        TopBar = root.findViewById(R.id.TopBar);
        btRemove = root.findViewById(R.id.btRemove);
        btRemove.setOnClickListener(listener);
        btAdd.setOnClickListener(listener);
        ItemClass.setTotalCost(0f);
        updateBudget();
        ItemClass sheep = new ItemClass("Sheep", 20.0f);
        ItemClass maple = new ItemClass("Food", 500.0f);
        //BigMan = new ItemClass();
        //BigMan = new ItemClass("Test",20.0f);
        itemList = new ArrayList<>();
        //itemList.add(sheep);
        //itemList.add(maple);
        items = new ArrayList<>();
        costs = new ArrayList<>();

        adapter = new ItemlistAdapter(getContext(), R.layout.list_item, itemList);
        listViewItem.setAdapter(adapter);

        reference.child(uid).child("Items (Weekly)").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dss : snapshot.getChildren()) {
                    String check = dss.getValue(String.class);
                    items.add(check);
                    //Toast.makeText(getContext(), check, Toast.LENGTH_SHORT).show();
                    //BigMan = new ItemClass(check,20.0f);
                    //itemList.add(BigMan);
                    //adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(uid).child("Costs (Weekly)").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dss : snapshot.getChildren()) {
                    Float checkers = dss.getValue(Float.class);
                    costs.add(checkers);
                    ItemClass.addTotalCost(checkers);
                    //Toast.makeText(getContext(), check, Toast.LENGTH_SHORT).show();
                    //BigMan = new ItemClass(check,20.0f);
                    //itemList.add(BigMan);
                    //adapter.notifyDataSetChanged();

                }

                for (int i = 0; i < items.size(); i++) {
                    BigMan = new ItemClass(items.get(i), costs.get(i));
                    itemList.add(BigMan);
                    adapter.notifyDataSetChanged();
                }
                updateBudget();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Toast.makeText(getContext(), items.get(0), Toast.LENGTH_SHORT).show();
/*        for(int i = 0;i < items.size();i++){
            BigMan = new ItemClass(items.get(i),costs.get(i));
            itemList.add(BigMan);
            adapter.notifyDataSetChanged();
        }*/
        return root;
    }

    private void updateBudget() {
        reference.child(uid).child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Budget budgetdata = snapshot.getValue(Budget.class);
                tmpBudget = budgetdata.fBudget/4.0f;
                //BigMan.summonTotalCost();
                tmpBudget = tmpBudget - ItemClass.getTotalCost();
                tvTesting.setText(format.format(tmpBudget));
                if(tmpBudget >= budgetdata.fltSavings/4.0f){
                    TopBar.setBackgroundColor(Color.parseColor("#6CCA52"));
                }else{
                    TopBar.setBackgroundColor(Color.parseColor("#DC5656"));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error in getting data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}