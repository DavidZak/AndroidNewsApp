package com.example.mradmin.androidnewsapp.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mradmin.androidnewsapp.R;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by yksoft on 18.07.18.
 */

public class Util {

    public static void setViewVisibility(View view, int visibility){
        if (view != null)
            view.setVisibility(visibility);
    }

    public static void setSwipeRefreshRefreshing(SwipeRefreshLayout swipeRefresh, boolean refreshing){
        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(refreshing);
    }

    public static void showAnchoredPopUpView(Context context, View anchorView, int layoutRes, String text) {
        PopupWindow popup = new PopupWindow(context);
        View layout = LayoutInflater.from(context).inflate(layoutRes, null);

        TextView textView = layout.findViewById(R.id.tvCaption);
        if (text == null)
            textView.setText("");
        else
            textView.setText(text);

        //View layout = getLayoutInflater().inflate(R.layout.refresh_info_popup, null);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
    }

    public static void showAnchoredPopUpView(Context context, View anchorView, int layoutRes, int text) {
        PopupWindow popup = new PopupWindow(context);
        View layout = LayoutInflater.from(context).inflate(layoutRes, null);

        TextView textView = layout.findViewById(R.id.tvCaption);
        textView.setText(text);

        //View layout = getLayoutInflater().inflate(R.layout.refresh_info_popup, null);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
    }

    public static InputStream convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            return is;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
