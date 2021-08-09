/*
 * Copyright 2021 Siphalor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.siphalor.bowtie.mixin;

import de.siphalor.bowtie.BowTie;
import de.siphalor.tweed4.data.DataSerializer;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {
	@Shadow @Final private ResourceType type;

	@Shadow @Final protected List<ResourcePack> packList;

	@Inject(
			method = "getResource",
			at = @At(
					value = "JUMP",
					opcode = Opcodes.GOTO
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	public void tryGetAdditionalResourcesForGetResource(
			Identifier identifier,
			CallbackInfoReturnable<Resource> cir,
			ResourcePack metadataRP, Identifier metadataId, int i
	) {
		if (!identifier.getPath().endsWith(".json")) {
			return;
		}

		ResourcePack resourcePack = packList.get(i + 1);
		String basePath = StringUtils.substring(identifier.getPath(), 0, -"json".length());
		Map<String, DataSerializer<?, ?, ?>> serializerMap = BowTie.getSerializerMapNonJson();
		for (Map.Entry<String, DataSerializer<?, ?, ?>> entry : serializerMap.entrySet()) {
			Identifier currentId = new Identifier(identifier.getNamespace(), basePath + entry.getKey());
			if (resourcePack.contains(type, currentId)) {
				try {
					InputStream inputStream = resourcePack.open(type, currentId);
					cir.setReturnValue(
							BowTie.asJsonResource(currentId, inputStream, entry.getValue(), resourcePack.getName())
					);
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Inject(
			method = "getAllResources",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/resource/ResourcePack;contains(Lnet/minecraft/resource/ResourceType;Lnet/minecraft/util/Identifier;)Z"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void tryGetAdditionalResourcesForGetAllResources(
			Identifier id,
			CallbackInfoReturnable<List<Resource>> cir,
			List<Resource> resources, Identifier metadataId,
			Iterator<ResourcePack> packIterator, ResourcePack resourcePack
	) {
		if (!id.getPath().endsWith(".json")) {
			return;
		}
		String basePath = StringUtils.substring(id.getPath(), 0, -"json".length());

		for (Map.Entry<String, DataSerializer<?, ?, ?>> entry : BowTie.getSerializerMapNonJson().entrySet()) {
			String path = basePath + entry.getKey();
			if (resourcePack.contains(type, new Identifier(id.getNamespace(), path))) {
				try {
					resources.add(BowTie.asJsonResource(
							id, resourcePack.open(type, new Identifier(id.getNamespace(), path)),
							entry.getValue(), resourcePack.getName()
					));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
