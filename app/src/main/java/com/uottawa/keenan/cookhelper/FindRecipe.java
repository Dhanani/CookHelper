package com.uottawa.keenan.cookhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FindRecipe extends AppCompatActivity {

    public CreateDB recipeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_recipe);

        try {
            recipeDB = new CreateDB(getApplicationContext(), "RecipeDB.txt");
            recipeDB.readContents();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void OnSearch(View view) {

//        String userInput = ((EditText)findViewById(R.id.ingredients_editText)).getText()
//                .toString();
//        try {
//            ArrayList<String> relevantRecipes = findRelevantRecipes(userInput, recipeDB);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**STILL NEEDS TO BE IMPLEMENTED
     * This method needs to get each line of recipe in the database and needs to
     * put it into an ArrayList<String>
     * ALSO CHANGE FIRST LINE RELATED TO THIS METHOD IN THE FindReleventRecipes METHOD
     */
//    public ArrayList<String> getRecipeList(CreateDB recipeDB){
//        ArrayList<String> recipes = recipeDB.getAsArrayList();
//        return recipes;
//    }

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
     * Formats user input ingredients into a list of two arrayLists (index 0 is all wanted
     * ingredients, index 1 is not wanted ingredients)
     * @param input
     * @return
     */
    public ArrayList<ArrayList<String>> setUserInput (String input){

        String[] temp = input.split(" ");
        ArrayList<String> andIngredients = new ArrayList<String>();
        ArrayList<String> notIngredients = new ArrayList<String>();
        ArrayList<ArrayList<String>> userInput = new ArrayList<>();

        for (int i = 0; i<temp.length; i++){

            if (!temp[i].equals("and")){
                andIngredients.add(temp[i]);
            }
            if (temp[i].equals("not")){
                notIngredients.add(temp[i+1]);
            }
        }
        userInput.add(andIngredients);
        userInput.add(notIngredients);
        return userInput;
    }

    /**
     * Takes in user input and finds relevant recipes in the database
     * @param userInput
     * @param database
     * @throws IOException
     */
    public ArrayList<String> findRelevantRecipes (String userInput, CreateDB database)
            throws IOException{

        ArrayList<String> databaseRecipes = database.getAsArrayList();
        ArrayList<String> relevance = new ArrayList<>();
        ArrayList<String> relevantRecipes = new ArrayList<String>();
        ArrayList<ArrayList<String>> userIngredients = setUserInput(userInput);
        ArrayList<ArrayList<String>> indexedRecipes = new ArrayList<ArrayList<String>>();

        for (int i=0; i<databaseRecipes.size(); i++){
            ArrayList<String> temp = getIngredientsList(databaseRecipes.get(i));
            if (Integer.parseInt(haveAnyCommonElems(temp, userIngredients.get(0))) > 0 &&
                    Integer.parseInt(haveAnyCommonElems(temp, userIngredients.get(1))) == 0){
                relevantRecipes.add(databaseRecipes.get(i));
                relevance.add(haveAnyCommonElems(temp, userIngredients.get(0)));
            }
        }
        indexedRecipes.add(relevantRecipes);
        indexedRecipes.add(relevance);
        return sortByRelevance(indexedRecipes);
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

                    tempIndices = Integer.parseInt(indexedRecipes.get(1).get(i));
                    tempRecipes = indexedRecipes.get(0).get(i);

                    indexedRecipes.get(1).add(j-1, indexedRecipes.get(1).get(j));
                    indexedRecipes.get(1).remove(j); //FOR COUNTER

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
}
