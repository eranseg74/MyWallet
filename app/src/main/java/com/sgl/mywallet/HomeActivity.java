package com.sgl.mywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Context context;
    private static final int CAMERA_PERMISSION = 101;
    private boolean cameraEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkUserPermissions();
        ViewPager viewPager = findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager()
        , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Fragment registerFragment = new RegisterFragment();
        Bundle args  = new Bundle();
        args.putBoolean("cameraEnabled", cameraEnabled);
        registerFragment.setArguments(args);
        pagerAdapter.addFragmet(new LoginFragment());
        pagerAdapter.addFragmet(registerFragment);
        viewPager.setAdapter(pagerAdapter);
        setPointer();
    }

    private void setPointer() {
        this.context = this;
        Backendless.initApp(context, Defaults.APPLICATION_ID, Defaults.API_KEY);
        initRegularAndBusinessAccountsData();
    }

    private void initRegularAndBusinessAccountsData() {
        final List<AccountTypes> accounts = new ArrayList<>();
        AccountTypes regular = new AccountTypes("Regular", 2.0, 3.0);
        accounts.add(regular);

        AccountTypes business = new AccountTypes("Business", 0.0, 0.0);
        accounts.add(business);
        Backendless.Data.of(AccountTypes.class).getObjectCount(new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                if(response.intValue() > 0) {
                    Backendless.Data.of(AccountTypes.class).create(accounts, new AsyncCallback<List<String>>() {
                        @Override
                        public void handleResponse(List<String> response) {
                            Log.i("ACCOUT_TYPE_TABLE", "handleResponse: Table created successfully");
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("ACCOUT_TYPE_TABLE", "handleResponse: Failed to create table");
                            Log.e("ACCOUT_TYPE_TABLE", "handleFault: " + fault.getMessage());
                        }
                    });
                }
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Log.i("REQUEST ERROR", "handleFault: Failed to get number of items in the AccountType table");
            }
        });
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        AuthenticationPagerAdapter(FragmentManager fm, int n) {
            super(fm, n);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

    // Check the user permissions and request permissions if any permission is missing
    private void checkUserPermissions() {
        List<String> missingPermissions = new ArrayList<>();
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readExternalStorePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternalStorePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int getMMSPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_MMS);

        if(cameraPermission != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.add(Manifest.permission.CAMERA);
        } else {
            cameraEnabled = true;
        }
        if(readExternalStorePermission != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(writeExternalStorePermission != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(getMMSPermission != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.add(Manifest.permission.RECEIVE_MMS);
        }
        if(!missingPermissions.isEmpty()) {
            // Ask for permission
            ActivityCompat.requestPermissions(this, missingPermissions.toArray(new String[missingPermissions.size()]), CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION:
                cameraEnabled = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}
