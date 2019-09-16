package com.softfinite;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.softfinite.RoomDb.Truck;
import com.softfinite.RoomDb.TruckDao;
import com.softfinite.RoomDb.TruckRoomDatabase;
import com.softfinite.RoomDb.TruckViewModel;
import com.softfinite.adapter.AllTruckDataAdapter;
import com.softfinite.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllDataActivity extends BaseActivity {


    @BindView(R.id.rvAllData)
    RecyclerView rvAllData;
    @BindView(R.id.btnClearDatabase)
    Button btnClearDatabase;
    @BindView(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;

    AllTruckDataAdapter allTruckDataAdapter;
    private TruckViewModel truckViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_database);
        ButterKnife.bind(this);
        initBack();
        init();
    }

    private void init() {
        setTitleText("All Database");

        truckViewModel = ViewModelProviders.of(this).get(TruckViewModel.class);


        btnClearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        rvAllData.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAllData.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvAllData.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));

        allTruckDataAdapter = new AllTruckDataAdapter(getActivity());
        rvAllData.setAdapter(allTruckDataAdapter);

        allTruckDataAdapter.setEventlistener(new AllTruckDataAdapter.Eventlistener() {
            @Override
            public void onItemviewClick(int position) {

            }

            @Override
            public void onItemviewDeleteClick(int position) {

            }
        });


        truckViewModel.getAllWordsLive().observe(getActivity(), new Observer<List<Truck>>() {
            @Override
            public void onChanged(@Nullable final List<Truck> words) {
                // Update the cached copy of the words in the adapter.
                allTruckDataAdapter.addAll(words);
                refreshPlaceHolder();
            }
        });
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TruckDao mDao;

        PopulateDbAsync(TruckRoomDatabase db) {
            mDao = db.truckDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteAll();

//            Truck truck = new Truck("Hello");
//            mDao.insert(truck);
//            truck = new Truck("World");
//            mDao.insert(truck);
            return null;
        }
    }

    private void clearData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to clear all file?").setTitle("Alert").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TruckRoomDatabase db = TruckRoomDatabase.getDatabase(getApplication());
                new PopulateDbAsync(db).execute();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void refreshPlaceHolder() {
        if (rvAllData.getAdapter().getItemCount() == 0) {
            llPlaceHolder.setVisibility(View.VISIBLE);
            rvAllData.setVisibility(View.GONE);
        } else {
            llPlaceHolder.setVisibility(View.GONE);
            rvAllData.setVisibility(View.VISIBLE);
        }
    }
}
