package app.com.ezata.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import app.com.ezata.R;
import app.com.ezata.adapter.CustomAdapter;
import app.com.ezata.databinding.ActivityMainBinding;
import app.com.ezata.ui.fragments.InProgressOrderDetailsFragment;
import app.com.ezata.ui.fragments.InProgressOrderFragment;
import app.com.ezata.ui.fragments.NewOrderFragment;
import app.com.ezata.ui.fragments.OrderDetailFragment;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ActivityMainBinding binding;
    int state[] = {R.drawable.ic_active, R.drawable.ic_pause, R.drawable.ic_inactive};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.inprogress.setVisibility(View.GONE);
        binding.ivPrepareFoodSelect.setVisibility(View.VISIBLE);
        binding.ivPrepareOrder.setVisibility(View.GONE);

        binding.drawer.findViewById(R.id.ivActive).setOnClickListener(v -> {
            binding.drawer.findViewById(R.id.layoutNavigationItems).setVisibility(View.GONE);
            binding.drawer.findViewById(R.id.layouStatus).setVisibility(View.VISIBLE);
        });
        binding.ivHistory.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), HistoryActivity.class)));
        binding.ivMenu.setOnClickListener(view -> {
            openNavDrawer();
        });
        setNewOrderBgColor();
        setOrderFragment();
        setOrderDetails();


        binding.llNewOrders.setOnClickListener(view -> {
            binding.inprogress.setVisibility(View.GONE);
            binding.newOrders.setVisibility(View.VISIBLE);

            setNewOrderBgColor();
            Drawable inProgress = binding.llinProgress.getBackground();
            inProgress = DrawableCompat.wrap(inProgress);
            DrawableCompat.setTint(inProgress, Color.rgb(39, 39, 39));

            setOrderFragment();
            setOrderDetails();
        });

        binding.llinProgress.setOnClickListener(view -> {
            setInProgressBgColor();
            Drawable newOrder = binding.llNewOrders.getBackground();
            newOrder = DrawableCompat.wrap(newOrder);
            DrawableCompat.setTint(newOrder, Color.rgb(39, 39, 39));

            setInProgressFragment();
        });

        binding.ivPickupReady.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), PickUpReadyActivity.class));

        });

        binding.ivPrepareOrder.setOnClickListener(view -> {
            binding.ivPrepareOrder.setVisibility(View.GONE);
            binding.ivPrepareFoodSelect.setVisibility(View.VISIBLE);
            binding.ivPickupReady.setVisibility(View.VISIBLE);
            binding.ivSelectPickup.setVisibility(View.GONE);
        });
    }

    private void openNavDrawer() {
        binding.drawer.openDrawer(Gravity.LEFT);
    }

    private void setInProgressBgColor() {
        Drawable inProgress = binding.llinProgress.getBackground();
        inProgress = DrawableCompat.wrap(inProgress);
        DrawableCompat.setTint(inProgress, Color.rgb(104, 107, 113));
    }

    private void setNewOrderBgColor() {
        Drawable linear = binding.llNewOrders.getBackground();
        linear = DrawableCompat.wrap(linear);
        DrawableCompat.setTint(linear, Color.rgb(104, 107, 113));
    }

    private void setInProgressFragment() {
        binding.inprogress.setVisibility(View.VISIBLE);
        binding.newOrders.setVisibility(View.GONE);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout1, new InProgressOrderFragment());
        ft.replace(R.id.framelayout, new InProgressOrderDetailsFragment());
        ft.commit();


    }

    private void setOrderDetails() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, new OrderDetailFragment());
        ft.commit();
    }

    private void setOrderFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout1, new NewOrderFragment());
        ft.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.select_merchant_state, null);
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}