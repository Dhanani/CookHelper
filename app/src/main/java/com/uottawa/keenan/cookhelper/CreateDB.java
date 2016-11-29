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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by karim on 11/7/2016.
 */

public class CreateDB {

    private File myDataBase;
    private String dbName;
    private int size = 0;
    Context context;

    public CreateDB(Context context, String nameOfDB) throws IOException {

        try {
            if (myDataBase.exists()) {
                myDataBase = new File(context.getFilesDir(), nameOfDB);
                this.context = context;
                dbName = nameOfDB;
            }
        }catch(NullPointerException ex){
            myDataBase = new File(context.getFilesDir(), nameOfDB);
            this.context = context;
            dbName = nameOfDB;
        }
    }


    public void addToDB(String stringToAdd) throws IOException {

        try {
            if(alreadyExsistsInDB(stringToAdd)) {
                System.out.println(stringToAdd + "already exists in the database");
            }else{
                String separator = System.getProperty("line.separator");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(dbName, Context.MODE_APPEND));
                outputStreamWriter.write(stringToAdd);
                outputStreamWriter.append(separator);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                size++;
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        //readContents();
    }

    public boolean addToRecipeDB(String stringToAdd) throws IOException {
        String recipe_name = stringToAdd.split("\\|")[0];
        System.out.println(recipe_name);
        try {
            if(alreadyExsistsInRecipeDB(recipe_name)) {
                System.out.println(stringToAdd + "already exists in the database");
                return false;
            }else{
                String separator = System.getProperty("line.separator");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(dbName, Context.MODE_APPEND));
                outputStreamWriter.write(stringToAdd);
                outputStreamWriter.append(separator);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                size++;
                return true;
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
        //readContents();
    }

    public boolean removeFromDB(String stringToRemove) throws IOException{

        File tempFile = new File(context.getFilesDir(), "myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(myDataBase));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        boolean first = false;
        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(stringToRemove)) continue;
            first = true;
            writer.write(currentLine + System.getProperty("line.separator"));
        }

        writer.close();
        reader.close();
        size --;
        boolean successful = tempFile.renameTo(myDataBase);
        myDataBase = new File(context.getFilesDir(), dbName);
        tempFile.delete();
        return successful;
    }

    public ArrayList<String> getCategoriesUsedInRecipes(){

        if(dbName!="RecipeDB.txt"){
            return null;
        }

        ArrayList<String> allCategories = new ArrayList<>(size);

        try  {
            InputStream input = new FileInputStream(myDataBase);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {

                String[] recipeCategory;
                recipeCategory = line.split("\\|")[1].split("`");

                for(int i=0; i<recipeCategory.length; i++) {
                    if(!allCategories.contains(recipeCategory[i])){
                        allCategories.add(recipeCategory[i]);
                        System.out.println(recipeCategory[i]);
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        return allCategories;
    }


    public ArrayList<String> getTypesUsedInRecipes(){

        if(dbName!="RecipeDB.txt"){
            return null;
        }

        ArrayList<String> allTypes = new ArrayList<>(size);

        try  {
            InputStream input = new FileInputStream(myDataBase);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {

                String[] recipeType;
                recipeType = line.split("\\|")[2].split("`");

                for(int i=0; i<recipeType.length; i++) {
                    if(!allTypes.contains(recipeType[i])){
                        allTypes.add(recipeType[i]);
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        return allTypes;
    }


    public ArrayList<String> getIngredientsUsedInRecipes(){

        if(dbName!="RecipeDB.txt"){
            return null;
        }

        ArrayList<String> allIngredients = new ArrayList<>(size);

        try  {
            InputStream input = new FileInputStream(myDataBase);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {

                String[] recipeIngredients;
                recipeIngredients = line.split("\\|")[4].split("`");

                for(int i=0; i<recipeIngredients.length; i++) {
                    if(!allIngredients.contains(recipeIngredients[i])){
                        allIngredients.add(recipeIngredients[i]);
                        //System.out.println(recipeIngredients[i]);
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        return allIngredients;

    }

    public boolean alreadyExsistsInRecipeDB(String stringToCheck) throws IOException {

        boolean existsInDB = false;
        try  {
            InputStream input = new FileInputStream(this.myDataBase);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String recipe_name = line.split("\\|")[0];
                if(recipe_name.equals(stringToCheck)){
                    existsInDB = true;
                    break;
                }

            }
        }catch(Exception e){
            System.out.println(e);
        }
        return existsInDB;
    }

    /*
        Added by Keenan.
        Get Recipe line of database (i.e. recipe posting) by recipe name.
     */
    public String getRecipeListingFromName(String recipeName) throws IOException {
        try  {
            InputStream input = new FileInputStream(this.myDataBase);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String recipe_name = line.split("\\|")[0];
                if(recipe_name.equals(recipeName)){
                    return line;
                }

            }
        }catch(Exception e){
            System.out.println(e);
        }

        return null;
    }

    public boolean alreadyExsistsInDB(String stringToCheck) throws IOException {

        boolean existsInDB = false;
        try  {
            InputStream input = new FileInputStream(this.myDataBase);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.equals(stringToCheck)){
                    existsInDB = true;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return existsInDB;
    }


    public String readContents() throws IOException {

        StringBuilder builder = new StringBuilder();

        try  {
            InputStream input = new FileInputStream(myDataBase);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println(builder.toString());
        return builder.toString();
    }


    public ArrayList<String> getAsArrayList(){

        ArrayList<String> dbAsAL = new ArrayList<>(size);

        //StringBuilder builder = new StringBuilder();

        try  {
            InputStream input = new FileInputStream(myDataBase);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, "UTF-8"));
            String line;
            int i=0;
            while ((line = reader.readLine()) != null) {
                dbAsAL.add(i, line);
                i++;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return dbAsAL;
    }




    public File getMyDataBase(){
        return myDataBase;
    }

    public void destroyDataBase(){
        myDataBase.delete();
        dbName = null;
        size = 0;
        context = null;
    }

    public String toString(){ return dbName;}


}
