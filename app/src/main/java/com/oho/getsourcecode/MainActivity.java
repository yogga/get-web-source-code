package com.oho.getsourcecode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    TextView myText;
    EditText editText;
    Spinner spin;
    ArrayAdapter<CharSequence> list_spin;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myText = (TextView) findViewById(R.id.txt_id);
        editText = (EditText) findViewById(R.id.edit_id);
        spin = (Spinner) findViewById(R.id.spin_id);

        list_spin = ArrayAdapter.createFromResource(this,R.array.list_spin,android.R.layout
                .simple_spinner_item);
        list_spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(list_spin);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
            }
        });

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void getSource(View view) {
        String getSpin, getUrl, link;
        getSpin = spin.getSelectedItem().toString(); //Mengambil spin untuk menjadi string
        getUrl = editText.getText().toString(); //Mengambil edittext untuk menjadi string

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


        if(!getUrl.isEmpty()){ //Kondisi EditText tidak kosong
            if(getUrl.contains(".") && !(getUrl.contains(" "))){ //cek input url mengandung . dan tidak mengandung spasi
                if(checkConnection()){ //cek koneksi internet
                    myText.setText("Loading....");
                    myText.setTextSize(15);

                    link = getSpin + getUrl;

                    Bundle bundle = new Bundle();
                    bundle.putString("link_pros", link);
                    getSupportLoaderManager().restartLoader(0, bundle, this);
                }
                else{
                    Toast.makeText(this,"Check Your Internet Connection and Try Again",Toast.LENGTH_SHORT).show();
                    myText.setText("Check Your Internet Connection and Try Again");
                    myText.setTextSize(25);
                }
            }
            else{
                Toast.makeText(this,"Invalid Input URL",Toast.LENGTH_SHORT).show();
                myText.setText("Invalid Input URL");
                myText.setTextSize(25);
            }
        }
        else{
            Toast.makeText(this,"Please Input URL",Toast.LENGTH_SHORT).show();
            myText.setText("URL is empty, please input URL");
            myText.setTextSize(25);
        }
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ConnectInternet(this, args.getString("link_pros"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        myText.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan Sekali Lagi", Toast.LENGTH_SHORT).show();
    }
}
