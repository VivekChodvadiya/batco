package com.softfinite;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.google.gson.Gson;
import com.softfinite.RoomDb.Truck;
import com.softfinite.RoomDb.TruckDao;
import com.softfinite.RoomDb.TruckRoomDatabase;
import com.softfinite.RoomDb.TruckViewModel;
import com.softfinite.adapter.TruckListAdapter;
import com.softfinite.utils.CameraSelectorDialogFragment;
import com.softfinite.utils.Constant;
import com.softfinite.utils.Debug;
import com.softfinite.utils.ExitStrategy;
import com.softfinite.utils.FormatSelectorDialogFragment;
import com.softfinite.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class HomeActivity extends BaseActivity implements
        ZBarScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener {

    @BindView(R.id.editTruckNumber)
    EditText editTruckNumber;
    @BindView(R.id.rvTruckData)
    RecyclerView rvTruckData;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;

    private TruckViewModel truckViewModel;
    TruckListAdapter truckListAdapter;
    CharSequence todayDate;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initDrawer();
        init();
    }

    private void init() {
        setTitleText("Home");

        truckViewModel = ViewModelProviders.of(this).get(TruckViewModel.class);
        Date d = new Date();
        todayDate = DateFormat.format("dd-MM-yyyy", d.getTime());


        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        setupFormats();
        contentFrame.addView(mScannerView);

        rvTruckData.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTruckData.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvTruckData.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));

        truckListAdapter = new TruckListAdapter(getActivity());
        rvTruckData.setAdapter(truckListAdapter);

        truckViewModel.getFromTruckAndDate(editTruckNumber.getText().toString().trim(), todayDate.toString()).observe(getActivity(), new Observer<List<Truck>>() {
            @Override
            public void onChanged(@Nullable final List<Truck> words) {
                // Update the cached copy of the words in the adapter.
                truckListAdapter.addAll(words);
                refreshPlaceHolder();
            }
        });

//        btnApply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                truckViewModel.getFromTruckAndDate(editTruckNumber.getText().toString().trim(), todayDate.toString()).observe(getActivity(), new Observer<List<Truck>>() {
//                    @Override
//                    public void onChanged(@Nullable final List<Truck> words) {
//                        // Update the cached copy of the words in the adapter.
//                        truckListAdapter.addAll(words);
//                        refreshPlaceHolder();
//                    }
//                });
//            }
//        });

        tvCount.setText("0");

        editTruckNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truckViewModel.getFromTruckAndDate(editTruckNumber.getText().toString().trim(), todayDate.toString()).observe(getActivity(), new Observer<List<Truck>>() {
                    @Override
                    public void onChanged(@Nullable final List<Truck> words) {
                        // Update the cached copy of the words in the adapter.
                        truckListAdapter.addAll(words);
                        if (truckListAdapter != null) {
                            if (truckListAdapter.getAllDAta() != null) {
                                tvCount.setText("" + truckListAdapter.getAllDAta().size());
                            }
                        }
                        refreshPlaceHolder();
                    }
                });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder data = new StringBuilder();
                for (int i = 0; i < truckListAdapter.getAllDAta().size(); i++) {
                    data.append(truckListAdapter.getAllDAta().get(i).getBarcodenumber());
                    data.append("\n");
                }
                generateNoteOnSD(getActivity(), todayDate + editTruckNumber.getText().toString().trim(), data.toString());
            }
        });

        truckListAdapter.setEventlistener(new TruckListAdapter.Eventlistener() {
            @Override
            public void onItemviewClick(int position) {

            }

            @Override
            public void onItemviewDeleteClick(int position) {
                Truck data = truckListAdapter.getItem(position);
                deleteRecord(data.getDate(), data.getTrucknumber(), data.getBarcodenumber(), position);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);


        if (mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < BarcodeFormat.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(BarcodeFormat.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if (mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if (mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            case R.id.menu_formats:
                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(this, mSelectedIndices);
                fragment.show(getSupportFragmentManager(), "format_selector");
                return true;
            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                DialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
                cFragment.show(getSupportFragmentManager(), "camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep);
            mediaPlayer.start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName()).setTitle("Scan Result").setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validate()) {
                    try {
                        Truck truck = new Truck(editTruckNumber.getText().toString().trim(), todayDate.toString(), rawResult.getContents());
                        truckViewModel.insert(truck);
                        dialog.dismiss();
                        Debug.e("truck :", new Gson().toJson(truck));
                    } catch (Exception e) {
                        showToast("Something went wrong.. Data Not Inserted", Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resumeCamView();
                truckViewModel.getFromTruckAndDate(editTruckNumber.getText().toString().trim(), todayDate.toString()).observe(getActivity(), new Observer<List<Truck>>() {
                    @Override
                    public void onChanged(@Nullable final List<Truck> words) {
                        // Update the cached copy of the words in the adapter.
                        truckListAdapter.addAll(words);
                        refreshPlaceHolder();
                    }
                });
            }
        }).show();
//        showMessageDialog("Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName(), rawResult.getContents());
    }

//    public void showMessageDialog(String message, String contains) {
//        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this, contains);
//        fragment.show(getSupportFragmentManager(), "scan_results");
//    }

    public void resumeCamView() {
        mScannerView.resumeCameraPreview(this);
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Constant.FOLDER_RIDEINN_PATH);
            if (!root.exists()) {
                root.mkdirs();
            } else {
//                root.delete();
            }
            File gpxfile = new File(root, sFileName + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "File Saved Successfully : " + sFileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            } else {
                if (ExitStrategy.canExit()) {
                    super.onBackPressed();
                } else {
                    ExitStrategy.startExitDelay(2000);
                    Toast.makeText(getActivity(), getString(R.string.exit_msg),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (editTruckNumber.getText().toString().trim().length() <= 0) {
            mScannerView.stopCamera();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Enter Truck Number First, Without truck number data will not save.").setTitle("Alert").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editTruckNumber.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editTruckNumber, InputMethodManager.SHOW_IMPLICIT);
                    mScannerView.startCamera();
                    dialog.dismiss();
                }
            }).show();
//            showToast("Enter Truck Number", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    private void refreshPlaceHolder() {
        if (rvTruckData.getAdapter().getItemCount() == 0) {
            llPlaceHolder.setVisibility(View.VISIBLE);
            rvTruckData.setVisibility(View.GONE);
        } else {
            llPlaceHolder.setVisibility(View.GONE);
            rvTruckData.setVisibility(View.VISIBLE);
        }
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final TruckDao mDao;
        String date;
        String truckNumber;
        String barcode;

        PopulateDbAsync(TruckRoomDatabase db, String date, String truckNumber, String barcode) {
            mDao = db.truckDao();
            this.date = date;
            this.truckNumber = truckNumber;
            this.barcode = barcode;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteFromDateAnd(date, truckNumber, barcode);

//            Truck truck = new Truck("Hello");
//            mDao.insert(truck);
//            truck = new Truck("World");
//            mDao.insert(truck);
            return null;
        }
    }

    private void deleteRecord(String date, String truckNumber, String barcode, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete this record? \n\nDate : " + date + "\nTruck Number : " + truckNumber + "\nBarcode : " + barcode).setTitle("Alert").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TruckRoomDatabase db = TruckRoomDatabase.getDatabase(getApplication());
                new PopulateDbAsync(db, date, truckNumber, barcode).execute();
                truckListAdapter.remove(position);
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