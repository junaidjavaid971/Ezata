package app.com.ezata.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
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
    int[] state = {R.drawable.ic_active, R.drawable.ic_pause, R.drawable.ic_inactive};

    public static final String SHARED_PREFS = "sharedPrefs";
    String State = "s", text, paused = "2", pauseText = "2";
    ImageView ivActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ivActive = binding.drawer.findViewById(R.id.ivActive);
        binding.inprogress.setVisibility(View.GONE);
        binding.ivPrepareFoodSelect.setVisibility(View.VISIBLE);
        binding.ivPrepareOrder.setVisibility(View.GONE);

        binding.drawer.findViewById(R.id.ivActive).setOnClickListener(v -> {
            binding.drawer.findViewById(R.id.layoutNavigationItems).setVisibility(View.GONE);
            binding.drawer.findViewById(R.id.layouStatus).setVisibility(View.VISIBLE);
        });

        manageStateClick();
        loadData();
        updateViews();

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

    private void manageStateClick() {
        binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeLayout).setOnClickListener(view -> {
            ivActive.setImageResource(state[0]);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.VISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.INVISIBLE);
            activeState();
        });
        binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseLayout).setOnClickListener(view -> {
            ivActive.setImageResource(state[1]);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.VISIBLE);
            pauseState();
        });

        binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                .findViewById(R.id.tv15M).setOnClickListener(view1 -> {
            ivActive.setImageResource(state[1]);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.VISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv15M).setBackgroundResource(R.drawable.ic_black_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle);
            pauseText = "1";
            pauseState();
        });

        binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                .findViewById(R.id.tv30M).setOnClickListener(view1 -> {
            ivActive.setImageResource(state[1]);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.VISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv30M).setBackgroundResource(R.drawable.ic_black_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle);
            pauseText = "2";
            pauseState();
        });

        binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                .findViewById(R.id.tv1H).setOnClickListener(view1 -> {
            ivActive.setImageResource(state[1]);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.VISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv1H).setBackgroundResource(R.drawable.ic_black_circle);
            pauseText = "3";
            pauseState();
        });

        binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveLayout).setOnClickListener(view -> {
            ivActive.setImageResource(state[2]);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.VISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.INVISIBLE);
            inactiveState();
        });
    }

    private void activeState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(State, "Active");
        editor.apply();
    }

    private void pauseState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(State, "Pause");
        editor.putString(paused, pauseText);
        editor.apply();
    }


    private void inactiveState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(State, "Inactive");
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(State, "Active");
        pauseText = sharedPreferences.getString(paused, pauseText);
    }

    public void updateViews() {
        if (text.equals("Active")) {
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.VISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.INVISIBLE);
            ivActive.setImageResource(state[0]);
        }
        if (text.equals("Pause")) {
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.VISIBLE);
            ivActive.setImageResource(state[1]);
        }

        if (text.equals("Inactive")) {
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.activeCheck).setVisibility(View.INVISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.inactiveCheck).setVisibility(View.VISIBLE);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.pauseCheck).setVisibility(View.INVISIBLE);
            ivActive.setImageResource(state[2]);
        }
        if (pauseText.equals("1")) {
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv15M).setBackgroundResource(R.drawable.ic_black_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle);
        }
        if (pauseText.equals("2")) {
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv30M).setBackgroundResource(R.drawable.ic_black_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle);
        }
        if (pauseText.equals("3")) {
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle);
            binding.drawer.findViewById(R.id.layouStatus).findViewById(R.id.timerLayout)
                    .findViewById(R.id.tv1H).setBackgroundResource(R.drawable.ic_black_circle);
        }
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