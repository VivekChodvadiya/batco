package com.softfinite;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.softfinite.RoomDb.TruckDao;
import com.softfinite.RoomDb.TruckRoomDatabase;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClearDataActivity extends BaseActivity {


    @BindView(R.id.rvAllData)
    RecyclerView rvAllData;
    @BindView(R.id.btnClearDatabase)
    Button btnClearDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_database);
        ButterKnife.bind(this);

        initBack();
        init();
    }

    private void init() {
        setTitleText("Clear Database");

        btnClearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
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

}
