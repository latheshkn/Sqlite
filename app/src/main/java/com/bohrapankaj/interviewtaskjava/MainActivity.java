package com.bohrapankaj.interviewtaskjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bohrapankaj.interviewtaskjava.adapter.RecyclerViewAdapter;
import com.bohrapankaj.interviewtaskjava.databinding.ActivityMainBinding;
import com.bohrapankaj.interviewtaskjava.databinding.UpdatePriceDialogBinding;
import com.bohrapankaj.interviewtaskjava.db.DatabaseHelper;
import com.bohrapankaj.interviewtaskjava.model.JsonModel;
import com.bohrapankaj.interviewtaskjava.network.Api;
import com.bohrapankaj.interviewtaskjava.network.BaseClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListner {

    DatabaseHelper dataBaseHelper;
    private ActivityMainBinding binding;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<JsonModel> apiListItem;
    ArrayList<JsonModel> sqlLiteListItem;
    Dialog dialog;
    SimpleArcLoader simpleArcLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dataBaseHelper = new DatabaseHelper(this);
        dialog = new Dialog(this);

        simpleArcLoader = binding.simpleArcLoader;
        simpleArcLoader.start();

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getsonSampleData();

        RetriveLocalDbData();
    }

    private void getsonSampleData() {

        Api api = BaseClient.getBaseClient().create(Api.class);
        Call<ArrayList<JsonModel>> call = api.getDetail();

        call.enqueue(new Callback<ArrayList<JsonModel>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonModel>> call, Response<ArrayList<JsonModel>> response) {
                if (response.isSuccessful()) {

                    apiListItem = response.body();

                    Cursor cursor = dataBaseHelper.RetriveData();
                    if (cursor.getCount() == 0) {
                        for (int i = 0; i < apiListItem.size(); i++) {

                            if (apiListItem.get(i).getN_state_uniq_id() != null) {
                                InsertLocalDbData(
                                        apiListItem.get(i).getN_product_uniq_id(),
                                        apiListItem.get(i).getN_state_uniq_id(),
                                        apiListItem.get(i).getC_state_name(),
                                        apiListItem.get(i).getEffective_from(),
                                        apiListItem.get(i).getC_added_by_admin_user_id(),
                                        apiListItem.get(i).getProduct_rate(),
                                        apiListItem.get(i).getAdded_time());
                            }
                        }
                    } else {
//                        Toast.makeText(MainActivity.this, "Data Already Saved", Toast.LENGTH_SHORT).show();
                    }

                } else {
//                    Toast.makeText(MainActivity.this, "Something went wrong " + response.errorBody(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "onFailure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnItemClicked(int position, String id) {
//        Toast.makeText(this, "Position " + id, Toast.LENGTH_SHORT).show();
        
        updatePriceDialog(id,position);
    }

    public void InsertLocalDbData(String n_product_uniq_id, String n_state_uniq_id,
                                  String c_state_name, String effective_from,
                                  String c_added_by_admin_user_id, String product_rate,
                                  String added_time) {

        boolean isDataInserted = dataBaseHelper.InsertData(n_product_uniq_id, n_state_uniq_id, c_state_name, effective_from,
                c_added_by_admin_user_id, product_rate, added_time);
        if (isDataInserted) {
//            Toast.makeText(MainActivity.this, "inserted successfully", Toast.LENGTH_SHORT).show();

            // after inserting data we are getting back
            RetriveLocalDbData();
        } else {
//            Toast.makeText(MainActivity.this, "Failed to insert", Toast.LENGTH_SHORT).show();
        }

    }


    public void RetriveLocalDbData() {
        Cursor cursor = dataBaseHelper.RetriveData();
        sqlLiteListItem = new ArrayList<>();
        if (cursor.getCount() == 0) {
//            Toast.makeText(this, "No data in local ", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            sqlLiteListItem.add(new JsonModel(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            ));
        }

        adapter = new RecyclerViewAdapter(sqlLiteListItem, MainActivity.this::OnItemClicked);
        recyclerView.setAdapter(adapter);
        simpleArcLoader.stop();
        simpleArcLoader.setVisibility(View.GONE);

    }


    public void updatePriceDialog(String id,int position) {
        UpdatePriceDialogBinding binding = UpdatePriceDialogBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        Button btnNo = binding.btnNo;
        Button btnYes = binding.btnYes;
        EditText updatePriceEt = binding.updatePriceEt;

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = updatePriceEt.getText().toString();
                if (price.isEmpty()) {
                    updatePriceEt.requestFocus();
                    updatePriceEt.setError("Enter Price");
                    return;
                }
                UpdateData(id, price,position);
            }
        });
        dialog.show();
    }

    private void UpdateData(String id, String price,int position) {

        boolean saved =dataBaseHelper.UpdateData(id,price);
        if (saved) {
//            Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            RetriveLocalDbData();
        } else {
//            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
        }
    }

}