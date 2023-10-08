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
package aztech.modern_industrialization.compat.tis3d;

import aztech.modern_industrialization.machines.blockentities.ElectricCraftingMachineBlockEntity;
import java.util.Objects;
import java.util.Optional;
import li.cil.tis3d.api.serial.SerialInterface;
import li.cil.tis3d.api.serial.SerialInterfaceProvider;
import li.cil.tis3d.api.serial.SerialProtocolDocumentationReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class ElectricCraftingMachineSerialInterfaceProvider implements SerialInterfaceProvider {
    private static final Component DOCUMENTATION_TITLE = Component
            .translatable("text.modern_industrialization.electric_machines");
    private static final String DOCUMENTATION_LINK = "electric_machine.md";
    private static final SerialProtocolDocumentationReference DOCUMENTATION_REFERENCE = new SerialProtocolDocumentationReference(
            DOCUMENTATION_TITLE, DOCUMENTATION_LINK);

    @Override
    public boolean matches(final Level level, final BlockPos position, final Direction side) {
        return level.getBlockEntity(position) instanceof ElectricCraftingMachineBlockEntity;
    }

    @Override
    public Optional<SerialInterface> getInterface(final Level level, final BlockPos position, final Direction face) {
        final ElectricCraftingMachineBlockEntity machine = Objects
                .requireNonNull((ElectricCraftingMachineBlockEntity) level.getBlockEntity(position));
        return Optional.of(new ElectricCraftingMachineSerialInterface(machine));
    }

    @Override
    public Optional<SerialProtocolDocumentationReference> getDocumentationReference() {
        return Optional.of(DOCUMENTATION_REFERENCE);
    }

    @Override
    public boolean stillValid(final Level level, final BlockPos position, final Direction side,
            final SerialInterface serialInterface) {
        return serialInterface instanceof ElectricCraftingMachineSerialInterface;
    }

    private static final class ElectricCraftingMachineSerialInterface implements SerialInterface {
        private static final String TAG_MODE = "mode";

        private enum Mode {
            PercentageEnergy,
            PercentageProgress,
            PercentageOverclock,
            OverclockTicks,
            MaxOverclockTicks
        }

        private final ElectricCraftingMachineBlockEntity machine;
        private Mode mode;

        public ElectricCraftingMachineSerialInterface(final ElectricCraftingMachineBlockEntity machine) {
            this.machine = machine;
            this.mode = Mode.PercentageEnergy;
        }

        @Override
        public boolean canWrite() {
            return true;
        }

        @Override
        public void write(final short value) {
            switch (value) {
            case 0: {
                mode = Mode.PercentageEnergy;
                break;
            }
            case 1: {
                mode = Mode.PercentageProgress;
                break;
            }
            case 2: {
                mode = Mode.PercentageOverclock;
                break;
            }
            case 3: {
                mode = Mode.OverclockTicks;
                break;
            }
            case 4: {
                mode = Mode.MaxOverclockTicks;
                break;
            }
            }
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public short peek() {
            switch (mode) {
            case MaxOverclockTicks: {
                final int maximum = machine.getCrafterComponent().getMaxEfficiencyTicks();
                if (maximum > Short.MAX_VALUE) {
                    return Short.MAX_VALUE;
                } else {
                    return (short) maximum;
                }
            }
            case OverclockTicks: {
                final int value = machine.getCrafterComponent().getEfficiencyTicks();
                if (value > Short.MAX_VALUE) {
                    return Short.MAX_VALUE;
                } else {
                    return (short) value;
                }
            }
            case PercentageEnergy: {
                final long value = machine.getEnergyComponent().getEu();
                final long capacity = machine.getEnergyComponent().getCapacity();
                return (short) (value * 100 / capacity);
            }
            case PercentageOverclock: {
                final int value = machine.getCrafterComponent().getEfficiencyTicks();
                final int maximum = machine.getCrafterComponent().getMaxEfficiencyTicks();
                return (short) (value * 100 / maximum);
            }
            case PercentageProgress: {
                return (short) (machine.getCrafterComponent().getProgress() * 100);
            }
            }
            return 0;
        }

        @Override
        public void skip() {
        }

        @Override
        public void reset() {
            mode = Mode.PercentageEnergy;
        }

        @Override
        public void load(final CompoundTag tag) {
            mode = ElectricCraftingMachineSerialInterface.Mode.class.getEnumConstants()[tag.getByte(TAG_MODE)];
        }

        @Override
        public void save(final CompoundTag tag) {
            tag.putByte(TAG_MODE, (byte) mode.ordinal());
        }
    }
}
