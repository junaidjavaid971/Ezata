package app.com.ezata.ui.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import app.com.ezata.R;
import app.com.ezata.adapter.InProgressOrderDetailAdapter;

import app.com.ezata.adapter.OrderDetailAdapter;
import app.com.ezata.databinding.FragmentInProgressOrderDetailsBinding;
import app.com.ezata.model.InProgressOrderDetail;


public class InProgressOrderDetailsFragment extends Fragment {
    FragmentInProgressOrderDetailsBinding binding;
    InProgressOrderDetail[] orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress_order_details, container, false);
        View view = binding.getRoot();
        binding.ivFullsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showALertDialog();
            }
        });
        initRecylerview();
        return view;
    }

    private void showALertDialog() {
        orders = new InProgressOrderDetail[]{
                new InProgressOrderDetail("1x", "Cheeseburger", "$5.78", "-No Ketchup"),
                new InProgressOrderDetail("2x", "Fries", "$2.39", ""),
                new InProgressOrderDetail("1x", "Vanilla Milkshake", "$1.82", ""),
                new InProgressOrderDetail("1x", "Garden Salad.", "$3.25", ""),
                new InProgressOrderDetail("1x", "Diet Coke.", "$0.89", "")


        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater1 = getLayoutInflater();
        View dialogView = inflater1.inflate(R.layout.dialog_inprogress, null);
        RecyclerView rvOrders = dialogView.findViewById(R.id.rvFoodOrder);
        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        InProgressOrderDetailAdapter adapter = new InProgressOrderDetailAdapter(orders);
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

    private void initRecylerview() {
        orders = new InProgressOrderDetail[]{
                new InProgressOrderDetail("1x", "Cheeseburger", "$5.78", "-No Ketchup"),
                new InProgressOrderDetail("2x", "Fries", "$2.39", ""),
                new InProgressOrderDetail("1x", "Vanilla Milkshake", "$1.82", ""),
                new InProgressOrderDetail("1x", "Garden Salad.", "$3.25", ""),
                new InProgressOrderDetail("1x", "Diet Coke.", "$0.89", "")


        };

        binding.rvFoodOrder.setHasFixedSize(true);
        binding.rvFoodOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        InProgressOrderDetailAdapter adapter = new InProgressOrderDetailAdapter(orders);
        binding.rvFoodOrder.setAdapter(adapter);

    }
}