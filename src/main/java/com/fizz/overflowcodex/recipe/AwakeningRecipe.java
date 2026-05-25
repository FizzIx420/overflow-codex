package com.fizz.overflowcodex.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AwakeningRecipe implements Recipe<RecipeInput> {
    public static final MapCodec<AwakeningRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.listOf().fieldOf("pedestal_ingredients").forGetter(r -> r.pedestalIngredients),
            Ingredient.CODEC.fieldOf("source_gem", Ingredient::EMPTY).forGetter(r -> r.sourceGem),
            ItemStack.ITEM_CODEC.fieldOf("result", ItemStack::EMPTY).forGetter(r -> r.result),
            com.mojang.serialization.Codec.INT.optionalFieldOf("mana_cost", 50000).forGetter(r -> r.manaCost)
    ).apply(instance, AwakeningRecipe::new));

    public static final Type TYPE = new Type();
    public static final Serializer SERIALIZER = new Serializer();

    private final NonNullList<Ingredient> pedestalIngredients;
    private final Ingredient sourceGem;
    private final ItemStack result;
    private final int manaCost;

    public AwakeningRecipe(NonNullList<Ingredient> pedestalIngredients,
                           Ingredient sourceGem, ItemStack result, int manaCost) {
        this.pedestalIngredients = pedestalIngredients;
        this.sourceGem = sourceGem;
        this.result = result;
        this.manaCost = manaCost;
    }

    @Override
    public boolean matches(RecipeInput container, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public NonNullList<Ingredient> getPedestalIngredients() { return pedestalIngredients; }
    public Ingredient getSourceGem() { return sourceGem; }
    public int getManaCost() { return manaCost; }

    public static class Type implements RecipeType<AwakeningRecipe> {
        public static final MapCodec<AwakeningRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        AwakeningRecipe.CODEC.forGetter(r -> r)
                ).apply(instance, (r) -> r)
        );

        @Override
        public MapCodec<AwakeningRecipe> codec() {
            return CODEC;
        }
    }

    public static class Serializer implements RecipeSerializer<AwakeningRecipe> {
        private static StreamCodec<RegistryFriendlyByteBuf, AwakeningRecipe> makeStreamCodec() {
            return StreamCodec.of(
                    (buf) -> {
                        int size = buf.readVarInt();
                        NonNullList<Ingredient> pedestals = NonNullList.create();
                        for (int i = 0; i < size; i++) {
                            pedestals.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                        }
                        Ingredient sourceGem = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                        ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                        int manaCost = buf.readVarInt();
                        return new AwakeningRecipe(pedestals, sourceGem, result, manaCost);
                    },
                    (buf, recipe) -> {
                        buf.writeVarInt(recipe.pedestalIngredients.size());
                        for (Ingredient ing : recipe.pedestalIngredients) {
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                        }
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.sourceGem);
                        ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                        buf.writeVarInt(recipe.manaCost);
                    }
            );
        }

        @Override
        public MapCodec<AwakeningRecipe> codec() {
            return AwakeningRecipe.CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AwakeningRecipe> streamCodec() {
            return makeStreamCodec();
        }
    }
}
