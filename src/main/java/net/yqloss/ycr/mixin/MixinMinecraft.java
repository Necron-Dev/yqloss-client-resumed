package net.yqloss.ycr.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.yqloss.ycr.callback.CallbackMixinMinecraft;
import net.yqloss.ycr.gui.system.ScreenProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

  @Inject(method = "runTick", at = @At("HEAD"))
  private void onTickHead(CallbackInfo ci) {
    CallbackMixinMinecraft.INSTANCE.runTickHead(ci);
  }

  @Redirect(method = "resizeDisplay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;resize(Lnet/minecraft/client/Minecraft;II)V"))
  private void resizeDisplayRedirectResize(Screen instance, Minecraft minecraft, int width, int height) {
    ScreenProxy.INSTANCE.resize(instance, minecraft, width, height);
  }
}
