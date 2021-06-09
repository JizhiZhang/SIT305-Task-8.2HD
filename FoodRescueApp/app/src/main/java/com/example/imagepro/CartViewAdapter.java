package com.example.imagepro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagepro.data.DatabaseHelper;
import com.example.imagepro.model.Cart;

import java.util.List;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {
    private List<Cart> cartList;
    private Context context;
    DatabaseHelper db;

    public CartViewAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
        this.db = new DatabaseHelper(context.getApplicationContext());
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.cartFoodNameTextView.setText(cartList.get(position).getCart_name());
        holder.cartFoodNumTextView.setText(String.valueOf(cartList.get(position).getCart_quantity()));
        holder.cartFoodPriceTextView.setText("$ " + String.valueOf(cartList.get(position).getCart_price()));
        holder.cartFoodTotalPriceTextView.setText("$ " + String.valueOf(cartList.get(position).getTotal_price()));

        holder.cartDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = cartList.get(position).getCart_id();
                String name = cartList.get(position).getCart_name();
                Integer quantity = cartList.get(position).getCart_quantity();
                Integer price = cartList.get(position).getCart_price();
                Integer totalPrice = cartList.get(position).getTotal_price();

                int deleteRow = db.deleteCart(new Cart(id, name, quantity, price, totalPrice));
                if (deleteRow > 0) {
                    Toast.makeText(context.getApplicationContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(holder.itemView.getContext(), com.example.imagepro.HomeActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                } else {
                    Toast.makeText(context.getApplicationContext(), "No row found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView cartFoodNameTextView;
        TextView cartFoodNumTextView;
        TextView cartFoodPriceTextView;
        TextView cartFoodTotalPriceTextView;
        ImageView cartDeleteImageView;

        public CartViewHolder(View itemView) {
            super(itemView);
            cartFoodNameTextView = itemView.findViewById(R.id.cartFoodNameTextView);
            cartFoodNumTextView = itemView.findViewById(R.id.cartFoodNumTextView);
            cartFoodPriceTextView = itemView.findViewById(R.id.cartFoodPriceTextView);
            cartFoodTotalPriceTextView = itemView.findViewById(R.id.cartFoodTotalPriceTextView);
            cartDeleteImageView = itemView.findViewById(R.id.cartDeleteImageView);
        }
    }
}
