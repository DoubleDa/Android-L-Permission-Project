package com.dyx.alpp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @Bind(R.id.rv_menu)
    RecyclerView rvMenu;

    private MenuAdapter mMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setHasFixedSize(true);
        rvMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mMenuAdapter = new MenuAdapter(Arrays.asList(getResources().getStringArray(R.array.menu)));
        rvMenu.setAdapter(mMenuAdapter);
        mMenuAdapter.setmOnRvClickListener(new MenuAdapter.OnRvClickListener() {
            @Override
            public void onItemClick(int pos) {
                switch (pos) {
                    case 0:
                        /**
                         * 获取所有权限
                         */
                        getAllPermission();
                        break;
                    case 1:
                        /**
                         * 获取sd卡权限
                         */
                        writeSd();
                        break;
                    case 2:
                        /**
                         * 获取读IMEI权限
                         */
                        getImie();
                        break;
                    case 3:
                        /**
                         * 获取拨打电话权限
                         */
                        callPhone();
                        break;
                }
            }
        });
    }

    private void callPhone() {
        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CALL_PHONE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10086"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onDenied(List<String> permissions) {
                showToast(permissions.toString() + "权限拒绝");
            }
        });
    }

    private void getAllPermission() {
        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.SEND_SMS).build(), new AcpListener() {
            @Override
            public void onGranted() {
                writeSd();
                getImie();
            }

            @Override
            public void onDenied(List<String> permissions) {
                showToast(permissions.toString() + "权限拒绝");
            }
        });
    }

    private void getImie() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (tm != null) {
            showToast("读imei成功：" + tm.getDeviceId());
        }
    }

    private void writeSd() {
        File myFile = getCacheDir("dyx", this);
        if (myFile != null) {
            showToast("写SD成功：" + myFile.getAbsolutePath());
        }
    }

    private File getCacheDir(String msg, Context context) {
        File result;
        if (isSdExist()) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + context.getPackageName() + "/cache/" + msg);
            } else {
                result = new File(cacheDir, msg);
            }
        } else {
            result = new File(context.getCacheDir(), msg);
        }

        if (result.exists() || result.mkdir()) {
            return result;
        } else {
            return null;
        }
    }

    private boolean isSdExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
