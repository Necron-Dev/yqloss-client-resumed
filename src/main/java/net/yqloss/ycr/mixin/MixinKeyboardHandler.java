package net.yqloss.ycr.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.yqloss.ycr.gui.system.ScreenProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler {
  @Redirect(method = "charTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;charTyped(Lnet/minecraft/client/input/CharacterEvent;)Z"))
  private boolean charTypedRedirectCharTyped(Screen instance, CharacterEvent characterEvent) {
    return ScreenProxy.INSTANCE.charTyped(instance, characterEvent);
  }

  @Redirect(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;getFocused()Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
  private GuiEventListener keyPressRedirectGetFocused(Screen instance) {
    return ScreenProxy.INSTANCE.getFocused(instance);
  }

  @Redirect(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z"))
  private boolean keyPressRedirectKeyPressed(Screen instance, KeyEvent keyEvent) {
    return ScreenProxy.INSTANCE.keyPressed(instance, keyEvent);
  }

  @Redirect(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;keyReleased(Lnet/minecraft/client/input/KeyEvent;)Z"))
  private boolean keyPressRedirectKeyReleased(Screen instance, KeyEvent keyEvent) {
    return ScreenProxy.INSTANCE.keyReleased(instance, keyEvent);
  }

  @Redirect(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;afterKeyboardAction()V"))
  private void keyPressRedirectAfterKeyboardAction(Screen instance) {
    ScreenProxy.INSTANCE.afterKeyboardAction(instance);
  }
}
