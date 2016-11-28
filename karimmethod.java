public ArrayList<String> getIngredientsUsedInRecipes(){

        if(dbName!="RecipeDB.txt"){
            return null;
        }

        ArrayList<String> allIngredients = new ArrayList<>(size);

        try  {
            InputStream input = new FileInputStream(myDataBase);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {

                String[] recipeIngredients;
                recipeIngredients = line.split("\\|")[4].split("`");

                for(int i=0; i<recipeIngredients.length; i++) {
                    if(!allIngredients.contains(recipeIngredients[i])){
                        allIngredients.add(recipeIngredients[i]);
                        //System.out.println(recipeIngredients[i]);
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

        return allIngredients;

    }
