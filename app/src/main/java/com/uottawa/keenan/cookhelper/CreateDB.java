package com.uottawa.keenan.cookhelper;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by karim on 11/7/2016.
 */

public class CreateDB {

    private Context context;
    private File myDataBase; //this is the file that contains data
    private String dbName; //the name of the database as a string
    private int size = 0; //the size corresponds to the number of lines in the database

    /*
        The constructor creates a new file with the given name
        or references an already existing file with that name
     */
    public CreateDB(Context context, String nameOfDB) throws IOException {
        try {
            if (myDataBase.exists()) {
                myDataBase = new File(context.getFilesDir(), nameOfDB);
                this.context = context;
                dbName = nameOfDB;
            }
        }catch(NullPointerException ex) {
            myDataBase = new File(context.getFilesDir(), nameOfDB);
            this.context = context;
            dbName = nameOfDB;
        }
    }


    /*
        Takes a string and checks if it already exists in the database before adding it
     */
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
    }


    /*
        Used for adding recipes to the recipe database
     */
    public boolean addToRecipeDB(String stringToAdd) throws IOException {

        if(dbName!="RecipeDB.txt")return false;

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
    }


    /*
        Deletes a line from the database based on the given string
     */
    public boolean removeFromDB(String stringToRemove) throws IOException{

        boolean successful = false;

        //if we're dealing RecipeDB then we must take into account
        //that each line contains delimiters and that the first value is the recipe name
        if(dbName.equals("RecipeDB.txt") ){

            File tempFile = new File(context.getFilesDir(), "myTempFile.txt");

            BufferedReader reader = new BufferedReader(new FileReader(myDataBase));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            boolean first = false;
            String line;
            String[] recipeName;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                recipeName = trimmedLine.split("\\|")[0].split("`");
                if (recipeName[0].equals(stringToRemove)) continue;
                first = true;
                writer.write(line + System.getProperty("line.separator"));
            }

            writer.close();
            reader.close();
            size--;
            successful = tempFile.renameTo(myDataBase);
            myDataBase = new File(context.getFilesDir(), dbName);
            tempFile.delete();

        }
        //if not RecipeDB.txt then compare the string to the line and delete away!
        else{
            File tempFile = new File(context.getFilesDir(), "myTempFile.txt");


            BufferedReader reader = new BufferedReader(new FileReader(myDataBase));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            boolean first = false;
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.equals(stringToRemove)) continue;
                first = true;
                writer.write(line + System.getProperty("line.separator"));
            }

            writer.close();
            reader.close();
            size--;
            successful = tempFile.renameTo(myDataBase);
            myDataBase = new File(context.getFilesDir(), dbName);
            tempFile.delete();
        }
        return successful;
    }


    /*
        Returns an ArrayList of strings that correspond to categories that are used by
        active existing recipes. It is used to check if a category is allowed to be removed
        from the UI or not.
     */
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


    /*
        Returns an ArrayList of strings that correspond to types that are used by
        active existing recipes. It is used to check if a type is allowed to be removed
        from the UI or not.
     */
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


    /*
        Returns an ArrayList of strings that correspond to ingredients that are used by
        active existing recipes. It is used to check if an ingredient is allowed to be removed
        from the UI or not.
     */
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


    /*
        Used to check whether a string with a given value already exists in the database
        or not before adding it.
     */
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


    /*
        Used to check whether a recipe with a given name already exists in the database
        or not before adding it.
     */
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
        Returns Recipe line of database (i.e. recipe posting) by recipe name.
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


    /*
        Returns all lines in the database as an ArrayList of strings.
     */
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


    /*
        Getter method for the File that is the database
     */
    public File getMyDataBase(){
        return myDataBase;
    }


    /*
        Delete the File that is the database from the local directory
     */
    public void destroyDataBase(){
        myDataBase.delete();
        dbName = null;
        size = 0;
        context = null;
    }


    /*
        returns the name of the database as a string
     */
    public String toString(){ return dbName;}

}
