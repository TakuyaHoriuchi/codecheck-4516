package recipe.entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import recipe.dto.Recipe;
import recipe.dto.ResponseDto;

@RestController
public class RecipiRestController {
    @Autowired
    DataSourceProfiler dbSetup;

    /**
     * 0:パスをGETした時
     * RequestMethod.GET
     * @param reqDto
     * @return 
     */
    @RequestMapping(method=RequestMethod.GET, value="/")
    @ResponseStatus(value = HttpStatus.OK)
    public String getRoot() {
        return "welcome";
    }

    
    /**
     * ①レシピ全件を取得するメソッド。
     * RequestMethod.GET
     * @param reqDto
     * @return 
     */
    @RequestMapping(method=RequestMethod.GET, value="recipes", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseDto getAll() {
        ResponseDto response = new ResponseDto();
        List<Recipe> recipes = new ArrayList<>();
        response.setRecipes(recipes);
        
        try(Connection conn = dbSetup.getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM recipes")
            ) {
            while(resultSet.next()) {
                Recipe recipe = getRecipe(resultSet);
                recipe.setId(resultSet.getInt("id"));
                recipes.add(recipe);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * ②id指定でレシピを取得するメソッド。
     * RequestMethod.GET
     * @param reqDto
     * @return 
     */
    @RequestMapping(method=RequestMethod.GET, value="recipes/{id}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseDto getIdRecipe(@PathVariable("id") int id) {
        ResponseDto response = new ResponseDto();
        List<Recipe> recipes = new ArrayList<>();
        response.setRecipes(recipes);
        
        try(Connection conn = dbSetup.getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM recipes")
            ) {
            while(resultSet.next()) {
                if(resultSet.getInt("id") == id) {
                    Recipe recipe = getRecipe(resultSet);
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!response.getRecipes().isEmpty()) {
            response.setMessage("Recipe details by id");
            return response;
        } else {
            response.setMessage("No Recipe found");
            return response;
        }
    }
    
    /**
     * ③レシピを登録するメソッド。
     * RequestMethod.POST
     * @param reqDto
     * @return 
     */
    @RequestMapping(method=RequestMethod.POST, value="recipes", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseDto registerRecipe(@RequestBody Recipe requestRecipe) {
        ResponseDto response = new ResponseDto();
        List<Recipe> recipes = new ArrayList<>();
        response.setRecipes(recipes);
        
        String sql = "INSERT INTO recipes values(?, ?, ?, ?, ?, ?)";
        int insertRecords = 0;
        
        if(!StringUtils.isEmpty(requestRecipe.getTitle()) &&
           !StringUtils.isEmpty(requestRecipe.getMaking_time()) &&
           !StringUtils.isEmpty(requestRecipe.getServes()) &&
           !StringUtils.isEmpty(requestRecipe.getIngredients()) &&
           !StringUtils.isEmpty(requestRecipe.getCost()) ) {
            
            try(Connection conn = dbSetup.getDataSource().getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)
                    ) {
                ps.setInt(1, 3);
                ps.setString(2, requestRecipe.getTitle());
                ps.setString(3, requestRecipe.getMaking_time());
                ps.setString(4, requestRecipe.getServes());
                ps.setString(5, requestRecipe.getIngredients());
                ps.setInt(6, Integer.parseInt(requestRecipe.getCost()));
                insertRecords = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(insertRecords > 0) {
            response.setMessage("Recipe successfully created!");
            response.getRecipes().add(requestRecipe);
            return response;
        } else {
            response.setMessage("Recipe creation failed!");
            response.setRequired("title, making_time, serves, ingredients, cost");
            return response;
        }
    }
    
    /**
     * ④レシピを更新するメソッド。
     * RequestMethod.PATCH
     * @param reqDto
     * @return 
     */
    @RequestMapping(method=RequestMethod.PATCH, value="recipes/{id}", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseDto updateRecipe(@RequestBody Recipe requestRecipe, @PathVariable("id") int id) {
        ResponseDto response = new ResponseDto();
        List<Recipe> recipes = new ArrayList<>();
        response.setRecipes(recipes);
        
        String sql = "UPDATE recipes SET title=?, making_time=?, serves=?, ingredients=?, cost=? where id = ?";
        int updateRecords = 0;
        
        try(Connection conn = dbSetup.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
            ) {
            ps.setString(1, requestRecipe.getTitle());
            ps.setString(2, requestRecipe.getMaking_time());
            ps.setString(3, requestRecipe.getServes());
            ps.setString(4, requestRecipe.getIngredients());
            ps.setInt(5, Integer.parseInt(requestRecipe.getCost()));
            ps.setInt(6, id);
            
            updateRecords = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(updateRecords > 0) {
            response.setMessage("Recipe successfully updated!");
            response.getRecipes().add(requestRecipe);
            return response;
        } else {
            response.setMessage("Recipe creation failed!");
            response.setRequired("title, making_time, serves, ingredients, cost");
            return response;
        }
    }
    
    
    /**
     * ⑤レシピを削除するメソッド。
     * RequestMethod.DELETE
     * @param reqDto
     * @return 
     */
    @RequestMapping(method=RequestMethod.DELETE, value="recipes/{id}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseDto deleteRecipe(@PathVariable("id") int id) {
        ResponseDto response = new ResponseDto();
        List<Recipe> recipes = new ArrayList<>();
        response.setRecipes(recipes);
        
        String sql = "DELETE FROM recipes where id = ?";
        int deleteRecords = 0;
        
        try(Connection conn = dbSetup.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
            ) {
            ps.setInt(1, id);
            deleteRecords = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(deleteRecords > 0) {
            response.setMessage("Recipe successfully removed!");
            return response;
        } else {
            response.setMessage("No Recipe found");
            return response;
        }
    }
    
    
    /**
     * SQLの取得結果からレシピ情報を取り出すメソッド。
     * 
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Recipe getRecipe(ResultSet resultSet) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setTitle(resultSet.getString("title"));
        recipe.setMaking_time(resultSet.getString("making_time"));
        recipe.setServes(resultSet.getString("serves"));
        recipe.setIngredients(resultSet.getString("ingredients"));
        recipe.setCost(resultSet.getString("cost"));
        return recipe;
    }
    
}
