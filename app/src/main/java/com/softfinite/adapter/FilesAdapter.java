package com.softfinite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.softfinite.R;
import com.softfinite.utils.Utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by om on 7/7/2017.
 */

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    //    ImageLoader imageLoader;
    public List<File> files = new ArrayList<>();
    Eventlistener mEventlistener;
    Context context;

    public FilesAdapter(Context c) {
        this.context = c;
//        imageLoader = Utils.initImageLoader(context);
    }

    public void addAll(List<File> mData) {
        files.addAll(mData);
        notifyDataSetChanged();
    }

    public File getItem(int position) {
        return files.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.files_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final File slider = files.get(position);
        holder.tvFileName.setText(Utils.nullSafe("" + slider.getName()));
//        holder.tvslidecaption.setText(Utils.nullSafe("" + slider.slidecaption));
//
//        if (!StringUtils.isEmpty(slider.slideimage)) {
//            imageLoader.displayImage(slider.slideimage, holder.imgView);
//        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {
                    mEventlistener.onItemviewClick(position);
                }
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {
                    mEventlistener.onItemShareClick(position);
                }
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {
                    mEventlistener.onItemDeleteClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.tvJobId)
//        TextView tvJobId;
//        @BindView(R.id.tvNeedRev)
//        TextView tvslidecaption;
//        @BindView(R.id.tvSupervisor)
//        TextView tvSupervisor;
//        @BindView(R.id.tvDate)
//        TextView tvDate;
//        @BindView(R.id.tvStatus)
//        TextView tvStatus;
        @BindView(R.id.container)
        View container;
        @BindView(R.id.tvFileName)
        TextView tvFileName;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.imgDelete)
        ImageView imgDelete;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface Eventlistener {
        void onItemviewClick(int position);

        void onItemShareClick(int position);

        void onItemDeleteClick(int position);
    }

    public void setEventlistener(Eventlistener eventlistener) {
        this.mEventlistener = eventlistener;
    }

}
