package com.cleanroommc.groovyscript.compat.mods.essentialcraft;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.StandardListRegistry;
import essentialcraft.api.WindImbueRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@RegistryDescription
public class WindRune extends StandardListRegistry<WindImbueRecipe> {

    @RecipeBuilderDescription(example = @Example(".input(item('minecraft:gold_block')).output(item('minecraft:diamond_block')).espe(500)"))
    public WindRune.RecipeBuilder recipeBuilder() {
        return new WindRune.RecipeBuilder();
    }

    @Override
    public Collection<WindImbueRecipe> getRecipes() {
        return WindImbueRecipe.RECIPES;
    }

    @MethodDescription(example = @Example("item('minecraft:diamond')"))
    public boolean removeByInput(IIngredient x) {
        ItemStack[] stacks = x.getMatchingStacks();
        if (stacks.length == 0) return false;
        return getRecipes().removeIf(r -> {
            if (r.input.test(stacks[0])) {
                addBackup(r);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(example = @Example("item('essentialcraft:air_potion')"))
    public boolean removeByOutput(IIngredient x) {
        return getRecipes().removeIf(r -> {
            if (x.test(r.result)) {
                addBackup(r);
                return true;
            }
            return false;
        });
    }

    @Property(property = "input", comp = @Comp(eq = 1))
    @Property(property = "output", comp = @Comp(eq = 1))
    public static class RecipeBuilder extends AbstractRecipeBuilder<WindImbueRecipe> {

        @Property(comp = @Comp(gte = 1))
        private int espe;

        @RecipeBuilderMethodDescription
        public RecipeBuilder espe(int cost) {
            espe = cost;
            return this;
        }

        @Override
        protected int getMaxItemInput() {
            return 1;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding Wind Rune Recipe";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateItems(msg, 1, 1, 1, 1);
            validateFluids(msg);
            msg.add(espe < 1, "espe cost must be 1 or greater, got {}", espe);
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable WindImbueRecipe register() {
            if (!validate()) return null;
            Ingredient inputItem = input.get(0).toMcIngredient();
            WindImbueRecipe recipe = new WindImbueRecipe(inputItem, output.get(0), espe);  // also adds the recipe
            ModSupport.ESSENTIALCRAFT.get().windRune.addScripted(recipe);
            return recipe;
        }
    }
}
