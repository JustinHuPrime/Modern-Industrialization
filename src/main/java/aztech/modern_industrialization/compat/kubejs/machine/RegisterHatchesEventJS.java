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
package aztech.modern_industrialization.compat.kubejs.machine;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.init.MultiblockHatches;
import aztech.modern_industrialization.machines.models.MachineCasings;
import dev.latvian.mods.kubejs.event.KubeEvent;

public class RegisterHatchesEventJS implements KubeEvent {
    public void energy(String cableTier) {
        MultiblockHatches.registerEnergyHatches(CableTier.getTier(cableTier));
    }

    public void fluid(String englishPrefix, String prefix, String casing, int bucketCapacity) {
        MultiblockHatches.registerFluidHatches(englishPrefix, prefix, MachineCasings.get(casing), bucketCapacity);
    }

    public void item(String englishPrefix, String prefix, String casing, int rows, int columns, int xStart, int yStart) {
        MultiblockHatches.registerItemHatches(englishPrefix, prefix, MachineCasings.get(casing), rows, columns, xStart, yStart);
    }
}
