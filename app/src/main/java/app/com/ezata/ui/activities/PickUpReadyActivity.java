package app.com.ezata.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import app.com.ezata.R;
import app.com.ezata.adapter.OnRouteOrderAdapter;
import app.com.ezata.adapter.PickUpReadyAdapter;
import app.com.ezata.databinding.ActivityPickUpReadyBinding;
import app.com.ezata.model.OnRouteOrders;
import app.com.ezata.model.PickUpReady;

public class PickUpReadyActivity extends AppCompatActivity {
    ActivityPickUpReadyBinding binding;
    PickUpReady[] pickup;
    OnRouteOrders[] orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_up_ready);
        binding.ivHistory.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), HistoryActivity.class)));
        binding.ivMenu.setOnClickListener(view -> {
            openNavDrawer();
        });
        binding.ivPrepareOrder.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        initReadyOrderRecyclerview();
        initOnRouteRecyclerview();
    }
    private void openNavDrawer() {
        binding.drawer.openDrawer(Gravity.LEFT);
    }
    private void initOnRouteRecyclerview() {
        orders = new OnRouteOrders[]{
                new OnRouteOrders("KU17A", "Alex T.", R.drawable.white_vehicle, "10 Mins"),
                new OnRouteOrders("JH17A", "David T.", R.drawable.white_vehicle, "14 Mins"),
                new OnRouteOrders("KU98A", "John C.", R.drawable.white_vehicle, "19 Mins"),
                new OnRouteOrders("KU17A", "Alex T.", R.drawable.white_vehicle, "32 Mins")

        };
        binding.rvOnRoute.setHasFixedSize(true);
        binding.rvOnRoute.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        OnRouteOrderAdapter adapter = new OnRouteOrderAdapter(orders);
        binding.rvOnRoute.setAdapter(adapter);
    }

    private void initReadyOrderRecyclerview() {
        pickup = new PickUpReady[]{
                new PickUpReady("J83NW", "Alex T.", R.drawable.vehicle, "John S.", "18 Mins", "11:38 PM", "2356"),
                new PickUpReady("J83NW", "Alex T.", R.drawable.vehicle, "John S.", "18 Mins", "11:38 PM", "2356"),
                new PickUpReady("J83NW", "Alex T.", R.drawable.vehicle, "John S.", "18 Mins", "11:38 PM", "2356"),
                new PickUpReady("J83NW", "Alex T.", R.drawable.vehicle, "John S.", "18 Mins", "11:38 PM", "2356")

        };

        binding.rvPickup.setHasFixedSize(true);
        binding.rvPickup.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        PickUpReadyAdapter adapter = new PickUpReadyAdapter(pickup);
        binding.rvPickup.setAdapter(adapter);
    }
}