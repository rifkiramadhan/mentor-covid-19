package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodayDeaths, tvAffectedCountries;
    SimpleArcLoader simpleArcLoader;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);

        simpleArcLoader = findViewById(R.id.loader);
        scrollView = findViewById(R.id.scrollStats);
        pieChart = findViewById(R.id.piechart);

        fetchData();
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all/";
        simpleArcLoader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            tvCases.setText(jsonObject.getString("cases"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvActive.setText(jsonObject.getString("active"));
                            tvTodayCases.setText(jsonObject.getString("todayCases"));
                            tvTotalDeaths.setText(jsonObject.getString("deaths"));
                            tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                            tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));

                            pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#ffa726")));
                            pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66bb6a")));
                            pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#ef5350")));
                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29b6f6")));
                            pieChart.startAnimation();

                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void onTrackCountries(View view) {
        startActivity(new Intent(getApplicationContext(), AffectedCountries.class));

    }
}