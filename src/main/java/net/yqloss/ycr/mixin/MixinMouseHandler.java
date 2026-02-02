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

  @Redirect(method = "onButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;afterMouseAction()V"))
  private void onButtonRedirectAfterMouseAction(Screen instance) {
    ScreenProxy.INSTANCE.afterMouseAction(instance);
  }

  @Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z"))
  private boolean onScrollRedirectMouseScrolled(Screen instance, double mouseX, double mouseY, double scrollX, double scrollY) {
    return ScreenProxy.INSTANCE.mouseScrolled(instance, mouseX, mouseY, scrollX, scrollY);
  }

  @Redirect(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseMoved(DD)V"))
  private void handleAccumulatedMovementRedirectMouseMoved(Screen instance, double mouseX, double mouseY) {
    ScreenProxy.INSTANCE.mouseMoved(instance, mouseX, mouseY);
  }

  @Redirect(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z"))
  private boolean handleAccumulatedMovementRedirectMouseDragged(Screen instance, MouseButtonEvent mouseButtonEvent, double mouseX, double mouseY) {
    return ScreenProxy.INSTANCE.mouseDragged(instance, mouseButtonEvent, mouseX, mouseY);
  }

  @Redirect(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;afterMouseMove()V"))
  private void handleAccumulatedMovementRedirectAfterMouseMove(Screen instance) {
    ScreenProxy.INSTANCE.afterMouseMove(instance);
  }
}
