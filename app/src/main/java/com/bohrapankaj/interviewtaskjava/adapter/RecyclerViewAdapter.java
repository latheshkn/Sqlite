package com.bohrapankaj.interviewtaskjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bohrapankaj.interviewtaskjava.databinding.RecyclerviewItemBinding;
import com.bohrapankaj.interviewtaskjava.model.JsonModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Recycler_VH> {

    ArrayList<JsonModel> list;
    OnItemClickListner onItemClickListner;

    public RecyclerViewAdapter(ArrayList<JsonModel> list, OnItemClickListner onItemClickListner) {
        this.list = list;
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public Recycler_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewItemBinding binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Recycler_VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_VH holder, int position) {
            JsonModel data = list.get(position);

            if (data.getN_state_uniq_id()!=null){
                holder.productId.setText(data.getN_product_uniq_id());
                holder.stateId.setText(data.getN_state_uniq_id());
                holder.stateName.setText(data.getC_state_name());
                holder.adminId.setText(data.getC_added_by_admin_user_id());
                holder.date.setText(data.getEffective_from());
                holder.productRate.setText(data.getProduct_rate());
            }

        holder.updateAmount.setOnClickListener(v -> {
            onItemClickListner.OnItemClicked(position,data.getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Recycler_VH extends RecyclerView.ViewHolder {

        TextView productId, stateId, stateName, adminId, date, productRate;
        Button updateAmount;

        public Recycler_VH(RecyclerviewItemBinding binding) {
            super(binding.getRoot());

            productId = binding.productId;
            stateId = binding.stateId;
            stateName = binding.stateName;
            adminId = binding.adminId;
            date = binding.date;
            productRate = binding.productRate;
            updateAmount = binding.updateAmount;
        }
    }

    public interface OnItemClickListner {
        void OnItemClicked(int position, String id);
    }
}
