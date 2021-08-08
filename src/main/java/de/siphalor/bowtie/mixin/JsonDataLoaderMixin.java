package de.siphalor.bowtie.mixin;

import com.google.gson.JsonElement;
import de.siphalor.bowtie.BowTie;
import de.siphalor.tweed4.data.gson.GsonSerializer;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(JsonDataLoader.class)
public class JsonDataLoaderMixin {
	@Shadow @Final private String dataType;

	@Inject(method = "prepare", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void loadAdditional(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<Map<Identifier, JsonElement>> cir, Map<Identifier, JsonElement> map) {
		BowTie.loadResourcesAsData(resourceManager,dataType, (identifier, dataValue) ->
			map.put(identifier, dataValue.convert(GsonSerializer.INSTANCE).getRaw())
		);
	}
}
