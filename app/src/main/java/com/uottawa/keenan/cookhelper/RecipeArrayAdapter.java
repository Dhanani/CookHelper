package com.uottawa.keenan.cookhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;




public class RecipeArrayAdapter extends ArrayAdapter <String> {

    private final Context context;
    private final ArrayList<String> values;
    public CreateDB recipeDB;


    public RecipeArrayAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.activity_recipe_list_content, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            recipeDB = new CreateDB(context.getApplicationContext(), "RecipeDB.txt");
        } catch (IOException e){
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_recipe_list_content, parent, false);
        TextView textViewRecipeName = (TextView) rowView.findViewById(R.id.recipe_result_name);
        TextView textViewRecipeCategory = (TextView) rowView.findViewById(R.id.recipe_result_cate);
        TextView textViewRecipeType = (TextView) rowView.findViewById(R.id.recipe_result_type);

        try {
            String fullDBRecipe = getFullDatabaseRecipe(values.get(position), recipeDB);
            textViewRecipeName.setText(getName(fullDBRecipe));
            textViewRecipeCategory.setText(getCategory(fullDBRecipe));
            textViewRecipeType.setText(getType(fullDBRecipe));
        } catch (IOException e) {
            e.printStackTrace();

        }

        return rowView;
    }

    public String getCategory(String recipe) throws IOException {
        System.out.println(recipe);
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
     * Takes the recipe name and returns the whole string from the db
     * @param recipe
     * @param database
     * @return
     * @throws IOException
     */
    public String getFullDatabaseRecipe (String recipe, CreateDB database)throws IOException{
        ArrayList<String> databaseRecipes = database.getAsArrayList();
        for (int i=0; i<databaseRecipes.size(); i++) {
            if (getName(databaseRecipes.get(i)).equals(recipe)) {
                return databaseRecipes.get(i);
            }
        }
        return null;
    }

}
