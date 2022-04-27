package app.com.ezata.ui.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ezata.R;
import app.com.ezata.adapter.NewOrderAdapter;
import app.com.ezata.databinding.FragmentNewOrderBinding;
import app.com.ezata.model.NewOrder;

public class NewOrderFragment extends Fragment {
    FragmentNewOrderBinding binding;
    NewOrder[] orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_order, container, false);
        View view = binding.getRoot();
        initRecylerview();
        return view;
    }

    private void initRecylerview() {
        orders=new NewOrder[] {
                new NewOrder("J83NW", "Alex T.", "32 Mins","6 item" ),
                new NewOrder("K34LE", "James R.", "32 Mins","6 item" ),
                new NewOrder("K40RP", "Pat W.", "32 Mins","6 item" ),
                new NewOrder("J83NW", "Alex T.", "32 Mins","6 item" )

        };

        binding.rvOrders.setHasFixedSize(true);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        NewOrderAdapter adapter=new NewOrderAdapter(orders,getActivity());
        binding.rvOrders.setAdapter(adapter);

    }
}