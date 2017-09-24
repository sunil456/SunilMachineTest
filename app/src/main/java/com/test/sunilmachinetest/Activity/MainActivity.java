package com.test.sunilmachinetest.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.test.sunilmachinetest.Model.CityModel;
import com.test.sunilmachinetest.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    AQuery aQuery;
    Context context;
    List<String> stateList=new ArrayList<>();
    List<CityModel> cityList=new ArrayList<>();
    Spinner spinnerState,spinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        aQuery = new AQuery(context);

        spinnerState=(Spinner)findViewById(R.id.spinnerState);
        spinnerCity=(Spinner)findViewById(R.id.spinnerCity);

        getStateAndCity();

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                setCitySpinner(stateList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    public void  getStateAndCity()
    {
        String url="http://watsmo.com/clients/techmate/getCity.php";
        aQuery.ajax(url.trim(), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);

                try {
                    // String jsonStr = object.toString();
                    Log.d("responseState",""+object);
                    Iterator currentKey = object.keys();
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    while (currentKey.hasNext()) {

                        String dateKey = (String) currentKey.next();
                        JSONObject jsonObject = object.getJSONObject(dateKey);
                        hashMap.put(dateKey, jsonObject);


                        for (Map.Entry entry : hashMap.entrySet()) {

                            if (!stateList.contains(entry.getKey().toString())) {
                                stateList.add((String) entry.getKey());
                            }

                            System.out.println(entry.getKey() + ", " + entry.getValue());

                            JSONObject cityValue= (JSONObject) entry.getValue();
                            Iterator currentKey1 = cityValue.keys();
                            HashMap<String, String> hashMap1 = new HashMap<String, String>();

                            while (currentKey1.hasNext()) {

                                String dateKey1 = (String) currentKey1.next();
                                Log.d("dateKey",dateKey1);
                                String datavalue1= (String) cityValue.get(dateKey1);
                                Log.d("datavalue1",datavalue1);


                                  /*  for (int i=0;i<cityList.size();i++) {
                                        Log.d("inFor",datavalue1);
                                        Log.d("inFor",""+cityList.get(i).getName().equals(datavalue1));

                                        if (!cityList.get(i).getName().equals(datavalue1)) {
                                            Log.d("inIf",datavalue1);

*/
                                CityModel cityModel = new CityModel();
                                cityModel.setId(dateKey1);
                                cityModel.setName(datavalue1);
                                cityModel.setState((String) entry.getKey());

                                cityList.add(cityModel);
                                //}
                                // }

                                //hashMap1.put(dateKey1, jsonObject1);
                            }
                        }
                    }

                    if (!stateList.isEmpty())
                        setStateSpinner();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public  void setStateSpinner()
    {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, stateList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerState.setAdapter(dataAdapter);
    }
    public  void setCitySpinner(String state)
    {
        List<String> tempCityList=new ArrayList<>();
        for (int i=0;i<cityList.size();i++)
        {
            if (cityList.get(i).getState().equals(state))
            {
                if (!tempCityList.contains(cityList.get(i).getName()))
                    tempCityList.add(cityList.get(i).getName());
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tempCityList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCity.setAdapter(dataAdapter);
    }

}
