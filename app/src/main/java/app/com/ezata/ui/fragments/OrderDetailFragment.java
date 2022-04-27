package app.com.ezata.ui.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import app.com.ezata.R;
import app.com.ezata.adapter.NewOrderAdapter;
import app.com.ezata.adapter.OrderDetailAdapter;
import app.com.ezata.databinding.FragmentOrderDetailBinding;
import app.com.ezata.model.NewOrder;
import app.com.ezata.model.OrderDetail;

public class OrderDetailFragment extends Fragment {
    FragmentOrderDetailBinding binding;
    OrderDetail[] orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, container, false);
        View view = binding.getRoot();
        initRecyclerview();
        binding.ivFullsize.setOnClickListener(view1 -> {
            showAlertDialog();

        });
        return view;
    }

    private void showAlertDialog() {
        orders = new OrderDetail[]{
                new OrderDetail("1x", "Cheeseburger", "$5.78", "- No Ketchup"),
                new OrderDetail("2x", "Fries", "$2.39", ""),
                new OrderDetail("1x", "Milk Shake", "$3.25", ""),
                new OrderDetail("1x", "Garden Salad", "$3.25", ""),
                new OrderDetail("1x", "Diet Coke", "$0.89", "")


        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater1 = getLayoutInflater();
        View dialogView = inflater1.inflate(R.layout.dialog_neworder, null);
        RecyclerView rvOrders = dialogView.findViewById(R.id.rvFoodOrder);
        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderDetailAdapter adapter = new OrderDetailAdapter(orders);
        rvOrders.setAdapter(adapter);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
        layoutParams.height = WindowManager.LayoutParams.FILL_PARENT;
        alertDialog.getWindow().setAttributes(layoutParams);
    }

    private void initRecyclerview() {
        orders = new OrderDetail[]{
                new OrderDetail("1x", "Cheeseburger", "$5.78", "- No Ketchup"),
                new OrderDetail("2x", "Fries", "$2.39", ""),
                new OrderDetail("1x", "Milk Shake", "$3.25", ""),
                new OrderDetail("1x", "Garden Salad", "$3.25", ""),
                new OrderDetail("1x", "Diet Coke", "$0.89", "")

        };

        binding.rvFoodOrder.setHasFixedSize(true);
        binding.rvFoodOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderDetailAdapter adapter = new OrderDetailAdapter(orders);
        binding.rvFoodOrder.setAdapter(adapter);
    }
}