package com.uottawa.keenan.cookhelper;

/**
 * Created by Keenan on 2016-10-25.
 */

public class RecipeType {

    String recipe_type;

    public RecipeType(String recipe_type) {
        this.recipe_type = recipe_type.trim().toLowerCase();
    }

    public String getRecipeType() {
        return this.recipe_type;
    }

    public void setRecipeType(String recipe_category) {
        this.recipe_type = recipe_category;
    }

    public boolean equals(RecipeType other) {
        return (this.recipe_type.equals(other.getRecipeType()) );
    }
}
