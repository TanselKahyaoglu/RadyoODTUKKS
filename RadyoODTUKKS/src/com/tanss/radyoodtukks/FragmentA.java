package com.tanss.radyoodtukks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FragmentA extends Fragment {
    String str;
    String textSource="http://radyoodtukibris.net/rds.txt";
    String playToast="Yayýn yükleniyor lütfen bekleyiniz.";
	TextView textMsg;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_a, container, false);    
        textMsg = new TextView(container.getContext());
        textMsg.setText("Checkiing...");
        ReadExtPortfolio rext = new ReadExtPortfolio();
        rext.execute();
	    textMsg = (TextView)view.findViewById(R.id.textMsg);
        final ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggleButton1);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Intent i = new Intent(getActivity(), RadioService.class);
            	if (isChecked) {
            		boolean getServiceState = isMyServiceRunning();
            		if (getServiceState) {
            		} else {
                    	getActivity().startService(new Intent(getActivity(),RadioService.class));
          			     Toast.makeText(getActivity().getBaseContext(),playToast, Toast.LENGTH_LONG).show();
                     	System.out.println("Yayýn Baþladý...");
            		};
            		// The toggle is enabled
                } else {
                    // The toggle is disabled
                	getActivity().stopService(new Intent(getActivity(),RadioService.class));
                	getActivity().stopService(i);
                	}
            }
        });
        return view;
    }
   private boolean isMyServiceRunning() {
		Log.i("Activity Manager :", "Checking RadioService");
       ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
       for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
           if (RadioService.class.getName().equals(service.service.getClassName())) {
			return true;
		    }
		}
		return false;
	    }
   
   class ReadExtPortfolio extends AsyncTask<Void, Void, Void>
   {

       @Override
       protected void onPreExecute() {
    	   textMsg.setText("loading...");
       }
       @Override
       protected Void doInBackground(Void... arg0) {
           try {
        	   Log.i("Reading Text", "Reading Text");
               URL url = new URL(textSource);
               BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
               str = in.readLine();
               in.close();
             } catch (IOException e) {
                str = "Baðlantý Hatasý";
              }           
           return null;
       }
       // Executed on the UI thread after the
       // time taking process is completed
       @Override
       protected void onPostExecute(Void result) {
           super.onPostExecute(result);
           textMsg.setText(str);           
       }
   }   
}