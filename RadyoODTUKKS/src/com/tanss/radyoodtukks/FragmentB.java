package com.tanss.radyoodtukks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentB extends Fragment {
	WebView websayfasi;
	String facebook = "https://www.facebook.com/RadyoODTUKKS";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    	Log.i("Facebook loading", "Facebook loading");
        View view = inflater.inflate(R.layout.fragment_b, container, false);       
        websayfasi = (WebView)view.findViewById(R.id.webview);       
        websayfasi.getSettings().setJavaScriptEnabled(true);     
       WebViewClient wvc = new WebViewClient();     
        websayfasi.setWebViewClient(wvc);
        websayfasi.loadUrl(facebook);             
        return view;
    }
 
}