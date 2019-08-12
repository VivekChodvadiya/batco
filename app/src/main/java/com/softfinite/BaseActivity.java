package com.softfinite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
import com.softfinite.utils.AsyncProgressDialog;
import com.softfinite.utils.Constant;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
//import com.nostra13.universalimageloader.core.ImageLoader;


public class BaseActivity extends AppCompatActivity {
    AsyncProgressDialog ad;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.FINISH_ACTIVITY);

        commonReciever = new MyEventServiceReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                commonReciever, intentFilter);

    }

    Drawer result;

    public void initDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this).withCloseOnClick(true).withSelectedItemByPosition(-1)
                .withDrawerGravity(Gravity.LEFT)
                .withHeader(R.layout.nav_header_main)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withSelectable(false),
                        new PrimaryDrawerItem().withName("Dummy Activity").withSelectable(false),
                        new PrimaryDrawerItem().withName("Logout").withSelectable(false)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 1) {
                            if (getActivity() instanceof HomeActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        } else if (position == 2) {
                            if (getActivity() instanceof DummyActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        DummyActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        }
//                        } else if (position == 3) {
//                            if (getActivity() instanceof YourWalletActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        YourWalletActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 4) {
//                            if (getActivity() instanceof TransferAmountActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        TransferAmountActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 5) {
//                            if (getActivity() instanceof MyBussinessActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        MyBussinessActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 6) {
//                            if (getActivity() instanceof OurShopActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        OurShopActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 7) {
//                            if (getActivity() instanceof TransactionActivity) {
//                                hideMenu(true);
//                            } else {
//                                Intent intent = new Intent(getActivity(),
//                                        TransactionActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(intent);
//                                hideMenu(false);
//                                finishActivity();
//                            }
//                        } else if (position == 8) {
//                            confirmLogout();
//                        }
                        return true;
                    }
                })
                .build();

        ImageView imgMenu = (ImageView) findViewById(R.id.imgMenu);
        if (imgMenu != null) {
            imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (result.isDrawerOpen()) {
                        result.closeDrawer();
                    } else {
                        result.openDrawer();
                    }
                }
            });
        }

//        initMenuItems();
//        fillProfileData();
    }

//    private void confirmLogout() {
//
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
//                .title(R.string.logout_title)
//                .content(R.string.logout_msg)
//                .positiveText(R.string.btn_yes)
//                .negativeText(R.string.btn_no)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//                        showToast("Logged Out", Toast.LENGTH_SHORT);
////                        Intent i = new Intent(getActivity(), LoginActivity.class);
////                        startActivity(i);
////                        finish();
//                    }
//                }).onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//
//                    }
//                });
//
//        MaterialDialog dialog = builder.build();
//        dialog.show();
//    }

    private void hideMenu(boolean b) {
        try {
//            if (b)
            result.closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishActivity() {
        if ((getActivity() instanceof HomeActivity)) {

        } else {
            getActivity().finish();
        }

    }

//    ImageLoader imageLoader;


    MyEventServiceReciever commonReciever;

    class MyEventServiceReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase(
                        Constant.FINISH_ACTIVITY)) {
                    finish();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BaseActivity getActivity() {
        return this;
    }

    private TextView tvTitleText;

    public void setTitleText(String text) {
        try {

            if (tvTitleText == null)
                tvTitleText = (TextView) findViewById(R.id.tvTitleText);
            tvTitleText.setText(text);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(String msg) {
        try {
            if (ad != null && ad.isShowing()) {
                return;
            }

            ad = AsyncProgressDialog.getInstant(getActivity());
            ad.show(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String msg) {
        try {
            ad.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (ad != null) {
                ad.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = super.onKeyDown(keyCode, event);

        // Eat the long press event so the keyboard doesn't come up.
        if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()) {
            return true;
        }

        return handled;
    }

    Toast toast;

    public void showToast(final String text, final int duration) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .unregisterReceiver(commonReciever);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }


    BaseCallback baseCallback;

    public void setBaseCallback(BaseCallback baseCallback) {
        this.baseCallback = baseCallback;
    }

    interface BaseCallback {
        void onMasterDataLoad();
    }


}
