package com.tanss.radyoodtukks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

public class MainActivity extends FragmentActivity {
    ViewPager viewpager = null;
	PhoneStateListener phoneStateListener; 
    String str;
    String textSource="http://radyoodtukibris.net/rds.txt";
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fm = getSupportFragmentManager();
        viewpager.setAdapter(new MyAdapter(fm));
        //-------------------------------notification için gerekenler.
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, "Hayatýn Sesini Aç!", when);
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
        contentView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
        contentView.setTextViewText(R.id.title, "Radyo ODTÜ KKS");
        contentView.setTextViewText(R.id.text, "Çalýþýyor");
        notification.contentView = contentView;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;
        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        mNotificationManager.notify(1, notification);
        //------------------notification yaratýldý.
        ReadExtPortfolio rext = new ReadExtPortfolio();
        rext.execute();
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
        	@Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                	stopService(new Intent(MainActivity.this, RadioService.class));
                } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                	stopService(new Intent(MainActivity.this, RadioService.class));
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        CheckConnectivity check = new CheckConnectivity();
        Boolean conn = check.checkNow(this.getApplicationContext());
        if(conn == true){
             //run your normal code path here
        }
        else{        
             //Send a warning message to the user
     	   new AlertDialog.Builder(this)
           .setIcon(android.R.drawable.ic_dialog_alert)
           .setTitle("Baðlantý Hatasý")
           .setMessage("Lütfen internet baðlantýnýzý kontrol edin")
           .setPositiveButton("TAMAM", new DialogInterface.OnClickListener()
           
       {
           @Override
           public void onClick(DialogInterface dialog, int which) {  
               stopService(new Intent(MainActivity.this,RadioService.class));
   	        finish();
   			System.exit(0);
           }
       })
           .show();
    } 
        };  

	@Override
    public void onDestroy() {
        super.onDestroy();       
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    	stopService(new Intent(MainActivity.this, RadioService.class));
    	TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    	if(mgr != null) {
    	    mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    	}
    }
    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }
        @Override
        public Fragment getItem(int i) {
            // TODO Auto-generated method stub
            Fragment fragment = null;
            if (i == 0) {
                fragment = new FragmentA();
            }
            if (i == 1) {
                fragment = new FragmentB();
            }
            if (i == 2) {
                fragment = new FragmentC();
            }
            return fragment;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int i) {
            // TODO Auto-generated method stub
            String title = new String();
            if (i == 0) {
                return "Canlý Yayýn";
            }
            if (i == 1) {
                return "Facebook";
            }
            if (i == 2) {
                return "Twitter";
            }
            return null;
        }
    }
	   @Override
	   public void onBackPressed() {	
		new AlertDialog.Builder(this)
       .setIcon(android.R.drawable.ic_dialog_alert)
       .setTitle("Uygulama Kapatýlýyor")
       .setMessage("Uygulamadan çýkmak istediðinize emin misiniz?")
       .setPositiveButton("Evet", new DialogInterface.OnClickListener()
   {
       @Override
       public void onClick(DialogInterface dialog, int which) {
           stopService(new Intent(MainActivity.this,RadioService.class));
	        finish();
			System.exit(0);  
       }

   })
   .setNegativeButton("Hayýr", null)
   .show();
}
	   class ReadExtPortfolio extends AsyncTask<Void, Void, Void>
	   {
	       @Override
	       protected void onPreExecute() {
	       }
	       @Override
	       protected Void doInBackground(Void... arg0) {
	           try {
	        	   Log.i("Reading Text 2", "Reading Text 2");
	               URL url = new URL(textSource);
	               BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	               str = in.readLine();
	               in.close();
		           RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
		           contentView.setTextViewText(R.id.text, str);
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

	       }
	   }   
	}