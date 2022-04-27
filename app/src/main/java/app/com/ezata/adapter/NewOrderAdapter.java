package app.com.ezata.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.com.ezata.R;
import app.com.ezata.model.NewOrder;
import app.com.ezata.ui.activities.MainActivity;
import app.com.ezata.ui.fragments.OrderDetailFragment;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.ViewHolder> {
    NewOrder[] orderList;
    Context context;

    public NewOrderAdapter(NewOrder[] orderList, Context context) {
        this.orderList = orderList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.new_order_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewOrder newOrder= orderList[position];
        holder.tvOrderNumber.setText(newOrder.getOrderNumber());
        holder.tvCustomerName.setText(newOrder.getCustomerName());
        holder.tvOrderTime.setText(newOrder.getOrderTime());
        holder.tvOrderItems.setText(newOrder.getOrderCount());
        holder.cardView.setOnClickListener(view -> {
        });

    }

    @Override
    public int getItemCount() {
        return orderList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvCustomerName, tvOrderTime, tvOrderItems;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber=itemView.findViewById(R.id.tvOrderNumber);
            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            tvOrderTime=itemView.findViewById(R.id.tvTime);
            tvOrderItems=itemView.findViewById(R.id.tvItemsCount);
            cardView=itemView.findViewById(R.id.cardview);
        }
    }
}
