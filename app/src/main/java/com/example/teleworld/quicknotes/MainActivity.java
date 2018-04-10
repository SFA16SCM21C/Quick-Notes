package com.example.teleworld.quicknotes;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import static com.example.teleworld.quicknotes.R.drawable.notepad;

public class MainActivity extends AppCompatActivity {

    //TAG declaration for logging instances
    private static final String TAG = "Main_Activity";

    //variable declarations on widgets to be used
    TextView noteDate;
    EditText noteData;

    //java fil declaration
    QuickNotes quickNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting variables and fetching values from widget
        quickNotes = new QuickNotes();
        noteDate = (TextView)findViewById(R.id.dateText);
        noteData = (EditText)findViewById(R.id.dataText);
        noteData.setMovementMethod(new ScrollingMovementMethod());

        JSONObject data_obj = new JSONObject();

        //setting the date in the widget in the specified format
        quickNotes.setDate("Last Updated : " + getDate());
    }

    //to fetch the date in the desired format
    private String getDate()
    {
        //String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        //return date;

        //to stamp date into the textView
        long date = System.currentTimeMillis();

        //creating the date format to be displayed in the notepad
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE MMM d, h:mm a");
        String dateString = sdf.format(date);
        return dateString;
    }

    //enabling edit text with button click
    public void EditClick(View view){

        //enabling the data to be edited
        noteData.setEnabled(true);
        Toast.makeText(this,getString(R.string.editnote),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //logging the resume state to check
        Log.e(TAG, "In Resume state");

        //calling load function to load the values whenever the application resumes
        loadData();
    }

    //for loading the file
    public void loadData(){

        //logging the loading activity
        Log.e(TAG, "Load File : Loading JSON File");
        try{
            InputStream is = getApplicationContext().openFileInput((getString(R.string.filename)));
            JsonReader jr = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
            jr.beginObject();

            while(jr.hasNext()){
             String name = jr.nextName();
                Log.e(TAG, "LoadFile : " + name);
                if(name.equals("Date")){
                    quickNotes.setDate(jr.nextString());
                    noteDate.setText("Last Update: " + quickNotes.getDate());
                    Log.e(TAG, "Date Fetched : " + quickNotes);
                }
                else if(name.equals("Data")){
                    quickNotes.setData(jr.nextString());
                    noteData.setText(quickNotes.getData());
                    Log.e(TAG, "Data Fetched : " + quickNotes);
                }
                else{
                    jr.skipValue();
                }
            }
            jr.endObject();
        }
        catch (FileNotFoundException e){
            Toast.makeText(this, getString(R.string.nofile), Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e(TAG, "In Pause state");
        quickNotes.setDate(noteDate.getText().toString());
        quickNotes.setData(noteData.getText().toString());

        saveData();
    }

    //for saving the data
    public void saveData() {
        try{
            FileOutputStream f = getApplicationContext().openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE);
            JsonWriter jw = new JsonWriter(new OutputStreamWriter(f, getString(R.string.encoding)));
            jw.beginObject();
            jw.name("Date").value(getDate());
            jw.name("Data").value(quickNotes.getData());
            jw.endObject();
            jw.close();

            //to check if json is created or not
            StringWriter sw = new StringWriter();
            jw = new JsonWriter(sw);
            jw.beginObject();
            jw.name("Date").value(quickNotes.getDate());
            jw.name("Data").value(quickNotes.getData());
            jw.endObject();
            jw.close();

            //logging the json file in console
            Log.d(TAG, "Saved Quick Notes : JSON:\n" + sw.toString());

            Toast.makeText(this,getString(R.string.savedata),Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.getStackTrace();
        }
    }
}
