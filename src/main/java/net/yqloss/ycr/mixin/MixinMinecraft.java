package net.yqloss.ycr.mixin;

import net.minecraft.client.Minecraft;
import net.yqloss.ycr.callback.CallbackMixinMinecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
  @Inject(method = "runTick", at = @At("HEAD"))
  private void runTickHead(boolean renderLevel, CallbackInfo ci) {
    CallbackMixinMinecraft.INSTANCE.runTickHead(renderLevel, ci);
  }
}
