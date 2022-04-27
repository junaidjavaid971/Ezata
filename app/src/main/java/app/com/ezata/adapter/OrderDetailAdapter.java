package app.com.ezata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.com.ezata.R;
import app.com.ezata.model.NewOrder;
import app.com.ezata.model.OrderDetail;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    OrderDetail[] orderList;

    public OrderDetailAdapter(OrderDetail[] orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_detail_item, parent, false);
        OrderDetailAdapter.ViewHolder viewHolder = new OrderDetailAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail = orderList[position];
        holder.tvQty.setText(orderDetail.getQuantity());
        holder.tvItem.setText(orderDetail.getItems());
        holder.tvPrice.setText(orderDetail.getPrice());
        if (orderDetail.getInstruction().isEmpty()) {
            holder.tvInstruction.setVisibility(View.GONE);
        } else {
            holder.tvInstruction.setText(orderDetail.getInstruction());
        }    }

    @Override
    public int getItemCount() {
        return orderList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQty, tvItem, tvPrice,tvInstruction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvItem = itemView.findViewById(R.id.tvItem);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvInstruction=itemView.findViewById(R.id.tvInstruction);

        }
    }
}
