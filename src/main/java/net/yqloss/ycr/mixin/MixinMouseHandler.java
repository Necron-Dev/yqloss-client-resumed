package net.yqloss.ycr.mixin;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.yqloss.ycr.gui.system.ScreenProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
public abstract class MixinMouseHandler {
  @Redirect(method = "onButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"))
  private boolean onButtonRedirectMouseClicked(Screen instance, MouseButtonEvent mouseButtonEvent, boolean isDoubleClick) {
    return ScreenProxy.INSTANCE.mouseClicked(instance, mouseButtonEvent, isDoubleClick);
  }

  @Redirect(method = "onButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseReleased(Lnet/minecraft/client/input/MouseButtonEvent;)Z"))
  private boolean onButtonRedirectMouseReleased(Screen instance, MouseButtonEvent mouseButtonEvent) {
    return ScreenProxy.INSTANCE.mouseReleased(instance, mouseButtonEvent);
  }
}
