package nl.kallestruik.seedchunkchecker.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import nl.kallestruik.seedchunkchecker.SeedChunkChecker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "startServer", at = @At("RETURN"), cancellable = true)
    private static void onServerStart(CallbackInfoReturnable<MinecraftDedicatedServer> cir) {
        SeedChunkChecker.server = cir.getReturnValue();
    }
}
