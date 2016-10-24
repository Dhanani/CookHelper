package com.uottawa.keenan.cookhelper;

/**
 * Created by Keenan on 2016-10-24.
 */

public class Ingredient {
    String ingredient;

    public Ingredient(String step) {
        this.ingredient = step;
    }

    public String getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(String step) {
        this.ingredient = step;
    }
}
