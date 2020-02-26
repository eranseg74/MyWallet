package com.sgl.mywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class UserActionsActivity extends AppCompatActivity {

    Context context;
    String accountType, action, accountId;
    double negInterest, posInterest, overdraftLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_actions);
        setPointer();
    }

    private void setPointer() {
        this.context = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            accountType = extras.getString("accountType");
            action = extras.getString("action");
            accountId = extras.getString("accountId");
        }
        createAccountObj(accountType);
    }

    private void createAccountObj(final String accountType) {

        Backendless.Data.of(Accounts.class).findById(accountId, new AsyncCallback<Accounts>() {
            @Override
            public void handleResponse(Accounts response) {
                if(accountType.equals("Preferred")) {
                    String whereClause = "accountId = " + accountId;
                    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                    queryBuilder.setWhereClause(whereClause);
                    Backendless.Data.of(PreferredAccounts.class).find(queryBuilder, new AsyncCallback<List<PreferredAccounts>>() {
                        @Override
                        public void handleResponse(List<PreferredAccounts> response) {
                            if(response.size() > 0) {
                                //PreferredBankAccount account
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void deposit(double amount) {

    }
}
