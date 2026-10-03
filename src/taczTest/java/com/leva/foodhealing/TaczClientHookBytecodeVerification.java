package com.leva.foodhealing;

import com.mojang.logging.LogUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import java.io.IOException;
import java.util.Set;

/** Static exact-artifact audit only; does not launch/load a Minecraft client. */
final class TaczClientHookBytecodeVerification {
    static void verify() throws IOException {
        ClassNode node = new ClassNode();
        try (var stream = TaczClientHookBytecodeVerification.class.getClassLoader()
                .getResourceAsStream("com/tacz/guns/client/gameplay/LocalPlayerBolt.class")) {
            if (stream == null) throw new AssertionError("exact client bolt class resource missing");
            new ClassReader(stream).accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
        int chamber = 0, magazine = 0, inventory = 0;
        for (var method : node.methods) {
            if (!Set.of("lambda$bolt$0", "tickAutoBolt").contains(method.name)) continue;
            for (var instruction : method.instructions) {
                if (!(instruction instanceof MethodInsnNode call) || !call.owner.equals("com/tacz/guns/api/item/IGun")) continue;
                if (call.name.equals("hasBulletInBarrel")) chamber++;
                if (call.name.equals("getCurrentAmmoCount")) magazine++;
                if (call.name.equals("hasInventoryAmmo")) inventory++;
            }
        }
        if (chamber != 2 || magazine != 1 || inventory != 1) throw new AssertionError("client hook call sites differ from audited exact version");
        LogUtils.getLogger().info("FOODHEALING_TACZ_CLIENT_BYTECODE_PASS chamberSites=2 magazineSites=1 inventorySites=1 CLIENT_RUNTIME=NOT_RUN");
    }
}
