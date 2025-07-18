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
package aztech.modern_industrialization.compat.ftbquests;

import aztech.modern_industrialization.config.MIStartupConfig;
import java.util.UUID;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

public interface FTBQuestsFacade {
    FTBQuestsFacade INSTANCE = getInstance();

    private static FTBQuestsFacade getInstance() {
        if (ModList.get().isLoaded("ftbquests") && MIStartupConfig.INSTANCE.ftbQuestsIntegration.getAsBoolean()) {
            try {
                return Class.forName("aztech.modern_industrialization.compat.ftbquests.FTBQuestsFacadeImpl")
                        .asSubclass(FTBQuestsFacade.class).getConstructor().newInstance();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        return (uuid, item, amount) -> {
        };
    }

    void addCompleted(UUID uuid, Item item, long amount);
}
