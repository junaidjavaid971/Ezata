package app.com.ezata.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.com.ezata.OnItemClicked;
import app.com.ezata.R;
import app.com.ezata.model.HistoryDetails;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    HistoryDetails[] historyDetails;
    OnItemClicked onItemClicked;

    public HistoryAdapter(HistoryDetails[] historyDetails, OnItemClicked onItemClicked) {
        this.historyDetails = historyDetails;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new HistoryAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HistoryDetails details = historyDetails[position];
        holder.tvDate.setText(details.getDate());
        holder.tvTime.setText(details.getTime());
        holder.tvOrderNmber.setText(details.getOrderNumber());
        holder.tvCustomerName.setText(details.getCustomerName());
        holder.tvType.setText(details.getType());
        holder.tvAmount.setText(details.getAmount());
        holder.tvStatus.setText(details.getStatus());
        holder.fullSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked.onItemSelect(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyDetails.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvOrderNmber, tvCustomerName, tvType, tvAmount, tvStatus;
        ConstraintLayout fullSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvOrderNmber = itemView.findViewById(R.id.tvOrderNumber);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvType = itemView.findViewById(R.id.tvType);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            fullSize = itemView.findViewById(R.id.fullsize);
        }
    }
}
