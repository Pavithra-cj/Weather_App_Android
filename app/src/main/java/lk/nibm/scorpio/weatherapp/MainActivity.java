package lk.nibm.scorpio.weatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import lk.nibm.scorpio.weatherapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    EditText etCity, etCountry;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "bb9a7e721f24b3de439f438c37e6aa00";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        etCity = binding.etCity;
        etCountry = binding.etCountry;
    }

    private void showToast(String message)
    {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void imageView(String weather)
    {
        if (weather.equals("Rain"))
        {
            binding.image.setImageResource(R.drawable.rain);
        } else if (weather.equals("Clouds")) {
            binding.image.setImageResource(R.drawable.cloudy);
        } else if (weather.equals("Cloudy Sunny")) {
            binding.image.setImageResource(R.drawable.cloudy_sunny);
        } else if (weather.equals("Rainy")) {
            binding.image.setImageResource(R.drawable.rainy);
        } else if (weather.equals("Snow")) {
            binding.image.setImageResource(R.drawable.snowy);
        } else if (weather.equals("Thunderstorm")) {
            binding.image.setImageResource(R.drawable.storm);
        } else if (weather.equals("Clear")) {
            binding.image.setImageResource(R.drawable.sunny);
        } else if (weather.equals("Windy")) {
            binding.image.setImageResource(R.drawable.windy);
        }
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if(city.equals("")){
            showToast("City cannot be empty!");
        }else{
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid + "&units=metric";
            }else{
                tempUrl = url + "?q=" + city + "&appid=" + appid + "&units=metric";
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String weather = jsonObjectWeather.getString("main");
                        imageView(weather);
                        binding.infoMain.setText(weather);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        binding.degreeMain.setText(String.valueOf(temp) + "°C");
                        double feelsLike = jsonObjectMain.getDouble("feels_like");
                        binding.seaGroundLevel.setText(String.valueOf(feelsLike) + "°C");
                        float pressure = jsonObjectMain.getInt("pressure");
                        binding.barometerMain.setText(String.valueOf(pressure) + "%");
                        int humidity = jsonObjectMain.getInt("humidity");
                        binding.humidityMain.setText(String.valueOf(humidity) + "%");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        binding.windMain.setText(wind + "m/s");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        binding.city.setText(cityName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}