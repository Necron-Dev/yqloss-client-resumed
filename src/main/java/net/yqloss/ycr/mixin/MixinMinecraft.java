package net.yqloss.ycr.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.yqloss.ycr.callback.CallbackMixinMinecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
  @Inject(method = "tick", at = @At("HEAD"))
  private void tickHead(CallbackInfo ci) {
    CallbackMixinMinecraft.INSTANCE.tickHead(ci);
  }

  @Inject(method = "setScreen", at = @At("RETURN"))
  private void setScreenReturn(Screen guiScreen, CallbackInfo ci) {
    CallbackMixinMinecraft.INSTANCE.setScreenReturn(guiScreen, ci);
  }
}
