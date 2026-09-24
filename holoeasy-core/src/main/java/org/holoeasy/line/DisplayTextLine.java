package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DisplayTextLine extends Line<String> {

    private int lineWidth = 200;
    private int backgroundColor = 0x40000000;
    private byte textOpacity = -1;
    /** Default CENTER — FIXED (0) is often invisible depending on view angle. */
    private byte billboard = 3;

    public DisplayTextLine(Hologram hologram, Function<Player, String> valueSupplier) {
        super(hologram, EntityTypes.TEXT_DISPLAY, valueSupplier);
    }

    @Override
    public @NotNull Type getType() {
        return Type.DISPLAY_TEXT_LINE;
    }


    @Override
    public void show(@NotNull Player player) {
        spawn(player);
        this.update(player);
    }

    @Override
    public void hide(@NotNull Player player) {
        destroy(player);
    }

    @Override
    public void update(@NotNull Player player) {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        if (version.isOlderThan(ServerVersion.V_1_19_4)) {
            throw new RuntimeException("DisplayTextLine is available since 1.19.4");
        }

        // Official entity metadata (minecraft.wiki / PrismarineJS 26.1 entities.json):
        // Display starts at index 8; since 1.20.2 billboard=15, text=23, line_width=24,
        // background_color=25, text_opacity=26, style_flags=27.
        // Before 1.20.2 those are -1 (billboard=14, text=22, ...).
        boolean pre1202 = version.isOlderThan(ServerVersion.V_1_20_2);
        int interpolationDelayIndex = 8;
        int scaleIndex = pre1202 ? 11 : 12;
        int billboardIndex = pre1202 ? 14 : 15;
        int textIndex = pre1202 ? 22 : 23;
        int lineWidthIndex = textIndex + 1;
        int backgroundIndex = textIndex + 2;
        int opacityIndex = textIndex + 3;
        int styleFlagsIndex = textIndex + 4;

        Component textComponent = LegacyComponentSerializer.legacySection().deserialize(getValue(player));

        List<EntityData<?>> entityData = new ArrayList<>();
        // Apply transform immediately (same approach as HologramLib / EntityLib users).
        entityData.add(new EntityData<>(interpolationDelayIndex, EntityDataTypes.INT, -1));
        entityData.add(new EntityData<>(scaleIndex, EntityDataTypes.VECTOR3F, new Vector3f(1f, 1f, 1f)));
        entityData.add(new EntityData<>(billboardIndex, EntityDataTypes.BYTE, billboard));
        entityData.add(new EntityData<>(textIndex, EntityDataTypes.ADV_COMPONENT, textComponent));
        entityData.add(new EntityData<>(lineWidthIndex, EntityDataTypes.INT, lineWidth));
        entityData.add(new EntityData<>(backgroundIndex, EntityDataTypes.INT, backgroundColor));
        entityData.add(new EntityData<>(opacityIndex, EntityDataTypes.BYTE, textOpacity));
        entityData.add(new EntityData<>(styleFlagsIndex, EntityDataTypes.BYTE, (byte) 0));

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityID, entityData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    // Builder

    public DisplayTextLine yOffset(double yOffset) {
        super.setYOffset(yOffset);
        return this;
    }

    public DisplayTextLine lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public DisplayTextLine backgroundColor(@NotNull Color backgroundColor) {
        this.backgroundColor = backgroundColor.asRGB();
        return this;
    }

    public DisplayTextLine backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public DisplayTextLine textOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }

    /**
     * Set billboard constraint (rotation behavior)
     * @param billboard 0=FIXED, 1=VERTICAL, 2=HORIZONTAL, 3=CENTER
     */
    public DisplayTextLine billboard(byte billboard) {
        this.billboard = billboard;
        return this;
    }
}
