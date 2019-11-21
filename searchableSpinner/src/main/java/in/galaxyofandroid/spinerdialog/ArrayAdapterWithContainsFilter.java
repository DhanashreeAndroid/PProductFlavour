package in.galaxyofandroid.spinerdialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArrayAdapterWithContainsFilter<T> extends ArrayAdapter {

    private List<T> items = null;
    private List<T> arraylist;

    public ArrayAdapterWithContainsFilter(Activity context, int items_view, List<T> items) {
        super(context,items_view,items);
        this.items = items;
        this.arraylist = new ArrayList<T>();
        this.arraylist.addAll(items);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    // Filter Class
    public void getContainsFilter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arraylist);
        }
        else
        {
            for (T item : arraylist)
            {
                if (item.toString().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
