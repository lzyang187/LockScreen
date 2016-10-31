package com.lzy.lockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DevicePolicyManager policyManager;
    ComponentName componentName;
    private TextView mTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTV = ((TextView) findViewById(R.id.tv));
        mTV.setOnClickListener(this);
        policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        if (!policyManager.isAdminActive(componentName)) {
            goSetActivity();
        } else {
            systemLock();
        }
    }

    private void goSetActivity() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        startActivityForResult(intent, 1);
    }

    /**
     * 锁屏并关闭屏幕
     */
    private void systemLock() {
        if (policyManager.isAdminActive(componentName)) {
            Window localWindow = getWindow();
            WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
            localLayoutParams.screenBrightness = 0.05F;
            localWindow.setAttributes(localLayoutParams);
            policyManager.lockNow();
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == mTV) {
            goSetActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (RESULT_OK == resultCode) {
                systemLock();
            } else if (RESULT_CANCELED == resultCode) {
                //用户拒绝激活
            }
        }
    }

}
