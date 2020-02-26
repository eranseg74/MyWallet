package com.sgl.mywallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context;
    private String httpReq;
    List<Currency> currenciesList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String imagePath, uName, accountId, accountType;
    ImageView userFace;
    TextView txtBalance;
    double balance = 111.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPointer();
    }

    private void setPointer() {
        this.context = this;
        currenciesList = new ArrayList<>();
        userFace = findViewById(R.id.uFace);
        txtBalance = findViewById(R.id.txtBalance);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            imagePath = extras.getString("imageFileName");
            uName = extras.getString("str");
            accountId = extras.getString("accountId");
            accountType = extras.getString("accountType");
        }
        Picasso.get().load(imagePath).placeholder(R.drawable.anonymous).into(userFace, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("SUCCESS", "onSuccess: Succeeded loading face image.");
            }

            @Override
            public void onError(Exception e) {
                Log.e("ERROR", "onError: Failed to load face. " + e.getLocalizedMessage());
            }
        });
        getCurrencies();
        updateAccountDetails();
        setButtonsListeners();

    }

    private void updateAccountDetails() {

        Backendless.Data.of(Accounts.class).findById(accountId, new AsyncCallback<Accounts>() {
            @Override
            public void handleResponse(Accounts response) {
                balance = response.getBalance();
                txtBalance.setText(String.valueOf(balance));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("BALANCE ERROR", "handleFault: " + fault.getMessage());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getCurrencies() {
        httpReq = buildRequestForCurrencies();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                HttpURLConnection urlConnection = null;
                StringBuilder result = new StringBuilder();
                try {
                    Log.e("URL", "doInBackground: " + httpReq); // DELETE
                    URL url = new URL(httpReq);
                    urlConnection = (HttpURLConnection)url.openConnection();
                    int statusCode = urlConnection.getResponseCode();
                    Log.e("STATUS CODE", "doInBackground: " + statusCode); // DELETE
                    if(statusCode == 200) {
                        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            result.append(line);
                        }
                        inputStream.close();
                        return result.toString();
                    } else
                        Log.e("RESPONSE ERROR", "doInBackground: Response came with status code - " + statusCode);
                } catch (MalformedURLException e) {
                    Log.e("URL ERROR", "doInBackground: Failed to create url");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("URL CONNECTION ERROR", "doInBackground: Failed to open url connection");
                    e.printStackTrace();
                } finally {
                    assert urlConnection != null;
                    urlConnection.disconnect();
                }
                return result.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                Log.i("HTTPREQUEST RESPONSE", "onPostExecute: HttpRequest response came out: " + s);
                try {
                    final JSONObject obj = new JSONObject(s);
                    final String time = obj.getString("timestamp");
                    final String source = obj.getString("source");
                    final JSONObject currObj = obj.getJSONObject("quotes");
                    Iterator<String> keys = currObj.keys();
                    int id = 1;
                    ((ImageView)findViewById(R.id.baseCountryFlag)).setImageDrawable(getDrawable(R.drawable.usa));
                    ((TextView)findViewById(R.id.baseCoinAcronym)).setText(source);
                    ((TextView)findViewById(R.id.baseCoinFullName)).setText(getCoinFullName("USD"));
                    ((TextView)findViewById(R.id.baseCoinValToBase)).setText(R.string.sourceVal);
                    ((ImageView)findViewById(R.id.baseCoinFormat)).setImageDrawable(getDrawable(R.drawable.usd));
                    while(keys.hasNext()) {
                        String nextKey = keys.next();
                        String coinInitials = trimSourceCountry(nextKey, source);
                        currenciesList.add(new Currency(id++, getImageID(coinInitials), getCoinFormat(coinInitials),
                                coinInitials, getCoinFullName(coinInitials), currObj.getString(nextKey)));
                    }
                    recyclerView = findViewById(R.id.currencies);
                    // setHasFixedSize is for improving the performance! can only be used if we know
                    // that changes in the content won't change the size of the RecyclerView
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new CurrenciesAdapter(context, currenciesList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void setButtonsListeners() {
        findViewById(R.id.btnDeposit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserActionsActivity("deposit");
            }
        });
        findViewById(R.id.btnWithdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserActionsActivity("withdraw");
            }
        });
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserActionsActivity("send");
            }
        });
        findViewById(R.id.btnClaim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserActionsActivity("claim");
            }
        });
    }

    private void goToUserActionsActivity(String action) {
        Intent intent = new Intent(context, UserActionsActivity.class);
        intent.putExtra("action", action);
        intent.putExtra("accountType", accountType);
        intent.putExtra("accountId", accountId);
        startActivity(intent);
    }


    @org.jetbrains.annotations.Contract(pure = true)
    private String buildRequestForCurrencies() {
        String reqStr = "http://apilayer.net/api/live?access_key=";
        reqStr += Defaults.CURRENCYLAYER_API_KEY;
        reqStr += "&currencies=";
        String[] requiredCurrencies = {"EUR", "GBP", "CAD", "ILS"};// Take it from somewhere else
        for (String currency : requiredCurrencies) {
            reqStr += currency == requiredCurrencies[requiredCurrencies.length-1] ? currency : currency + ",";
        }
        reqStr += "&source=USD"; // Change the source dynamically
        reqStr += "&format=1";
        return reqStr;
    }

    private int getImageID(String s) {
        int imgID;
        switch (s) {
            case "USD":
                imgID = R.drawable.usa;
                break;
            case "GBP":
                imgID = R.drawable.gb;
                break;
            case "EUR":
                imgID = R.drawable.europeanunion;
                break;
            case "CAD":
                imgID = R.drawable.canada;
                break;
            default:
                imgID = R.drawable.israel;
                break;
        }
        return imgID;
    }

    private int getCoinFormat(String s) {
        int imgID;
        switch (s) {
            case "USD":
                imgID = R.drawable.usd;
                break;
            case "GBP":
                imgID = R.drawable.pound;
                break;
            case "EUR":
                imgID = R.drawable.euro;
                break;
            case "CAD":
                imgID = R.drawable.cad;
                break;
            default:
                imgID = R.drawable.shekel;
                break;
        }
        return imgID;
    }

    private String getCoinFullName(String coin) {
        String fName;
        switch (coin) {
            case "USD":
                fName = "United States Dollar";
                break;
            case "GBP":
                fName = "British Pound";
                break;
            case "EUR":
                fName = "European Euro";
                break;
            case "CAD":
                fName = "Canadian Dollar";
                break;
            default:
                fName = "Israeli Shekel";
                break;
        }
        return fName;
    }

    private String trimSourceCountry(String quote, String source) {
        return quote.replace(source, "");
    }
}
