package com.sgl.mywallet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Context context;
    private EditText password;
    private EditText email;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        password = v.findViewById(R.id.et_password);
        email = v.findViewById(R.id.et_email);

        v.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                Backendless.UserService.login(txtEmail, txtPassword, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_LONG).show();
                        goToMainActivity(response);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("LOGIN FAILURE", "LoginFault: " + fault.getMessage());
                    }
                });
            }
        });
        return v;
    }

    private void goToMainActivity(BackendlessUser user) {
        String accountId = String.valueOf(user.getProperty("accountId"));
        //Backendless.Data.of(Accounts.class).
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("str", "Hi, " + user.getProperty("name"));
        intent.putExtra("imageFileName", user.getProperty("image").toString());
        intent.putExtra("accountId", accountId);
        startActivity(intent);
    }


}
