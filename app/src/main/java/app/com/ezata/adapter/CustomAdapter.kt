package app.com.ezata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import app.com.ezata.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int state[];
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] state) {
        this.context = applicationContext;
        this.state = state;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return state.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.ivActive);
        icon.setImageResource(state[i]);
        return view;
    }
}
