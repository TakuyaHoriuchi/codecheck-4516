package recipe.entry;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import recipe.dto.Recipe;
import recipe.dto.ResponseDto;

/**
 * {@link:RecipiRestController}のテスト。
 *
 */
public class RecipiRestControllerTest {
    @Spy
    RecipiRestController controller;

    @Before
    public void setUp() throws Exception {
        controller = new RecipiRestController();
    }

    @Test
    public void test_正常系_0_Rootアクセスでエラーが出ないことの検証() {
        String actual = controller.getRoot();
        assertThat(actual, is("welcome"));
    }
    
    @Test
    public void test_正常系_1_全件取得出来ることの検証() {
        ResponseDto actualResponse = controller.getAll();
        assertTrue(actualResponse.getRecipes().size() >= 0);
    }
    
    @Test
    public void test_正常系_2_ID指定でレシピが取得出来ることの検証() {
        int id = 2;
        ResponseDto actualResponse = controller.getIdRecipe(id);
        assertThat(actualResponse.getRecipes().get(0).getId(), is(id));
    }
    
    @Test
    public void test_正常系_3_レシピが登録出来ることの検証() {
        Recipe recipe = new Recipe();
        recipe.setTitle("test");
        recipe.setMaking_time("15min");
        recipe.setServes("test1, test2");
        recipe.setIngredients("testRecipe");
        recipe.setCost("15000");
        ResponseDto responseDto = controller.registerRecipe(recipe);
        assertThat(responseDto.getRecipes().get(0), is(recipe));
    }
    
    @Test
    public void test_正常系_4_レシピの更新が出来ることの検証() {
        Recipe recipe = new Recipe();
        recipe.setTitle("test");
        recipe.setMaking_time("75min");
        recipe.setServes("test1, test2");
        recipe.setIngredients("testRecipe");
        recipe.setCost("15000");
        
        int id = 3;
        ResponseDto responseDto = controller.updateRecipe(recipe, id);
        assertThat(responseDto.getRecipes().get(id - 1).getMaking_time(), is("75min"));
    }
    
    @Test
    public void test_正常系_5_レシピを削除出来ることの検証() {
        int id = 3;
        ResponseDto actualResponse = controller.deleteRecipe(id);
        assertNull(actualResponse.getRecipes().get(id - 1));
    }
    

}
