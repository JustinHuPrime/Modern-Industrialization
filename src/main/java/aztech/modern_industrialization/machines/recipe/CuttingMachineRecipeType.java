/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.machines.recipe;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CuttingMachineRecipeType extends ProxyableMachineRecipeType {
    public CuttingMachineRecipeType(ResourceLocation id) {
        super(id);
    }

    @Override
    protected void fillRecipeList(Level world, List<RecipeHolder<MachineRecipe>> recipeList) {
        // Add all regular cutting machine recipes
        recipeList.addAll(getManagerRecipes(world));
        // Add all stone cutter recipes
        for (var stonecuttingRecipe : world.getRecipeManager().getAllRecipesFor(RecipeType.STONECUTTING)) {
            var recipe = RecipeConversions.ofStonecutting(stonecuttingRecipe, this, world.registryAccess());
            if (recipe != null) {
                recipeList.add(recipe);
            }
        }
    }
}
