package com.uottawa.keenan.cookhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Hasnain
 */

public class FindRecipe extends AppCompatActivity {

    public CreateDB recipeDB;
    public CreateDB categoryDB;
    public CreateDB typeDB;

    private ArrayList<String> category_entries = new ArrayList<>();
    private ArrayList<String> type_entries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_recipe);

        try {
            recipeDB = new CreateDB(getApplicationContext(), "RecipeDB.txt");
            recipeDB.readContents();

            categoryDB = new CreateDB(getApplicationContext(), "CategoriesDB.txt");
            typeDB = new CreateDB(getApplicationContext(), "TypesDB.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateCategoryEntries();
        updateCategorySpinner();
        updateTypeEntries();
        updateTypeSpinner();
    }

    private void add_boolean_operator(String operator){
        EditText ingredients_editText = (EditText) findViewById(R.id.ingredients_editText);
        String current_text = ingredients_editText.getText().toString().trim().toLowerCase();
        ingredients_editText.setText(current_text + " " + operator);
        ingredients_editText.setSelection(ingredients_editText.getText().length());
    }

    public void OnAnd(View view) {
        add_boolean_operator("and");
    }

    public void OnOr(View view) {
        add_boolean_operator("or");
    }

    public void OnNot(View view) {
        add_boolean_operator("not");
    }

    public void updateCategoryEntries(){
        category_entries.clear();
        category_entries.add("");
        ArrayList<String> dbSource = categoryDB.getAsArrayList();
        for(int i=0; i<dbSource.size(); i++){
            category_entries.add(i,dbSource.get(i));
        }
    }

    public void updateTypeEntries(){
        type_entries.clear();
        type_entries.add("");
        ArrayList<String> dbSource = typeDB.getAsArrayList();
        for(int i=0; i<dbSource.size(); i++){
            type_entries.add(i,dbSource.get(i));
        }
    }

    public void updateCategorySpinner() {
        if (category_entries.size()>0){
            Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);

            ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, category_entries);

            dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_spinner.setAdapter(dataAdapterCategory);

            int spinnerPosition = dataAdapterCategory.getPosition(category_entries.get(category_entries.size()-1));
            category_spinner.setSelection(spinnerPosition);
        }
    }

    public void updateTypeSpinner() {
        if (type_entries.size()>0){
            Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);
            ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, type_entries);
            dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_spinner.setAdapter(dataAdapterType);
            int spinnerPosition = dataAdapterType.getPosition(type_entries.get(type_entries.size()-1));
            type_spinner.setSelection(spinnerPosition);
        }
    }

    /*
        Executed when search button is pressed.
    */
    public void OnSearch(View view) {
        EditText ingredients_editText = (EditText) findViewById(R.id.ingredients_editText);

        try {
            Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);
            Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);

            if (true){
                if (category_spinner.getSelectedItem().toString().isEmpty() &&
                        type_spinner.getSelectedItem().toString().isEmpty() &&
                        ingredients_editText.getText().toString().trim().isEmpty()) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, "You need to select category/type or choose ingredients", duration);

                    toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
                    toast.show();
                }

                else if (!ingredients_editText.getText().toString().trim().isEmpty()){

                    if (ingredients_editText.getText().toString().trim().equals("and") ||
                            ingredients_editText.getText().toString().trim().equals("not")) {
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(this, "Not valid input!", duration);

                        toast.setGravity(Gravity.TOP|Gravity.LEFT, 450, 430);
                        toast.show();

                    }
                    else  {
                        ArrayList<Recipe> orderedRecipes =
                                findRelevantRecipes(ingredients_editText.getText().toString(), recipeDB);
                        for (Recipe r : orderedRecipes) {
                            System.out.println(r.getRecipeName());
                        }
                        if (orderedRecipes.size() == 0){
                            System.out.println("No recipes found");
                        }
                    }
                }
                else if (!category_spinner.getSelectedItem().toString().isEmpty() ||
                        !type_spinner.getSelectedItem().toString().isEmpty()) {
                    ArrayList<Recipe> orderedRecipes =
                            findRelevantRecipesByCategoryOrType(
                                    type_spinner.getSelectedItem().toString(),
                                    category_spinner.getSelectedItem().toString(), recipeDB);
                    for (Recipe r : orderedRecipes) {
                        System.out.println(r.getRecipeName());
                    }
                        if (orderedRecipes.size() == 0) {
                            System.out.println("No recipesss found");
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        //public ArrayList<Recipe> findRelevantRecipes (String userInput, CreateDB database)
        //THE METHOD ABOVE SHOULD BE CALLED

    }


    /**
     * Gets all the ingredients of a recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public ArrayList<String> getIngredientsList(String recipe) throws IOException{

        String[] temp =  recipe.split("\\|")[4].split("`");
        ArrayList<String> ingredients = new ArrayList<>();

        for (int i=0; i<temp.length; i++){
            ingredients.add(temp[i]);
        }
        return ingredients;
    }

    /**
     * Gets the steps of a recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public ArrayList<String> getStepsList(String recipe) throws IOException{

        String[] temp =  recipe.split("\\|")[3].split("`");
        ArrayList<String> steps = new ArrayList<String>();

        for (int i=0; i<temp.length; i++){
            steps.add(temp[i]);
        }
        return steps;
    }

    /**
     * Gets the category of the recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public String getCategory(String recipe) throws IOException{

        String[] category =  recipe.split("\\|")[1].split("`");
        return category[0] ;
    }

    /**
     * Gets the type of the recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public String getType(String recipe) throws IOException{

        String[] type =  recipe.split("\\|")[2].split("`");
        return type[0] ;
    }

    /**
     * Gets name of the recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public static String getName(String recipe) throws IOException{

        String[] name =  recipe.split("\\|")[0].split("`");
        return name[0] ;
    }


    /**
     * Formats user input ingredients into a list of two arrayLists (index 0 is all wanted
     * ingredients, index 1 is not wanted ingredients)
     * @param input
     * @return
     */
    public ArrayList<ArrayList<String>> setUserInput (String input){

        String[] stringInput = input.split(" ");
        ArrayList<String> andIngredients = new ArrayList<String>();
        ArrayList<String> notIngredients = new ArrayList<String>();
        ArrayList<ArrayList<String>> userInput = new ArrayList<>();

        for (int i = 0; i<stringInput.length; i++){
            if (!stringInput[i].equals("and") && !stringInput[i].equals("not") ){
                try{
                    if (!stringInput[i-1].equals("not")){
                        andIngredients.add(stringInput[i]);
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    andIngredients.add(stringInput[i]);
                }
            }
            if (stringInput[i].equals("not") && !stringInput[stringInput.length-1].equals("not")){
                notIngredients.add(stringInput[i+1]);
            }
        }
        userInput.add(andIngredients);
        userInput.add(notIngredients);
        return userInput;
    }

    /**
     * Finds recipes according to category and/or type when no ingredients are inputted
     * @param type
     * @param category
     * @param database
     * @return
     * @throws IOException
     */

    public ArrayList<Recipe> findRelevantRecipesByCategoryOrType (String type,
                                                                   String category,
                                                                   CreateDB database)throws IOException{

        ArrayList<String> databaseRecipes = database.getAsArrayList();
        ArrayList<String> relevantRecipes = new ArrayList<String>();

        for (int i=0; i<databaseRecipes.size(); i++){
            if (getType(databaseRecipes.get(i)).equals(type) &&
                    (getCategory(databaseRecipes.get(i)).equals(category))){
                relevantRecipes.add(databaseRecipes.get(i));
            }
            else if (getType(databaseRecipes.get(i)).equals(type) && category.isEmpty()){
                relevantRecipes.add(databaseRecipes.get(i));
            }
            else if (type.isEmpty() && getCategory(databaseRecipes.get(i)).equals(category)){
                relevantRecipes.add(databaseRecipes.get(i));
            }
        }

        return convertStringToRecipe(relevantRecipes);
    }


    /**
     * Takes in user input and finds relevant recipes in the database
     * @param userInput
     * @param database
     * @throws IOException
     */
    public ArrayList<Recipe> findRelevantRecipes (String userInput, CreateDB database)
            throws IOException{

        ArrayList<String> databaseRecipes = database.getAsArrayList();
        ArrayList<String> relevance = new ArrayList<>();
        ArrayList<String> relevantRecipes = new ArrayList<String>();
        ArrayList<ArrayList<String>> userIngredients = setUserInput(userInput);
        ArrayList<ArrayList<String>> indexedRecipes = new ArrayList<ArrayList<String>>();

        Spinner category_spinner = (Spinner) findViewById(R.id.category_spinner);
        Spinner type_spinner = (Spinner) findViewById(R.id.type_spinner);

        String desiredCategory = category_spinner.getSelectedItem().toString();
        String desiredType = type_spinner.getSelectedItem().toString();

        for (int i=0; i<databaseRecipes.size(); i++) {
            ArrayList<String> recipeIngredients = getIngredientsList(databaseRecipes.get(i));
            if (Integer.parseInt(haveAnyCommonElems(recipeIngredients, userIngredients.get(0))) ==
                    userIngredients.get(0).size() &&
                    Integer.parseInt(haveAnyCommonElems(recipeIngredients, userIngredients.get(1))) == 0) {

                if (desiredCategory.isEmpty() && getType(databaseRecipes.get(i)).equals(desiredType)) {
                    relevantRecipes.add(databaseRecipes.get(i));
                    relevance.add(getPercentRelevance(recipeIngredients, userIngredients.get(0)));
                } else if (desiredType.isEmpty() && getCategory(databaseRecipes.get(i)).equals(desiredCategory)) {
                    relevantRecipes.add(databaseRecipes.get(i));
                    relevance.add(getPercentRelevance(recipeIngredients, userIngredients.get(0)));
                } else if (getCategory(databaseRecipes.get(i)).equals(desiredCategory) &&
                        getType(databaseRecipes.get(i)).equals(desiredType)) {
                    relevantRecipes.add(databaseRecipes.get(i));
                    relevance.add(getPercentRelevance(recipeIngredients, userIngredients.get(0)));
                } else if (desiredCategory.isEmpty() && desiredType.isEmpty()){
                    relevantRecipes.add(databaseRecipes.get(i));
                    relevance.add(getPercentRelevance(recipeIngredients, userIngredients.get(0)));
                }
            }
        }
        indexedRecipes.add(relevantRecipes);
        indexedRecipes.add(relevance);
        return convertStringToRecipe(sortByRelevance(indexedRecipes));
    }

    /**
     * Sorts the list of recipes in the order of relevance
     * @param indexedRecipes
     * @return
     */
    public ArrayList<String> sortByRelevance (ArrayList<ArrayList<String>> indexedRecipes){
        int n = indexedRecipes.get(1).size();
        int tempIndices = 0;
        String tempRecipes;
        for(int i=0; i < n; i++){
            for (int j=1; j < n-i; j++) {

                if (Integer.parseInt(indexedRecipes.get(1).get(j-1))
                        < Integer.parseInt(indexedRecipes.get(1).get(j))) {

                    tempIndices = Integer.parseInt(indexedRecipes.get(1).get(j-1));
                    tempRecipes = indexedRecipes.get(0).get(j-1);

                    indexedRecipes.get(1).add(j-1, indexedRecipes.get(1).get(j));
                    indexedRecipes.get(1).remove(j); //FOR PERCENT RELEVANCE

                    indexedRecipes.get(0).add(j-1, indexedRecipes.get(0).get(j));
                    indexedRecipes.get(0).remove(j); //FOR RECIPES

                    indexedRecipes.get(1).add(j, Integer.toString(tempIndices));
                    indexedRecipes.get(1).remove(j+1);

                    indexedRecipes.get(0).add(j, tempRecipes);
                    indexedRecipes.get(0).remove(j+1);
                }
            }
        }
        return indexedRecipes.get(0);
    }

    /**
     * HELPER METHOD for converting ArrayList<String> to ArrayList<Recipe>
     * @param stringList
     * @return
     * @throws IOException
     */
    private ArrayList<ArrayList<Ingredient>> getListOfIngredientList(ArrayList<String> stringList) throws IOException{
        ArrayList<ArrayList<Ingredient>> listOfIngredientList = new ArrayList<ArrayList<Ingredient>>();
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        Ingredient sep = new Ingredient("sep"); //Seperator

        for (int j=0; j<stringList.size(); j++){
            for (int i=0; i<getIngredientsList(stringList.get(j)).size(); i++){
                Ingredient in = new Ingredient(getIngredientsList(stringList.get(j)).get(i));
                ingredientList.add(in);
            }
            ingredientList.add(sep);
        }
        if (ingredientList.size() != 0) {
            ingredientList.remove(ingredientList.size() - 1);
        }

        int prev = 0;
        for (int cur = 0; cur < ingredientList.size(); cur++) {
            if (ingredientList.get(cur).equals(sep)) {
                listOfIngredientList.add(new ArrayList<Ingredient>(ingredientList.subList(prev, cur)));
                prev = cur + 1;
            }
        }
        listOfIngredientList.add(new ArrayList<Ingredient>(ingredientList.subList(prev, ingredientList.size())));
        return listOfIngredientList;
    }

    /**
     * HELPER METHOD for converting ArrayList<String> to ArrayList<Recipe>
     * @param stringList
     * @return
     * @throws IOException
     */
    private ArrayList<ArrayList<RecipeStep>> getListOfStepsList(ArrayList<String> stringList) throws IOException{
        ArrayList<ArrayList<RecipeStep>> listOfStepsList = new ArrayList<ArrayList<RecipeStep>>();
        ArrayList<RecipeStep> stepsList = new ArrayList<>();
        RecipeStep sep = new RecipeStep("sep"); //Seperator

        for (int j=0; j<stringList.size(); j++){
            for (int i=0; i<getStepsList(stringList.get(j)).size(); i++){
                RecipeStep step = new RecipeStep(getStepsList(stringList.get(j)).get(i));
                stepsList.add(step);
            }
            stepsList.add(sep);
        }
        if (stepsList.size() != 0) {
            stepsList.remove(stepsList.size() - 1);
        }

        int prev = 0;
        for (int cur = 0; cur < stepsList.size(); cur++) {
            if (stepsList.get(cur).equals(sep)) {
                listOfStepsList.add(new ArrayList<RecipeStep>(stepsList.subList(prev, cur)));
                prev = cur + 1;
            }
        }
        listOfStepsList.add(new ArrayList<RecipeStep>(stepsList.subList(prev, stepsList.size())));
        return listOfStepsList;
    }


    /**
     * Takes in a ArrayList<String> type and converts it into a ArrayList<Recipe> type
     * @param stringList
     * @return
     * @throws IOException
     */
    public ArrayList<Recipe> convertStringToRecipe (ArrayList<String> stringList) throws IOException {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        ArrayList<ArrayList<Ingredient>> ingredients = new ArrayList<ArrayList<Ingredient>>();
        ArrayList<ArrayList<RecipeStep>> steps = new ArrayList<ArrayList<RecipeStep>>();

        ingredients = getListOfIngredientList(stringList);
        steps = getListOfStepsList(stringList);

        for (int i=0; i<stringList.size(); i++){
            RecipeCategory category = new RecipeCategory(getCategory(stringList.get(i)));
            RecipeType type = new RecipeType(getType(stringList.get(i)));
            String name = getName(stringList.get(i));
            Recipe recipe = new Recipe(ingredients.get(i), steps.get(i), category,type, name);
            recipes.add(recipe);
        }
        return recipes;
    }

    /**
     * Checks how many elems are common
     * @param l1
     * @param l2
     * @return
     */
    public static String haveAnyCommonElems (ArrayList<String> l1, ArrayList<String> l2){
        int counter = 0;
        for (int i=0; i<l1.size(); i++){
            for (int j=0; j<l2.size(); j++){
                if (l1.get(i).equals(l2.get(j))){
                    counter++;
                }
            }
        }
        return Integer.toString(counter);
    }

    /**
     *
     * @param recipeIngredients
     * @param userInput
     * @return
     */
    public static String getPercentRelevance (ArrayList<String> recipeIngredients,
                                              ArrayList<String> userInput ){
        double userInputAsDouble = userInput.size();
        double recipeIngAsDouble = recipeIngredients.size();
        double percentRelevance = (userInputAsDouble/recipeIngAsDouble)*100.0;
        return Integer.toString((int)percentRelevance);
    }
}
