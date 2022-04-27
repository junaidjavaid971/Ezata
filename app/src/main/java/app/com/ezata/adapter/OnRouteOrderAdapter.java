package app.com.ezata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.com.ezata.R;
import app.com.ezata.model.OnRouteOrders;

public class OnRouteOrderAdapter extends RecyclerView.Adapter<OnRouteOrderAdapter.ViewHolder>{
    OnRouteOrders[] orders;

    public OnRouteOrderAdapter(OnRouteOrders[] orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.onroute_item, parent, false);
        OnRouteOrderAdapter.ViewHolder viewHolder = new OnRouteOrderAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnRouteOrders onRouteOrders= orders[position];
        holder.tvOrderNumber.setText(onRouteOrders.getOrderNumber());
        holder.tvCourierName.setText(onRouteOrders.getCourierName());
        holder.tvTime.setText(onRouteOrders.getDeliveryTime());
        holder.ivVehicle.setImageResource(onRouteOrders.getVehicle());

    }

    @Override
    public int getItemCount() {
        return orders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber,tvCourierName, tvTime;
        ImageView ivVehicle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber=itemView.findViewById(R.id.tvOrderNumber);
            tvCourierName=itemView.findViewById(R.id.tvCourierName);
            tvTime=itemView.findViewById(R.id.tvDelieverdTime);
            ivVehicle=itemView.findViewById(R.id.ivVehicle);
        }
    }
}
