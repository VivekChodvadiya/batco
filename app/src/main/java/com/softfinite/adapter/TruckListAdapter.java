package com.softfinite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.softfinite.R;
import com.softfinite.RoomDb.Truck;
import com.softfinite.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by om on 7/7/2017.
 */

public class TruckListAdapter extends RecyclerView.Adapter<TruckListAdapter.MyViewHolder> {

    //    ImageLoader imageLoader;
    public List<Truck> truckData = new ArrayList<>();
    Eventlistener mEventlistener;
    Context context;

    public TruckListAdapter(Context c) {
        this.context = c;
//        imageLoader = Utils.initImageLoader(context);
    }

    public void addAll(List<Truck> mData) {
        truckData.clear();
        truckData.addAll(mData);
        notifyDataSetChanged();
    }

    //    public ResponseData.Slider getItem(int position){
//
//        return  truckData.get(position);
//    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.transaction_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Truck slider = truckData.get(position);
        holder.tvDate.setText(Utils.nullSafe("" + slider.getDate()));
        holder.tvTruckBarcodeNumber.setText(Utils.nullSafe("" + slider.getBarcodenumber()));
        holder.tvTruckNumber.setText(Utils.nullSafe("" + slider.getTrucknumber()));
//
//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mEventlistener != null) {
//
//                    mEventlistener.onItemviewClick(position);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return truckData.size();
    }

    public List<Truck> getAllDAta() {
        return truckData;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTruckNumber)
        TextView tvTruckNumber;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvTruckBarcodeNumber)
        TextView tvTruckBarcodeNumber;
        @BindView(R.id.container)
        View container;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface Eventlistener {

        void onItemviewClick(int position);
    }

    public void setEventlistener(Eventlistener eventlistener) {

        this.mEventlistener = eventlistener;
    }

}
