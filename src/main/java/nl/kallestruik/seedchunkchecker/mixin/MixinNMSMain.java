package nl.kallestruik.seedchunkchecker.mixin;

import net.minecraft.server.Main;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import nl.kallestruik.seedchunkchecker.SeedChunkChecker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.struct.CallbackInjectionInfo;

@Mixin(Main.class)
public class MixinNMSMain {

    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] args, CallbackInfo ci) {
        String seed = args[0];
        SeedChunkChecker.x = Long.parseLong(args[1]);
        SeedChunkChecker.z = Long.parseLong(args[2]);

        SeedChunkChecker.seed = Long.parseLong(seed);

        args[0] = "--nogui";
        args[1] = "--world";
        args[2] = seed;
    }
}
