package com.sgl.mywallet;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private Context context;
    private EditText fullName;
    private EditText password;
    private EditText email;
    private EditText rePassword;
    private String selfiName, fileRef, fileRefAbsPath, accountId, name;
    private Spinner accountType;
    private ImageView facePhoto;
    private Boolean cameraEnabled = false;
    private BackendlessUser user;
    private static final int CAMERA_ACTIVITY = 200;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();
        selfiName = "";
        cameraEnabled = getArguments().getBoolean("cameraEnabled");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        password = v.findViewById(R.id.et_password);
        email = v.findViewById(R.id.et_email);
        fullName = v.findViewById(R.id.et_name);
        rePassword = v.findViewById(R.id.et_repassword);
        facePhoto = v.findViewById(R.id.facePic);
        accountType = v.findViewById(R.id.sp_account_type);

        v.findViewById(R.id.camLogo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cameraEnabled) {
                    Toast.makeText(context, "Missing camera permission", Toast.LENGTH_LONG).show();
                    return;
                }
                // Avoid API26 policy restrictions
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                // Call Android image capture
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Set a pointer to the camera file name (path to file)
                selfiName = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        + File.separator + "myPic.jpg";
                Log.i("FILE LOCATION", "onClick: " + selfiName);
                File file = new File(selfiName);
                // Set a pointer to the file location
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                // Start the camera activity and wait for results (when activity is done)
                try {
                    startActivityForResult(intent, CAMERA_ACTIVITY);
                } catch(Exception e) {
                    Log.e("CAMERA ERROR", "onClick: " + e.getMessage());
                }
            }
        });

        v.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateNewUserParameters(email.getText().toString(), fullName.getText().toString(),
                        password.getText().toString(), rePassword.getText().toString(),
                        String.valueOf(accountType.getSelectedItem()))) {
                    // Set the file URL to upload to Backendless
                    Bitmap myImage = BitmapFactory.decodeFile(selfiName);
                    fileRef = UUID.randomUUID().toString().replace("-", "") + ".jpg";
                    Backendless.Files.Android.upload(myImage, Bitmap.CompressFormat.JPEG, 100,
                            fileRef, "faces", new AsyncCallback<BackendlessFile>() {
                                @Override
                                public void handleResponse(BackendlessFile response) {
                                    fileRefAbsPath = response.getFileURL();
                                    buildUserObject();
                                    registerUser();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(context, "Missing picture!", Toast.LENGTH_SHORT).show();
                                    Log.e("IMAGE UPLOAD TO BACKENDLESS FAILURE",
                                            "handleFault: " + fault.getMessage());
                                }
                            });
                } else {
                    resetInputs();
                }
            }
        });
        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CAMERA_ACTIVITY:
                // Convert the image file to a bitmap object
                final File file = new File(selfiName);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                facePhoto.setImageBitmap(bitmap);
                break;

        }
    }

    private void resetInputs() {
        fullName.setText("");
        password.setText("");
        rePassword.setText("");
        email.setText("");
        accountType.setSelection(0);
    }

    private boolean validateNewUserParameters(String uEmail, String uFullName, String uPass, String uRePass, String accountType) {
        if(uFullName.length() < 2) {
            Toast.makeText(context, "first or last name are too short!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(uPass.length() < 5) {
            Toast.makeText(context, "Password is not strong enough! Required at least 5 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if(!uPass.equals(uRePass)) {
                Toast.makeText(context, "Password and RePassword do not match!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if(accountType.isEmpty()) {
            Toast.makeText(context, "Missing account type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!validateEmail(uEmail)) {
            Toast.makeText(context, "Incorrect email format!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateEmail(String mailAddress) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return mailAddress.matches(emailPattern);
    }

    private void buildUserObject() {
        user = new BackendlessUser();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setProperty("name", fullName.getText().toString());
        user.setProperty("image", fileRefAbsPath);
    }

    private void registerUser() {
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_LONG).show();
                name = String.valueOf(response.getProperty("name"));
                setAccountDetails(String.valueOf(response.getUserId()));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                resetInputs();
                Log.e("ERROR", "RegisterFault: " + fault.getMessage());
            }
        });
    }

    private void setAccountDetails(final String userId) {
        final String accType = String.valueOf(accountType.getSelectedItem());
        Backendless.Data.of(Accounts.class).save(new Accounts(userId, accType, 0.0,
                        -20_000.0), new AsyncCallback<Accounts>() {
            @Override
            public void handleResponse(Accounts response) {
                accountId = response.getObjectId();
                user.setProperty("accountId", accountId);
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Log.i("USER UPDATE", "handleResponse: SUCCESS in updating account id");
                        if(accType.equals("Preferred")) {
                            Backendless.Data.of(PreferredAccounts.class).save(new PreferredAccounts(accountId, 2.0, 3.0), new AsyncCallback<PreferredAccounts>() {
                                @Override
                                public void handleResponse(PreferredAccounts response) {
                                    Log.i("PREFERRED ACCOUNT SUCCESS", "handleResponse: SUCCESS");
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.e("PREFERRED ACCOUNT ERROR", "handleFault: Error inserting data to preferred table in backendless" );
                                }
                            });
                        }
                        goToMainActivity(accType);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.i("USER UPDATE", "handleResponse: FAILED to update account id");
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("ACCOUNTS ERROR", "handleFault: Error inserting data to Accounts table in backendless");
            }
        });
    }

    private void goToMainActivity(String accType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("str", "Hi, " + name);
        intent.putExtra("imageFileName", fileRefAbsPath);
        intent.putExtra("accountId", accountId);
        intent.putExtra("accountType", accType);
        startActivity(intent);
    }
}
