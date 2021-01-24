package com.schoolthing.project2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.schoolthing.project2.R;

import java.text.NumberFormat;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private EditText etBudget_Home,etSaving_Home;
    private TextView tvBalance,tvSave;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String uid;
    private Button btTest_Home;
    private Float tmpBudget,fltSavings,tmpSaving;
    NumberFormat format;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fltSavings = tmpBudget * (Float.parseFloat(etSaving_Home.getText().toString())/100.0f);
            Budget budget = new Budget(Float.parseFloat(etBudget_Home.getText().toString()),fltSavings);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Account").setValue(budget);
            //tvTest_Home.setText(etBudget_Home.getText().toString());
 /*           reference.child(uid).child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Budget budgetdata = snapshot.getValue(Budget.class);

                    tvTest_Home.setText(budgetdata.fBudget.toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/
            updateBudget();
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        format = NumberFormat.getCurrencyInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();
        //final TextView textView = root.findViewById(R.id.text_home);
        etBudget_Home = root.findViewById(R.id.etBudget_Home);
        etSaving_Home = root.findViewById(R.id.etSaving_Home);
        tvBalance = root.findViewById(R.id.tvBalance);
        btTest_Home = root.findViewById(R.id.btTest_Home);
        tvSave = root.findViewById(R.id.tvSave);
        //tvBalance.setText("What????");
        btTest_Home.setOnClickListener(listener);
/*        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                textView.setText(s);
            }
        });*/
        //textView.setText("Check");
        updateBudget();
        return root;
    }

    private void updateBudget(){
        reference.child(uid).child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Budget budgetdata = snapshot.getValue(Budget.class);
                tmpBudget = budgetdata.fBudget;
                tmpSaving = budgetdata.fltSavings;
                //tmpBudget = tmpBudget - 0.0f;
                tvBalance.setText(format.format(tmpBudget));
                tvSave.setText(format.format(tmpSaving));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error in getting data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}