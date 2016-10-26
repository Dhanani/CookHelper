# CookHelper

An Android application.

## Coding Standards

1. Any front end element's (Button, TextView, Spinner, etc) accompanying back end connector method (OnClick, etc) must be written a certain way. For buttons, for example, the `id` of a button should be `add_recipe_btn` and it's accompanying `OnClick` method should be the *SAME* thing without the `btn` part, underscores (_), and capatalize all word beginnings. In this case, it would be `OnAddRecipe`.

2. No broken shit in `master`. No exceptions. If your feature does not work fully (I.e. Crashes for an unknown reason), ask a team member for help. Partial feature are allowed as long as they don't produce any errors/crashes.

3. Think of this application as one on the playstore/appstore. Think of actions people would do in this app. Remember, no bugs allowed in master.

## Things To Do:
1. A `Recipe` class. This will store a *list* of `Ingredients`, the name of the Recipe, a *list* of the Recipe steps, the Recipe Category and the Recipe Type. There is a class for Ingredient, RecipeStep, RecipeCategory, and RecipeType already. You must respect the existing classes in the implementation. You must also create a class RecipeName which is responsible for storing the name of a Recipe (String). The class will look similar to class Ingredient. I am also planning on keeping track of the caleries per food creation. That can be in a class variable (in Recipe) of type int.
