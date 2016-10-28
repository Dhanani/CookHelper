package com.uottawa.keenan.cookhelper;


import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Keenan on 2016-10-28.
 */

public class Recipe {
    private String recipe_name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<RecipeStep> recipe_steps;
    private RecipeCategory category;
    private RecipeType type;

    public Recipe(ArrayList<Ingredient> ingredients,ArrayList<RecipeStep> steps, RecipeCategory category, RecipeType type) {
        this.ingredients = ingredients;
        this.recipe_steps = steps;
        this.category = category;
        this.type = type;
    }

    public String getRecipeName(){
        return this.recipe_name;
    }

    public ArrayList<Ingredient> getIngredients(){
        return this.ingredients;
    }

    public ArrayList<RecipeStep> getRecipeSteps(){
        return this.recipe_steps;
    }

    public RecipeCategory getRecipeCategory(){
        return this.category;
    }

    public RecipeType getRecipeType(){
        return this.type;
    }
}
