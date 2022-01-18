package com.utkarsh.cryptoeye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText searchEdt;
    private RecyclerView currenciesRView;
    private ProgressBar loadingPb;

    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private CurrencyRvAdapter currencyRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEdt=findViewById(R.id.idEditSearch);
        currenciesRView=findViewById(R.id.idRvCurrencies);
        loadingPb=findViewById(R.id.idPbLoading);
        

        currencyRVModalArrayList = new ArrayList<>();
        currencyRvAdapter=new CurrencyRvAdapter(currencyRVModalArrayList,this);
        currenciesRView.setLayoutManager(new LinearLayoutManager(this));
        currenciesRView.setAdapter(currencyRvAdapter);



        getCurrencyData();

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                        filterCurrencies(s.toString());
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_AboutUs){
            Intent i = new Intent(MainActivity.this,About_us.class);
            startActivity(i);
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterCurrencies(String currency){
        ArrayList<CurrencyRVModal> filteredList = new ArrayList<>();
        for (CurrencyRVModal item : currencyRVModalArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No currency found for searched query", Toast.LENGTH_SHORT).show();
        }else {
            currencyRvAdapter.filterList(filteredList);
        }
    }

    private void getCurrencyData(){
        loadingPb.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPb.setVisibility(View.GONE);
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i=0;i<dataArray.length();i++){
                                JSONObject dataObj = dataArray.getJSONObject(i);
                                String name = dataObj.getString("name");
                                String symbol = dataObj.getString("symbol");
                                JSONObject quote = dataObj.getJSONObject("quote");
                                JSONObject USD=quote.getJSONObject("USD");
                                double price = USD.getDouble("price");
                                currencyRVModalArrayList.add(new CurrencyRVModal(name,symbol,price));
                            }
                            currencyRvAdapter.notifyDataSetChanged();
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Failed to get JSON data", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingPb.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Fail to get the data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","c26d43fa-4d03-48e-26a84e066cee");
                return headers;
            }
        };
                requestQueue.add(jsonObjectRequest);


    }
}
