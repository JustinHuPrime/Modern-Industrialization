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
package aztech.modern_industrialization.compat.viewer.impl.emi;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.compat.viewer.abstraction.ViewerCategory;
import aztech.modern_industrialization.compat.viewer.abstraction.ViewerPageManager;
import aztech.modern_industrialization.machines.gui.MachineScreen;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.fluid.FluidVariant;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.item.ItemVariant;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.storage.TransferVariant;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

class ViewerCategoryEmi<D> extends EmiRecipeCategory {
    public final ViewerCategory<D> wrapped;

    public ViewerCategoryEmi(ViewerCategory<D> category) {
        super(category.id, category.icon instanceof ViewerCategory.Icon.Stack stack ? EmiStack.of(stack.stack()) : new EmiRenderable() {
            @Override
            public void render(GuiGraphics guiGraphics, int x, int y, float delta) {
                var texture = (ViewerCategory.Icon.Texture) category.icon;
                guiGraphics.blit(texture.loc(), x - 1, y - 1, 0, texture.u(), texture.v(), 18, 18, 256, 256);
            }
        });

        this.wrapped = category;
    }

    @Override
    public Component getName() {
        return wrapped.title;
    }

    private void processLayout(D recipe, List<IngredientBuilder> inputs, List<IngredientBuilder> outputs) {
        wrapped.buildLayout(recipe, new ViewerCategory.LayoutBuilder() {
            @Override
            public ViewerCategory.SlotBuilder inputSlot(int x, int y) {
                var ing = new IngredientBuilder(x, y);
                inputs.add(ing);
                return ing;
            }

            @Override
            public ViewerCategory.SlotBuilder outputSlot(int x, int y) {
                var ing = new IngredientBuilder(x, y);
                outputs.add(ing);
                return ing;
            }

            @Override
            public void invisibleInput(ItemStack item) {
                var ing = new IngredientBuilder(0, 0);
                ing.item(item);
                ing.isVisible = false;
                inputs.add(ing);
            }

            @Override
            public void invisibleOutput(ItemStack item) {
                var ing = new IngredientBuilder(0, 0);
                ing.item(item);
                ing.isVisible = false;
                outputs.add(ing);
            }

            @Override
            public void scrollableSlots(int cols, int rows, List<ItemStack> stacks) {
                stacks.forEach(this::invisibleInput);
            }
        });
    }

    public void registerRecipes(EmiRegistry registry) {
        wrapped.buildRecipes(registry.getRecipeManager(), Minecraft.getInstance().level.registryAccess(), r -> registry.addRecipe(makeRecipe(r)));
    }

    private ViewerRecipe makeRecipe(D recipe) {
        List<IngredientBuilder> inputs = new ArrayList<>();
        List<IngredientBuilder> outputs = new ArrayList<>();

        processLayout(recipe, inputs, outputs);

        // If there is an invisible output, the recipe will be excluded from the tree.
        boolean excludeFromTree = outputs.stream().anyMatch(b -> !b.isVisible);

        // Remove empty ingredients that only exist for rendering reasons (e.g. empty fluid slots)
        inputs.removeIf(b -> b.ing.isEmpty());
        outputs.removeIf(b -> b.ing.isEmpty());

        // Separate catalysts
        List<IngredientBuilder> catalysts = inputs.stream().filter(b -> b.isCatalyst).toList();
        inputs.removeIf(b -> b.isCatalyst);

        return new ViewerRecipe(recipe, convertInputs(inputs), convertInputs(catalysts), convertOutputs(outputs), excludeFromTree);
    }

    private static List<EmiIngredient> convertInputs(List<IngredientBuilder> list) {
        return list.stream().map(b -> b.ing).toList();
    }

    private static List<EmiStack> convertOutputs(List<IngredientBuilder> list) {
        // A bit cursed... but we know that there's always one stack per output ;)
        return list.stream().flatMap(b -> b.ing.getEmiStacks().stream()).toList();
    }

    private static class IngredientBuilder implements ViewerCategory.SlotBuilder {
        private final int x, y;
        private EmiIngredient ing = EmiStack.EMPTY;
        private boolean isFluid = false;
        private boolean hasBackground = true;
        private boolean isVisible = true;
        private boolean isCatalyst = false;

        IngredientBuilder(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public ViewerCategory.SlotBuilder variant(TransferVariant<?> variant) {
            if (variant instanceof ItemVariant item) {
                item(item.toStack());
            } else if (variant instanceof FluidVariant fluid) {
                isFluid = true;
                hasBackground = false;
                if (!fluid.isBlank()) {
                    ing = EmiStack.of(fluid.getFluid(), fluid.getComponentsPatch());
                }
            } else {
                throw new IllegalArgumentException("Unknown variant type: " + variant.getClass());
            }
            return this;
        }

        @Override
        public ViewerCategory.SlotBuilder fluid(FluidVariant fluid, long amount, float probability) {
            isFluid = true;
            hasBackground = false;
            ing = EmiStack.of(fluid.getFluid(), fluid.getComponentsPatch(), amount);
            processProbability(probability);
            return this;
        }

        @Override
        public ViewerCategory.SlotBuilder fluid(FluidIngredient ingredient, long amount, float probability) {
            isFluid = true;
            hasBackground = false;
            ing = EmiIngredient.of(
                    Stream.of(ingredient.getStacks())
                            .map(fs -> EmiStack.of(fs.getFluid(), fs.getComponentsPatch()))
                            .toList(),
                    amount);
            processProbability(probability);
            return this;
        }

        private void processProbability(float probability) {
            if (probability == 0) {
                markCatalyst();
            } else {
                ing.setChance(probability);
            }
        }

        @Override
        public ViewerCategory.SlotBuilder item(ItemStack stack, float probability) {
            ing = EmiStack.of(stack);
            processProbability(probability);
            return this;
        }

        @Override
        public ViewerCategory.SlotBuilder ingredient(Ingredient ingredient, long amount, float probability) {
            ing = EmiIngredient.of(ingredient, amount);
            processProbability(probability);
            return this;
        }

        @Override
        public ViewerCategory.SlotBuilder removeBackground() {
            hasBackground = false;
            return this;
        }

        @Override
        public ViewerCategory.SlotBuilder markCatalyst() {
            isCatalyst = true;
            return this;
        }
    }

    public class ViewerRecipe implements EmiRecipe {
        final D recipe;
        private final List<EmiIngredient> inputs;
        private final List<EmiIngredient> catalysts;
        private final List<EmiStack> outputs;
        private final boolean excludeFromTree;

        public ViewerRecipe(D recipe, List<EmiIngredient> inputs, List<EmiIngredient> catalysts, List<EmiStack> outputs, boolean excludeFromTree) {
            this.recipe = recipe;
            this.inputs = inputs;
            this.catalysts = catalysts;
            this.outputs = outputs;
            this.excludeFromTree = excludeFromTree;
        }

        @Override
        public ViewerCategoryEmi<D> getCategory() {
            return ViewerCategoryEmi.this;
        }

        @Override
        public ResourceLocation getId() {
            return wrapped.getRecipeId(recipe);
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return inputs;
        }

        @Override
        public List<EmiIngredient> getCatalysts() {
            return catalysts;
        }

        @Override
        public List<EmiStack> getOutputs() {
            return outputs;
        }

        @Override
        public int getDisplayWidth() {
            return wrapped.width - 8;
        }

        @Override
        public int getDisplayHeight() {
            return wrapped.height - 8;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            // Extra widgets
            wrapped.buildWidgets(recipe, new ViewerCategory.WidgetList() {
                @Override
                public void text(Component text, float x, float y, ViewerCategory.TextAlign align, boolean shadow, boolean overrideColor,
                        @Nullable Component tooltip) {
                    var font = Minecraft.getInstance().font;
                    var width = font.width(text);
                    var alignedX = (int) switch (align) {
                    case LEFT -> x;
                    case CENTER -> x - width / 2f;
                    case RIGHT -> x - width;
                    };
                    widgets.addText(text.getVisualOrderText(), alignedX - 4, (int) y - 4, overrideColor ? 0xFF404040 : -1, shadow);
                    if (tooltip != null) {
                        tooltip(alignedX, (int) y, width, font.lineHeight, List.of(tooltip));
                    }
                }

                @Override
                public void arrow(int x, int y) {
                    texture(MI.id("textures/gui/jei/arrow.png"), x, y, 0, 17, 24, 17);
                }

                @Override
                public void texture(ResourceLocation loc, int x, int y, int u, int v, int width, int height) {
                    widgets.addTexture(loc, x - 4, y - 4, width, height, u, v);
                }

                @Override
                public void drawable(Consumer<GuiGraphics> widget) {
                    widgets.addDrawable(-4, -4, 0, 0, (matrices, mouseX, mouseY, delta) -> {
                        widget.accept(matrices);
                    });
                }

                @Override
                public void tooltip(int x, int y, int w, int h, List<Component> tooltip) {
                    var mapped = tooltip.stream().map(c -> ClientTooltipComponent.create(c.getVisualOrderText())).toList();
                    widgets.addDrawable(x - 4, y - 4, w, h, (matrices, mouseX, mouseY, delta) -> {
                    }).tooltip((mouseX, mouseY) -> mapped);
                }

                @Override
                public void scrollableSlots(int cols, int rows, List<ItemStack> stacks) {
                    int offsetX = (widgets.getWidth() / 2) - ((cols * 18) / 2);
                    int offsetY = 20;
                    int pageHeight = (widgets.getHeight() - 21) / 18;
                    int pageSize = cols * rows;
                    ViewerPageManager pages = new ViewerPageManager(stacks, pageSize);
                    if (pageSize < stacks.size()) {
                        widgets.addButton(2, 2, 12, 12, 0, 0, () -> true, (mx, my, button) -> {
                            pages.scroll(-1);
                        });
                        widgets.addButton(widgets.getWidth() - 14, 2, 12, 12, 12, 0, () -> true, (mx, my, button) -> {
                            pages.scroll(1);
                        });
                    }
                    int index = 0;
                    for (; index < stacks.size() && index / cols <= pageHeight; index++) {
                        final int finalIndex = index;
                        widgets.add(new SlotWidget(EmiStack.EMPTY, index % cols * 18 + offsetX, index / cols * 18 + offsetY) {
                            @Override
                            public EmiIngredient getStack() {
                                return EmiStack.of(pages.getStack(finalIndex));
                            }

                            @Override
                            public void drawStack(GuiGraphics draw, int mouseX, int mouseY, float delta) {
                                var stack = pages.getStack(finalIndex);

                                Bounds bounds = getBounds();
                                int xOff = (bounds.width() - 16) / 2;
                                int yOff = (bounds.height() - 16) / 2;

                                // Draw count separately to handle amounts >= 100 reasonably
                                int stackX = bounds.x() + xOff;
                                int stackY = bounds.y() + yOff;
                                EmiStack.of(stack, 1).render(draw, stackX, stackY, delta);
                                var amount = Component.literal(String.valueOf(stack.getCount()));
                                // Could use 1.0 if the count is < 100, but this looks more uniform
                                float scale = 0.8f;

                                draw.pose().pushPose();
                                draw.pose().translate(0, 0, 200);
                                int tx = stackX + 17 - Math.min(14, Math.round(Minecraft.getInstance().font.width(amount) * scale)) - 1;
                                draw.pose().translate(tx, stackY + 9, 0);
                                draw.pose().scale(scale, scale, 1);
                                draw.drawString(Minecraft.getInstance().font, amount, 0, 0, -1, true);
                                draw.pose().popPose();
                            }
                        });
                    }
                    for (; index < pageSize; index++) {
                        widgets.addSlot(EmiStack.EMPTY, index % cols * 18 + offsetX, index / cols * 18 + offsetY);
                    }
                }
            });

            // Inputs and outputs
            {
                List<IngredientBuilder> inputs = new ArrayList<>();
                List<IngredientBuilder> outputs = new ArrayList<>();

                processLayout(recipe, inputs, outputs);

                for (var input : inputs) {
                    processIngredient(this, widgets, input, true);
                }

                for (var output : outputs) {
                    processIngredient(this, widgets, output, false);
                }
            }
        }

        @Override
        public boolean supportsRecipeTree() {
            return !excludeFromTree && EmiRecipe.super.supportsRecipeTree();
        }
    }

    private static void createFluidSlotBackground(WidgetHolder widgets, int x, int y) {
        widgets.addDrawable(0, 0, 0, 0, (guiGraphics, mouseX, mouseY, delta) -> {
            guiGraphics.blit(MachineScreen.SLOT_ATLAS, x - 1 - 4, y - 1 - 4, 18, 0, 18, 18);
        });
    }

    private static void processIngredient(EmiRecipe recipe, WidgetHolder widgets, IngredientBuilder ing, boolean isInput) {
        if (!ing.isVisible) {
            return;
        }

        if (ing.isFluid) {
            createFluidSlotBackground(widgets, ing.x, ing.y);
        }
        var slot = widgets.addSlot(ing.ing, ing.x - 1 - 4, ing.y - 1 - 4);
        if (!isInput) {
            slot.recipeContext(recipe);
        }
        if (!ing.hasBackground) {
            slot.drawBack(false);
        }
        if (ing.isCatalyst) {
            slot.catalyst(true);
        }
    }
}
