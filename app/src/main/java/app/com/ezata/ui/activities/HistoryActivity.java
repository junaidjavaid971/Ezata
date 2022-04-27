package app.com.ezata.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import app.com.ezata.OnItemClicked;
import app.com.ezata.R;
import app.com.ezata.adapter.HistoryAdapter;
import app.com.ezata.adapter.OrderDetailAdapter;
import app.com.ezata.databinding.ActivityHistoryBinding;
import app.com.ezata.model.HistoryDetails;
import app.com.ezata.model.OrderDetail;

public class HistoryActivity extends AppCompatActivity implements OnItemClicked {
    ActivityHistoryBinding binding;
    HistoryDetails[] details;
    OrderDetail[] orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        setHistoryData();
        binding.ivMenu.setOnClickListener(view -> {
            openNavDrawer();
        });
        binding.btnSort.setOnClickListener(view1 -> {

        });
        binding.ivPrepareOrder.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        binding.ivPickupReady.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), PickUpReadyActivity.class)));
    }

    private void openNavDrawer() {
        binding.drawer.openDrawer(Gravity.LEFT);
    }

    private void setHistoryData() {
        details = new HistoryDetails[]{
                new HistoryDetails("03/12/22", "11:38 PM", "J83NW", "Leonard N.", "Delivery", "32.23", "Completed"),
                new HistoryDetails("03/12/22", "11:38 PM", "J83NW", "Leonard N.", "Delivery", "32.23", "Completed"),
                new HistoryDetails("03/12/22", "11:38 PM", "J83NW", "Leonard N.", "Delivery", "32.23", "Completed"),
                new HistoryDetails("03/12/22", "11:38 PM", "J83NW", "Leonard N.", "Delivery", "32.23", "Completed"),
                new HistoryDetails("03/12/22", "11:38 PM", "J83NW", "Leonard N.", "Delivery", "32.23", "Completed"),
                new HistoryDetails("03/12/22", "11:38 PM", "J83NW", "Leonard N.", "Delivery", "32.23", "Completed")
        };

        binding.rvHistory.setHasFixedSize(true);
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        HistoryAdapter adapter = new HistoryAdapter(details, this);
        binding.rvHistory.setAdapter(adapter);
    }

    private void showAlertDialog() {
        orders = new OrderDetail[]{
                new OrderDetail("1x", "Cheeseburger", "$5.78", "- No Ketchup"),
                new OrderDetail("2x", "Fries", "$2.39", ""),
                new OrderDetail("1x", "Milk Shake", "$3.25", ""),
                new OrderDetail("1x", "Garden Salad", "$3.25", ""),
                new OrderDetail("1x", "Diet Coke", "$0.89", "")

        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater1 = getLayoutInflater();
        View dialogView = inflater1.inflate(R.layout.dialog_history, null);
        RecyclerView rvOrders = dialogView.findViewById(R.id.rvFoodOrder);
        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public void onItemSelect(int position) {
        showAlertDialog();

    }
}