package com.uottawa.keenan.cookhelper;

/**
 * Created by Keenan on 2016-10-25.
 */

public class RecipeCategory {

    String recipe_category;

    public RecipeCategory(String recipe_category) {
        this.recipe_category = recipe_category.trim().toLowerCase();
    }

    public String getRecipeCategory() {
        return this.recipe_category;
    }

    public void setRecipeCategory(String recipe_category) {
        this.recipe_category = recipe_category;
    }

    public boolean equals(RecipeCategory other) {
        return (this.recipe_category.equals(other.getRecipeCategory()) );
    }

    public String toString(){
        return getRecipeCategory();
    }
}
