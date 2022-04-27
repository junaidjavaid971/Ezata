package app.com.ezata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.com.ezata.R;
import app.com.ezata.model.InProgressOrder;

public class InProgressOrderAdapter extends RecyclerView.Adapter<InProgressOrderAdapter.ViewHolder> {
    InProgressOrder[] list;

    public InProgressOrderAdapter(InProgressOrder[] list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.inprogess_item, parent, false);
        InProgressOrderAdapter.ViewHolder viewHolder = new InProgressOrderAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InProgressOrder progress = list[position];
        holder.tvOrderNumber.setText(progress.getOrderNumber());
        holder.tvCustomerName.setText(progress.getCustomerName());
        holder.tvOrderTime.setText(progress.getOrderTime());
        holder.tvOrderItems.setText(progress.getOrderCount());
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvCustomerName, tvOrderTime, tvOrderItems;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderTime = itemView.findViewById(R.id.tvTime);
            tvOrderItems = itemView.findViewById(R.id.tvItemsCount);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
