package com.uottawa.keenan.cookhelper;

/**
 * Created by Keenan on 2016-10-25.
 */

public class RecipeType {

    String recipe_type;

    public RecipeType(String recipe_type) {
        this.recipe_type = recipe_type;
    }

    public String getRecipeCategory() {
        return this.recipe_type;
    }

    public void setRecipeCategory(String recipe_type) {
        this.recipe_type = recipe_type;
    }
}
