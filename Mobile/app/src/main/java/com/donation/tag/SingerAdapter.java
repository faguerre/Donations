package com.donation.tag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.recyclerview.widget.RecyclerView;

import com.donation.R;

import java.util.ArrayList;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.MyViewHolder> {

    private final ArrayList<String> singersName;
    private final ArrayList<String> tags;
    Context context;


    public SingerAdapter(Context context, ArrayList<String> tags, ArrayList<String> tagsSelected) {
        this.singersName = tags;
        this.tags = tagsSelected;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView simpleCheckedTextView;


        public MyViewHolder(View view) {
            super(view);
            simpleCheckedTextView = view.findViewById(R.id.selectItem);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkable_list_layout, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.simpleCheckedTextView.setText(singersName.get(position));
        if (tags.contains(singersName.get(position))) {
            holder.simpleCheckedTextView.setChecked(true);
        }

        holder.simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value = holder.simpleCheckedTextView.isChecked();
                holder.simpleCheckedTextView.setChecked(!value);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singersName.size();
    }
}
