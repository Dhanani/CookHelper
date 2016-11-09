package com.uottawa.keenan.cookhelper;

import android.content.Context;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by karim on 11/7/2016.
 */

public class CreateDB {

    private File myDataBase;
    //private String path;
    OutputStreamWriter out;
    private String dbName;
    Context context;

    public CreateDB(Context context, String nameOfDB) throws IOException {


        myDataBase = new File(context.getFilesDir(), nameOfDB);
        this.context = context;
        //this.path = path;
        dbName = nameOfDB;
        //System.out.println("AAAA" +  context.getExternalFilesDir(null));
        String separator = System.getProperty("line.separator");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(dbName, Context.MODE_APPEND));
        outputStreamWriter.write("");
        outputStreamWriter.append(separator);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }




    public void addToDB(String stringToAdd) throws IOException {
        try {
            String separator = System.getProperty("line.separator");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(dbName, Context.MODE_APPEND));
            outputStreamWriter.write(stringToAdd);
            outputStreamWriter.append(separator);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            //myDataBase.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public File getMyDataBase(){
        return myDataBase;
    }

    public String toString(){
        return dbName;
    }

    public void readContents() throws IOException {
        FileInputStream is;
        BufferedReader reader;
        if (myDataBase.exists()) {
            System.out.println("Exists");
            is = new FileInputStream(myDataBase);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null){
                //Log.d("StackOverflow", line);
                line = reader.readLine();
                //System.out.println(line);
            }
        }
    }

    public boolean alreadyExsistsInDB(String stringToAdd) throws IOException {

        boolean existsInDB = false;

        FileInputStream is;
        BufferedReader reader;
        if (myDataBase.exists()) {
            //System.out.println("Exists");
            is = new FileInputStream(myDataBase);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null){
                line = reader.readLine();
                if(line!=null && line.equals(stringToAdd)){
                    System.out.println(stringToAdd + " already exists in db");
                    existsInDB = true;
                    break;
                }
            }
        }

        return existsInDB;
    }

}
