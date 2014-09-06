package com.tanss.radyoodtukks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentC extends Fragment {
	WebView websayfasi;
	String twitter = "https://twitter.com/RadyoODTUKKS";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    	Log.i("Twitter loading", "Twitter loading");
        View view = inflater.inflate(R.layout.fragment_c, container, false);
        websayfasi = (WebView)view.findViewById(R.id.webview);       
        websayfasi.getSettings().setJavaScriptEnabled(true);     
       WebViewClient wvc = new WebViewClient();     
        websayfasi.setWebViewClient(wvc);
        websayfasi.loadUrl(twitter);   
        return view;
    }
 
}