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
package aztech.modern_industrialization.compat.viewer.abstraction;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public class ViewerPageManager {
    public final List<ItemStack> stacks;
    public final int pageSize;

    public int currentPage;

    public ViewerPageManager(List<ItemStack> stacks, int pageSize) {
        this.stacks = stacks;
        this.pageSize = pageSize;
    }

    public int totalPages() {
        return (stacks.size() - 1) / pageSize + 1;
    }

    public void scroll(int delta) {
        currentPage += delta;
        int totalPages = totalPages();
        if (currentPage < 0) {
            currentPage = totalPages - 1;
        }
        if (currentPage >= totalPages) {
            currentPage = 0;
        }
    }

    public ItemStack getStack(int index) {
        index += pageSize * currentPage;
        if (index < stacks.size()) {
            return stacks.get(index);
        }
        return ItemStack.EMPTY;
    }
}
