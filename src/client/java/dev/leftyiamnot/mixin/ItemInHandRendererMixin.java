package dev.leftyiamnot.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Decouples the local first-person arm from the player's real main-arm setting.
 *
 * <p>The intercepted calls only occur in the first-person item renderer. Returning
 * {@link HumanoidArm#RIGHT} here makes the main hand render on the right and lets
 * vanilla derive the off hand as its opposite, without changing player settings,
 * networking, or third-person rendering.</p>
 */
@Mixin(ItemInHandRenderer.class)
abstract class ItemInHandRendererMixin {
	@ModifyExpressionValue(
			method =
					"renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FF" +
					"Lnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;F" +
					"Lcom/mojang/blaze3d/vertex/PoseStack;" +
					"Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/player/AbstractClientPlayer;getMainArm()Lnet/minecraft/world/entity/HumanoidArm;"
			),
			require = 2,
			expect = 2,
			allow = 2
	)
	private HumanoidArm leftyIAmNot$useRightArmInFirstPerson(HumanoidArm original) {
		return HumanoidArm.RIGHT;
	}
}
