package com.uottawa.keenan.cookhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FindRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_recipe);
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
        //THIS WILL BE USED TO CALL THE findRelevantRecipes METHOD WITH ITS APPROPRIATE PARAMS

    }

    /**STILL NEEDS TO BE IMPLEMENTED
     * This method needs to get each line of recipe in the database and needs to
     * put it into an ArrayList<String>
     * ALSO CHANGE FIRST LINE RELATED TO THIS METHOD IN THE FindReleventRecipes METHOD
     */
    public ArrayList<String> getRecipeList(){

        ArrayList<String> recipes = new ArrayList<>();
        return recipes;
    }

    /**
     * Gets all the ingredients of a recipe
     * @param recipe
     * @return
     * @throws IOException
     */
    public ArrayList<String> getIngredientsList(String recipe) throws IOException{

        String[] temp =  recipe.split("\\|")[4].split("`");
        ArrayList<String> ingredients = new ArrayList<String>();

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
    public Queue<String> findRelevantRecipes (String userInput, String database) throws IOException{

        ArrayList<String> databaseRecipes = getRecipeList();

        Queue<String> relevantRecipes = new LinkedList<String>();
        ArrayList<ArrayList<String>> userIngredients = setUserInput(userInput);

        for (int i=0; i<databaseRecipes.size(); i++){
            ArrayList<String> temp = getIngredientsList(databaseRecipes.get(i));
            if (haveAnyCommonElems(temp, userIngredients.get(0)) && !haveAnyCommonElems(temp,
                    userIngredients.get(1))){
                relevantRecipes.add(databaseRecipes.get(i).split("\\|")[0]);
            }
        }
        return relevantRecipes;
    }

    /**
     * Checks if there are any common elems in two lists
     * @param l1
     * @param l2
     * @return
     */
    public boolean haveAnyCommonElems (ArrayList<String> l1, ArrayList<String> l2){

        for (int i=0; i<l1.size(); i++){
            for (int j=0; j<l2.size(); j++){
                if (l1.get(i).equals(l2.get(j))){
                    return true;
                }
            }
        }
        return false;
    }
}
