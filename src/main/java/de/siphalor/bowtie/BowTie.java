package de.siphalor.bowtie;

import de.siphalor.tweed4.data.DataSerializer;
import de.siphalor.tweed4.data.DataValue;
import de.siphalor.tweed4.data.gson.GsonSerializer;
import de.siphalor.tweed4.data.gson.GsonValue;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class BowTie implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "bowtie";
    public static final String MOD_NAME = "Bow Tie";
    private static Collection<DataSerializer<?, ?, ?>> serializers;
    private static Map<String, DataSerializer<?, ?, ?>> serializerMap;
    private static Map<String, DataSerializer<?, ?, ?>> serializerMapNonJson;

    @Override
    public void onInitialize() {
        log(Level.INFO, "Tying ties");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

    public static Collection<DataSerializer<?, ?, ?>> getSerializers() {
        if (serializers != null) {
            return serializers;
        }
        //noinspection unchecked
        serializers = (Collection<DataSerializer<?, ?, ?>>) (Object) FabricLoader.getInstance().getEntrypoints("tweed4:serializer", DataSerializer.class);
        return serializers;
    }

    public static Map<String, DataSerializer<?, ?, ?>> getSerializerMap() {
        if (serializerMap != null) {
            return serializerMap;
        }

        serializerMap = new HashMap<>();
        serializerMapNonJson = new HashMap<>();
        for (DataSerializer<?, ?, ?> serializer : getSerializers()) {
            String fileExtension = serializer.getFileExtension();
            serializerMap.put(fileExtension, serializer);
            if (!"json".equals(fileExtension)) {
                serializerMapNonJson.put(fileExtension, serializer);
            }
        }
        return serializerMap;
    }

    public static Map<String, DataSerializer<?, ?, ?>> getSerializerMapNonJson() {
        if (serializerMapNonJson != null) {
            return serializerMapNonJson;
        }

        getSerializerMap();
        return serializerMapNonJson;
    }

    public static void loadResourcesAsData(ResourceManager resourceManager, String basePath, BiConsumer<Identifier, DataValue<?, ?, ?>> consumer) {
        Collection<Identifier> resourceIds = resourceManager.findResources(basePath, path -> {
            String extension = StringUtils.substringAfterLast(path, ".");
            return serializerMapNonJson.containsKey(extension);
        });

        for (Identifier resourceId : resourceIds) {
            DataSerializer<?, ?, ?> serializer = serializerMap.get(StringUtils.substringAfterLast(resourceId.getPath(), "."));
            Identifier identifier = new Identifier(resourceId.getNamespace(), StringUtils.substringBeforeLast(resourceId.getPath(), "."));

            try {
                Resource resource = resourceManager.getResource(resourceId);
                DataValue<?, ?, ?> dataValue = serializer.readValue(resource.getInputStream());
                consumer.accept(identifier, dataValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Resource asJsonResource(Resource base, DataSerializer<?, ?, ?> baseSerializer) {
        return asJsonResource(base.getId(), base.getInputStream(), baseSerializer, base.getResourcePackName());
    }

    public static Resource asJsonResource(
            Identifier id, InputStream inputStream, DataSerializer<?, ?, ?> baseSerializer, String packName
    ) {
        DataValue<?, ?, ?> dataValue = baseSerializer.readValue(inputStream);
        GsonValue gsonValue = dataValue.convert(GsonSerializer.INSTANCE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GsonSerializer.INSTANCE.writeValue(outputStream, gsonValue);
        return new ResourceImpl(packName, id, new ByteArrayInputStream(outputStream.toByteArray()), null);
    }
}
