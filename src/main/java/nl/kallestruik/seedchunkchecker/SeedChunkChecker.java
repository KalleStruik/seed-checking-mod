package nl.kallestruik.seedchunkchecker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SeedChunkChecker implements ModInitializer {
    // You should really keep these two equal.
    private static final int X_RANGE = 100;
    private static final int Z_RANGE = 100;

    public static long seed;
    public static long x;
    public static long z;
    public static MinecraftDedicatedServer server = null;
    public static ServerWorld world;

    @Override
    public void onInitialize() {
        Thread thread = new Thread(new WhyDoINeedAThreadForThis());
        thread.start();
    }

    private static JsonObject checkWorld(ServerWorld world, long xpos, long zpos) {
        System.out.println(world.getSeed());
        int wool = 0;
        int cactus = 0;
        for (long x = xpos-X_RANGE; x <= xpos+X_RANGE; x++) {
            for (long z = zpos-Z_RANGE; z <= zpos+Z_RANGE; z++) {
                for (int y = 50; y <= 80; y++) {
                    String block = Registry.BLOCK.getId(world.getBlockState(new BlockPos(x, y, z)).getBlock()).toString();
                    if (block.equals("minecraft:white_wool")){
                        wool++;
                    }
                    if(block.equals("minecraft:cactus")){
                        cactus++;
                    }
                }
            }
        }
        JsonObject obj = new JsonObject();
        obj.add("wool", new JsonPrimitive(wool));
        obj.add("cactus", new JsonPrimitive(cactus));
        if(cactus>=32&&wool>=70){
            obj.add("good", new JsonPrimitive(true));
        } else{
            obj.add("good", new JsonPrimitive(false));
        }
        return obj;
    }

    private static class WhyDoINeedAThreadForThis implements Runnable {

        @Override
        public void run() {
            while (server == null || (world = server.getWorld(World.OVERWORLD)) == null) {
                try {
                    //noinspection BusyWait - cry about it
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            File seedDir = new File(String.valueOf(seed));
            seedDir.mkdirs();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject data = checkWorld(world, x, z);
            try (JsonWriter writer = gson.newJsonWriter(new FileWriter(new File(seedDir, "data.json")))) {
                gson.toJson(data, writer);
                writer.flush();
            } catch (IOException ignored) {}

            server.stop(true);
        }
    }
}
