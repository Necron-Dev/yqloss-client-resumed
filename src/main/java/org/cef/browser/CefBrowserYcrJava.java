package org.cef.browser;

import kotlin.NotImplementedError;
import org.cef.CefBrowserSettings;
import org.cef.CefClient;
import org.cef.handler.CefRenderHandler;

import java.awt.*;
import java.lang.reflect.Field;

public abstract class CefBrowserYcrJava extends CefBrowser_N implements CefRenderHandler {
  protected CefBrowserYcrJava(CefClient client, String url, CefBrowserSettings settings) {
    super(client, url, null, null, null, settings);
  }

  @Override
  public void createImmediately() {
    createBrowser(getClient(), 0, getUrl(), true, true, null, getRequestContext());
  }

  @Override
  protected CefBrowser_N createDevToolsBrowser(CefClient client, String url, CefRequestContext context, CefBrowser_N parent, Point inspectAt) {
    throw new NotImplementedError();
  }

  public Field getIsPendingField() throws NoSuchFieldException {
    var field = CefBrowser_N.class.getDeclaredField("isPending_");
    field.setAccessible(true);
    return field;
  }
}
