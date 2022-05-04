package app.com.ezata.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.com.ezata.R;
import app.com.ezata.model.PickUpReady;

public class PickUpReadyAdapter extends RecyclerView.Adapter<PickUpReadyAdapter.ViewHolder> {
    PickUpReady[] pickUp;

    public PickUpReadyAdapter(PickUpReady[] pickUp) {
        this.pickUp = pickUp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.pickup_item, parent, false);
        PickUpReadyAdapter.ViewHolder viewHolder = new PickUpReadyAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PickUpReady pickUpReady= pickUp[position];
        holder.tvOrderNumber.setText(pickUpReady.getOrderNumber());
        holder.tvCustomerName.setText(pickUpReady.getCustomerName());
        holder.tvCourierName.setText(pickUpReady.getCourierName());
        holder.tvOrderTime.setText(pickUpReady.getOrderTime());
        holder.tvOrderPickTime.setText(pickUpReady.getPickUpTime());
        holder.tvPinCode.setText(pickUpReady.getPinCode());
        holder.ivVehicle.setImageResource(pickUpReady.getVehicleId());
    }

    @Override
    public int getItemCount() {
        return pickUp.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvCustomerName, tvCourierName,tvOrderTime, tvOrderPickTime, tvPinCode;
        ImageView ivVehicle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber=itemView.findViewById(R.id.tvOrderNumber);
            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            tvCourierName=itemView.findViewById(R.id.tvCourierName);
            tvOrderTime=itemView.findViewById(R.id.tvOrderTime);
            tvOrderPickTime=itemView.findViewById(R.id.tvPickup);
            tvPinCode=itemView.findViewById(R.id.tvPinCode);
            ivVehicle=itemView.findViewById(R.id.ivVehicle);
        }
    }
}
