package com.fizz.overflowcodex.recipe;

import com.fizz.overflowcodex.OverflowCodex;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AwakeningRecipe implements Recipe<SimpleContainer> {
    public static final Serializer SERIALIZER = new Serializer();
    private final ResourceLocation id;
    private final NonNullList<Ingredient> pedestalIngredients;
    private final Ingredient sourceGem;
    private final ItemStack result;
    private final int manaCost;

    public AwakeningRecipe(ResourceLocation id, NonNullList<Ingredient> pedestalIngredients,
                           Ingredient sourceGem, ItemStack result, int manaCost) {
        this.id = id;
        this.pedestalIngredients = pedestalIngredients;
        this.sourceGem = sourceGem;
        this.result = result;
        this.manaCost = manaCost;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        // Simplified matching - actual Ars Nouveau integration would check the apparatus
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess access) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.BREWING; // Placeholder; real impl uses Ars Nouveau apparatus type
    }

    public NonNullList<Ingredient> getPedestalIngredients() { return pedestalIngredients; }
    public Ingredient getSourceGem() { return sourceGem; }
    public int getManaCost() { return manaCost; }

    public static class Serializer implements RecipeSerializer<AwakeningRecipe> {
        @Override
        public AwakeningRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray pedestalArray = json.getAsJsonArray("pedestal_ingredients");
            NonNullList<Ingredient> pedestals = NonNullList.create();
            for (JsonElement element : pedestalArray) {
                pedestals.add(Ingredient.fromJson(element));
            }

            Ingredient sourceGem = Ingredient.fromJson(json.getAsJsonObject("source_gem"));
            JsonObject resultObj = json.getAsJsonObject("result");
            Item resultItem = net.minecraft.core.registries.BuiltInRegistries.ITEM
                    .get(new ResourceLocation(resultObj.get("item").getAsString()));
            ItemStack result = new ItemStack(resultItem, resultObj.has("count") ? resultObj.get("count").getAsInt() : 1);
            int manaCost = json.has("mana_cost") ? json.get("mana_cost").getAsInt() : 50000;

            return new AwakeningRecipe(id, pedestals, sourceGem, result, manaCost);
        }

        @Override
        public AwakeningRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            NonNullList<Ingredient> pedestals = NonNullList.create();
            for (int i = 0; i < size; i++) {
                pedestals.add(Ingredient.fromNetwork(buf));
            }
            Ingredient sourceGem = Ingredient.fromNetwork(buf);
            ItemStack result = buf.readItem();
            int manaCost = buf.readVarInt();
            return new AwakeningRecipe(id, pedestals, sourceGem, result, manaCost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AwakeningRecipe recipe) {
            buf.writeVarInt(recipe.pedestalIngredients.size());
            for (Ingredient ing : recipe.pedestalIngredients) {
                ing.toNetwork(buf);
            }
            recipe.sourceGem.toNetwork(buf);
            buf.writeItem(recipe.result);
            buf.writeVarInt(recipe.manaCost);
        }
    }
}
