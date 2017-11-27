// return Environment.getExternalStorageDirectory() + "/Testahel/" + path;

/**
 * 1-onPreExecute which be executed before the execution of the internet access occurs and in this project it's for
 * showing the process dialog for the process.
 * 2-doInBackground which create the connection between android and web script to get the response from server and make
 * this process executing in the background to not to freeze the device
 * it's for reading the response and control all values in it.
 * 3-openHttpConnect to open a connection with http server.
 * 4-downloadFile for opening stream connection and downloading file to SD card
 * 5-createMyFile this for create the file that is downloaded before
 * 6-onPostExecute to make an action which means the process finished and check the values and dismiss the process diaLog.
 * 7-isNetworkAvailable which used for check the internet status.
 */

package com.amr.sinnerschraderparsingtask.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.amr.sinnerschraderparsingtask.data.models.LinkModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amr El-Madah on 11/27/2017.
 */

public class PageTitle extends AsyncTask<String, String, String> {
    private static final Pattern TITLE_TAG =
            Pattern.compile("\\<title>(.*)\\</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);


    private HttpURLConnection httpConn;
    private Context context;
    private OnAllTitlesExtracted onAllTitlesExtracted;
    private List<LinkModel> links;
    int index;


    public PageTitle(Context context, List<LinkModel> links, OnAllTitlesExtracted onAllTitlesExtracted, int index) {
        this.context = context;
        this.links = links;
        this.onAllTitlesExtracted = onAllTitlesExtracted;
        this.index = index;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        return getPageTitle(links.get(index).getUrl());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getPageTitle(String urlStr) {
        if (isNetworkAvailable()) {
            try {
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10000);
                // ContentType is an inner class defined below
                ContentType contentType = getContentTypeHeader(conn);
                if (contentType == null || !contentType.contentType.equals("text/html"))
                    return "Can not extract title"; // don't continue if not HTML
                else {
                    // determine the charset, or use the default
                    Charset charset = getCharset(contentType);
                    if (charset == null)
                        charset = Charset.defaultCharset();

                    // read the response body, using BufferedReader for performance
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
                    int n = 0, totalRead = 0;
                    char[] buf = new char[1024];
                    StringBuilder content = new StringBuilder();

                    // read until EOF or first 8192 characters
                    while (totalRead < 8192 && (n = reader.read(buf, 0, buf.length)) != -1) {
                        content.append(buf, 0, n);
                        totalRead += n;
                    }
                    reader.close();

                    // extract the title
                    Matcher matcher = TITLE_TAG.matcher(content);
                    if (matcher.find()) {
                /* replace any occurrences of whitespace (which may
                 * include line feeds and other uglies) as well
                 * as HTML brackets with a space */
                        return matcher.group(1).replaceAll("[\\s\\<>]+", " ").trim();
                    } else
                        return null;
                }
            } catch (MalformedURLException e) {
                return "Can't extract title";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "Internet connection error";
        }
        return "Can't extract title";
    }

    @Override
    protected void onPostExecute(String result) {
        links.get(index).setTitle(result);
        if (index == links.size() - 1) {
            onAllTitlesExtracted.setAllTitlesExtracted(links);
        } else if (index < links.size()) {
            index++;
            new PageTitle(context, links, onAllTitlesExtracted, index).execute();
        }
    }

    private static ContentType getContentTypeHeader(URLConnection conn) {
        int i = 0;
        boolean moreHeaders = true;
        do {
            String headerName = conn.getHeaderFieldKey(i);
            String headerValue = conn.getHeaderField(i);
            if (headerName != null && headerName.equals("Content-Type"))
                return new ContentType(headerValue);

            i++;
            moreHeaders = headerName != null || headerValue != null;
        }
        while (moreHeaders);

        return null;
    }

    private static Charset getCharset(ContentType contentType) {
        if (contentType != null && contentType.charsetName != null && Charset.isSupported(contentType.charsetName))
            return Charset.forName(contentType.charsetName);
        else
            return null;
    }

    /**
     * Class holds the content type and charset (if present)
     */
    private static final class ContentType {
        private static final Pattern CHARSET_HEADER = Pattern.compile("charset=([-_a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        private String contentType;
        private String charsetName;

        private ContentType(String headerValue) {
            if (headerValue == null)
                throw new IllegalArgumentException("ContentType must be constructed with a not-null headerValue");
            int n = headerValue.indexOf(";");
            if (n != -1) {
                contentType = headerValue.substring(0, n);
                Matcher matcher = CHARSET_HEADER.matcher(headerValue);
                if (matcher.find())
                    charsetName = matcher.group(1);
            } else
                contentType = headerValue;
        }
    }

    public interface OnAllTitlesExtracted {
        public void setAllTitlesExtracted(List<LinkModel> links);
    }
}
