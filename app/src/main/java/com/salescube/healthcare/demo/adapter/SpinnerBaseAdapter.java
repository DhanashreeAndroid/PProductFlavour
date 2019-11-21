package com.salescube.healthcare.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.view.vBaseSpinner;

import java.util.ArrayList;

public class SpinnerBaseAdapter extends RecyclerView.Adapter<SpinnerBaseAdapter.ViewHolder>
        implements Filterable {

    private Context mContext;
    private ArrayList<vBaseSpinner> mList;
    private ArrayList<vBaseSpinner> mFilterList;
    private ISpinnerItemClick mListener;
    private String mLabel;

    public SpinnerBaseAdapter(Context context, String label, ISpinnerItemClick listner) {
        this.mContext = context;
        this.mListener = listner;
        this.mLabel = label;
    }

    public void setData(ArrayList<vBaseSpinner> list) {
        this.mList = list;
        this.mFilterList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.city_route_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        vBaseSpinner city = mFilterList.get(position);
        holder.tvTitle.setText(city.getName());

        // --All-- item removed because all selections make compulsory
       /* if (position == 0) {
            if (!TextUtils.isEmpty(mLabel)) {
                holder.tvAll.setVisibility(View.VISIBLE);
                holder.allDivider.setVisibility(View.VISIBLE);
            } else {
                holder.tvAll.setVisibility(View.GONE);
                holder.allDivider.setVisibility(View.GONE);
            }
        } else {
            holder.tvAll.setVisibility(View.GONE);
            holder.allDivider.setVisibility(View.GONE);
        }*/

        holder.tvAll.setVisibility(View.GONE);
        holder.allDivider.setVisibility(View.GONE);

        holder.tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSpinnerItemClick(null, holder.tvAll.getText().toString());
            }
        });

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSpinnerItemClick(mFilterList.get(position), mLabel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mFilterList != null && mFilterList.size() > 0) {
            return mFilterList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilterList = mList;
                } else {
                    ArrayList<vBaseSpinner> filteredList = new ArrayList<>();
                    for (vBaseSpinner row : mList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<vBaseSpinner>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvAll;
        LinearLayout mMainContainer;
        View allDivider;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvName);
            tvAll = itemView.findViewById(R.id.tvAll);
            allDivider = itemView.findViewById(R.id.all_divider);
            mMainContainer = itemView.findViewById(R.id.item_container);
        }
    }
}
