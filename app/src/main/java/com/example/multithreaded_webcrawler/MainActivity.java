package com.example.multithreaded_webcrawler;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MainActivity extends AppCompatActivity {

    String retweburl;
    String retwebrslt;
    TextView tvshowcode,txtcounter;
    Button codebtn,srchbtn;
    TaskSrcCode objsrc;
    EditText etfind;

    public class TaskSrcCode extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection huc;
            URL url;
            InputStream is;
            InputStreamReader isr;
            int ed;

            try {
                url = new URL(params[0]);
                huc = (HttpURLConnection)url.openConnection();
                is = huc.getInputStream();
                isr = new InputStreamReader(is);
                ed = isr.read();
                while(ed != -1) {
                    char edata = (char)ed;
                    retwebrslt += edata;
                    ed = isr.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return retwebrslt;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etfind = (EditText) findViewById(R.id.etsearch);
        tvshowcode = (TextView) findViewById(R.id.showhtmltxt);
        txtcounter = (TextView) findViewById(R.id.txtcount);
        codebtn = (Button) findViewById(R.id.btnhtml);
        srchbtn = (Button) findViewById(R.id.btnsearch);

        objsrc = new TaskSrcCode();

        codebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    retweburl = objsrc.execute("http://www.waheediqbal.info/courses/OS-2013/os_projects").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "HTML Page get successful", Toast.LENGTH_SHORT).show();

                tvshowcode.setText(retweburl);

            }
        });
        //ak.execute("wah g wah");

    }


    public void checkwords(View view){

        //int c = yewali.split("How to").length - 1;
        //Toast.makeText(getApplicationContext(), "some"+c, Toast.LENGTH_SHORT).show();

        int i = 0;
        //SpannableStringBuilder sb = new SpannableStringBuilder(yewali);
        Spannable spn = new SpannableString(tvshowcode.getText());
        Pattern p = Pattern.compile(etfind.getText().toString(), Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(retweburl);
        while (m.find()) {
            i++;
            spn.setSpan(new ForegroundColorSpan(Color.RED),m.start(),m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //sb.setSpan(new ForegroundColorSpan(Color.YELLOW),m.start(),m.end(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        //Toast.makeText(getApplicationContext(), ""+i, Toast.LENGTH_SHORT).show();
        tvshowcode.setText(spn);
        if(!etfind.equals("")) {
            txtcounter.setText(Html.fromHtml("&ldquo;" + etfind.getText().toString() + "&ldquo;") + " Word is used " + "\"" + i + "\"" + " times.");

        }
    }

}
