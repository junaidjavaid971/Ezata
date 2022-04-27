package app.com.ezata.ui.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.ezata.R;
import app.com.ezata.adapter.InProgressOrderAdapter;
import app.com.ezata.databinding.FragmentInProgressBinding;
import app.com.ezata.model.InProgressOrder;

public class InProgressOrderFragment extends Fragment {
    FragmentInProgressBinding binding;
    InProgressOrder[] list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress, container, false);
        View view = binding.getRoot();
        initRecyclerview();
        return view;
    }

    private void initRecyclerview() {
        list = new InProgressOrder[]{
                new InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items"),
                new InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items"),
                new InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items"),
                new InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items")
        };

        binding.rvInProgress.setHasFixedSize(true);
        binding.rvInProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
        InProgressOrderAdapter adapter = new InProgressOrderAdapter(list);
        binding.rvInProgress.setAdapter(adapter);
    }
}