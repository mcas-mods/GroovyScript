package com.cleanroommc.groovyscript.compat.mods.actuallyadditions;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.StandardListRegistry;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CrusherRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@RegistryDescription
public class Crusher extends StandardListRegistry<CrusherRecipe> {

    @RecipeBuilderDescription(example = {
            @Example(".input(item('minecraft:clay')).output(item('minecraft:diamond'), item('minecraft:diamond')).chance(100)"),
            @Example(".input(item('minecraft:diamond_block')).output(item('minecraft:diamond') * 12)")
    })
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    @Override
    public Collection<CrusherRecipe> getRecipes() {
        return ActuallyAdditionsAPI.CRUSHER_RECIPES;
    }

    public CrusherRecipe add(Ingredient input, ItemStack output) {
        return add(input, output, ItemStack.EMPTY, 100);
    }

    public CrusherRecipe add(Ingredient input, ItemStack output, ItemStack secondaryOutput, int chance) {
        CrusherRecipe recipe = new CrusherRecipe(input, output, secondaryOutput, chance);
        add(recipe);
        return recipe;
    }

    @MethodDescription(example = @Example("item('minecraft:bone')"))
    public boolean removeByInput(IIngredient input) {
        return getRecipes().removeIf(recipe -> {
            boolean found = recipe.getInput().test(IngredientHelper.toItemStack(input));
            if (found) {
                addBackup(recipe);
            }
            return found;
        });
    }

    @MethodDescription(example = @Example("item('minecraft:sugar')"))
    public boolean removeByOutput(ItemStack output) {
        return getRecipes().removeIf(recipe -> {
            boolean matches = ItemStack.areItemStacksEqual(recipe.getOutputOne(), output);
            if (matches) {
                addBackup(recipe);
            }
            return matches;
        });
    }

    @Property(property = "input", comp = @Comp(eq = 1))
    @Property(property = "output", comp = @Comp(gte = 1, lte = 2))
    public static class RecipeBuilder extends AbstractRecipeBuilder<CrusherRecipe> {

        @Property(comp = @Comp(gte = 0, lte = 100))
        private int chance;

        @RecipeBuilderMethodDescription
        public RecipeBuilder chance(int chance) {
            this.chance = chance;
            return this;
        }

        @Override
        protected int getMaxItemInput() {
            return 1;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding Actually Additions Crusher recipe";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateItems(msg, 1, 1, 1, 2);
            validateFluids(msg);
            msg.add(chance < 0 || chance > 100, "chance must be a non negative integer less than 100, yet it was {}", chance);
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable CrusherRecipe register() {
            if (!validate()) return null;
            CrusherRecipe recipe = new CrusherRecipe(input.get(0).toMcIngredient(), output.get(0), output.size() < 2 ? ItemStack.EMPTY : output.get(1), chance);
            ModSupport.ACTUALLY_ADDITIONS.get().crusher.add(recipe);
            return recipe;
        }
    }
}
