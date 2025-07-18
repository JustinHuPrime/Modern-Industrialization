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
package aztech.modern_industrialization.materials.part;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.items.PortableStorageUnit;

public class BatteryPart implements PartKeyProvider {

    @Override
    public PartKey key() {
        return new PartKey("battery");
    }

    public PartTemplate of(long batteryCapacity) {
        return new PartTemplate("Battery", "battery").withRegister(
                (partContext, part, itemPath, itemId, itemTag, englishName) -> {
                    var item = PartTemplate.createSimpleItem(englishName, itemPath, partContext, part);
                    item.withItemRegistrationEvent(i -> {
                        PortableStorageUnit.CAPACITY_PER_BATTERY.put(i, batteryCapacity);
                    });
                });
    }

    public PartTemplate of(CableTier tier) {
        // A factor of 60*20*8 = 9600 would be one minute at the max tier power. 10000 is nicer in the tooltips.
        return of(10000 * tier.eu);
    }
}
