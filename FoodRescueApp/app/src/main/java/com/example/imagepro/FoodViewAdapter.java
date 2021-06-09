package com.example.imagepro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagepro.model.Food;

import java.io.File;
import java.util.List;

public class FoodViewAdapter extends RecyclerView.Adapter<FoodViewAdapter.FoodViewHolder> {
    private List<Food> foodList;
    private Context context;
    private OnRowClickListener listener;


    public FoodViewAdapter(List<Food> foodList, Context context, OnRowClickListener listener) {
        this.foodList = foodList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.food_row, parent, false);
        return new FoodViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        String path = foodList.get(position).getImage();
        if (path != null) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.rfoodImageView.setImageBitmap(myBitmap);
            } else {
                holder.rfoodImageView.setImageResource(R.drawable.image);
            }
        } else {
            holder.rfoodImageView.setImageResource(R.drawable.image);
        }
        holder.rfoodNameTextView.setText(foodList.get(position).getTitle());
        holder.rfoodDescriptionTextView.setText(foodList.get(position).getDescription());
        holder.rfoodPickUpTimesTextView.setText(foodList.get(position).getPickUpTimes());
        holder.rfoodQuantityTextView.setText(String.valueOf(foodList.get(position).getQuantity()));
        holder.rfoodLocationTextView.setText(foodList.get(position).getLocation());
        holder.rfoodPriceTextView.setText(String.valueOf(foodList.get(position).getPrice()));
        holder.rfoodDateTextView.setText(foodList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView rfoodImageView;
        public TextView rfoodNameTextView;
        public TextView rfoodDescriptionTextView;
        public TextView rfoodPickUpTimesTextView;
        public TextView rfoodQuantityTextView;
        public TextView rfoodLocationTextView;
        public TextView rfoodPriceTextView;
        public TextView rfoodDateTextView;

        public ImageButton shareButton;
        public OnRowClickListener onRowClickListener;

        public FoodViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);
            rfoodImageView = itemView.findViewById(R.id.rfoodImageView);
            rfoodNameTextView = itemView.findViewById(R.id.rfoodNameTextView);
            rfoodDescriptionTextView = itemView.findViewById(R.id.rfoodDescriptionTextView);
            rfoodPickUpTimesTextView = itemView.findViewById(R.id.rfoodPickUpTimesTextView);
            rfoodQuantityTextView = itemView.findViewById(R.id.rfoodQuantityTextView);
            rfoodLocationTextView = itemView.findViewById(R.id.rfoodLocationTextView);
            rfoodPriceTextView = itemView.findViewById(R.id.rfoodPriceTextView);
            rfoodDateTextView = itemView.findViewById(R.id.rfoodDateTextView);

            shareButton = itemView.findViewById(R.id.shareButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRowClickListener.onItemClick(getAdapterPosition());
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRowClickListener.onShareButtonClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnRowClickListener {
        void onItemClick(int position);

        void onShareButtonClick(int position);
    }
}
