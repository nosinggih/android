package com.example.absen_gdblank.ordersharpening;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://192.168.168.43/tests/apps.php";
    String hasilApi = "0";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        sederhana();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("no_order");
                        String email = c.getString("reff_number");
                        String address = c.getString("kode_barang");
                        String gender = c.getString("deskripsi_barang");


                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("no_order", name);
                        contact.put("reff_number", email);


                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

    }

    void sederhana(){
        class sederhanaBackground extends AsyncTask<String, Void, String>{
            @Override
            protected String doInBackground(String... strings) {
                hasilApi="0";
                try {
                    // membuat httpclient
                    HttpClient httpClient = new DefaultHttpClient();
                    // membuat metode get
                    HttpGet httpGet = new HttpGet(url);
                    // membuat wadah respon
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    // konversi respon menjadi input stream
                    HttpEntity httpEntity = httpResponse.getEntity();
                    // konversi dari input stream menjadi string
                    konversiString(httpEntity.getContent());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return hasilApi;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("HASIL API"," => "+s);

            }
        }
        sederhanaBackground g = new sederhanaBackground();
        g.execute();
    }

    public void konversiString(InputStream is){
        String line=null;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while((line=reader.readLine()) != null){
                sb.append(line+"\n");
            }
            is.close();
            hasilApi=sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            hasilApi="0";
        }
    }


}
