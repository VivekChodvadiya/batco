package com.softfinite;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.softfinite.adapter.FilesAdapter;
import com.softfinite.utils.Constant;
import com.softfinite.utils.Utils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileRecordsActivity extends BaseActivity {

    @BindView(R.id.recyclerViewFiles)
    RecyclerView recyclerViewFiles;
    @BindView(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;
    FilesAdapter filesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_records);
        ButterKnife.bind(this);
        initBack();
        init();
    }

    private void init() {
        setTitleText("Files");

        recyclerViewFiles.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFiles.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        recyclerViewFiles.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));

        filesAdapter = new FilesAdapter(getActivity());
        recyclerViewFiles.setAdapter(filesAdapter);
        filesAdapter.setEventlistener(new FilesAdapter.Eventlistener() {
            @Override
            public void onItemviewClick(int position) {
                File file = filesAdapter.getItem(position);
                openFile(file);
            }

            @Override
            public void onItemShareClick(int position) {
                File file = filesAdapter.getItem(position);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
            }

            @Override
            public void onItemDeleteClick(int position) {
                File file = filesAdapter.getItem(position);
                try {
                    deleteFile(file, position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        getListFiles();
    }

    private void getListFiles() {
//        String path = Environment.getExternalStorageDirectory().toString() + "/batco";
        String path = Constant.FOLDER_RIDEINN_PATH;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();

            ArrayList<File> fileArrayList = new ArrayList<>();
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                fileArrayList.add(files[i]);
                Log.e("Files", "FileName:" + files[i].getName());
            }
            filesAdapter.addAll(fileArrayList);
        }
        refreshPlaceholder();
    }

    private void openFile(File f) {
//        Uri pathfile = FileProvider.getUriForFile(
//                getActivity(),
//                getActivity().getApplicationContext()
//                        .getPackageName() + ".provider", f);
        Uri pathfile = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", f);
//        Uri pathfile = Uri.fromFile(f);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfOpenintent.setDataAndType(pathfile, "text/plain");
        try {
            startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refreshPlaceholder() {
        if (recyclerViewFiles.getAdapter().getItemCount() == 0) {
            recyclerViewFiles.setVisibility(View.GONE);
            llPlaceHolder.setVisibility(View.VISIBLE);
        } else {
            recyclerViewFiles.setVisibility(View.VISIBLE);
            llPlaceHolder.setVisibility(View.GONE);
        }
    }

    private void deleteFile(File file, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this file?").setTitle("Are you sure?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    file.delete();
                    filesAdapter.remove(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
