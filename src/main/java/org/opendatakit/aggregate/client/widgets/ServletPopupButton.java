package org.opendatakit.aggregate.client.widgets;

import org.opendatakit.aggregate.client.FormsSubTab;
import org.opendatakit.aggregate.client.popups.ViewServletPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;

public class ServletPopupButton extends AButtonBase implements ClickHandler {
  private String url;
  private String title;
  private FormsSubTab basePanel;

  public ServletPopupButton(String buttonText, String title, String url, FormsSubTab basePanel) {
    super(buttonText);
    this.title = title;
    this.url = url;
    this.basePanel = basePanel;
    addClickHandler(this);
  }

  @Override
  public void onClick(ClickEvent event) {
    super.onClick(event);

    final ViewServletPopup servletPopup = new ViewServletPopup(title, url);
    servletPopup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
      @Override
      public void setPosition(int offsetWidth, int offsetHeight) {
        int left = (Window.getClientWidth() - offsetWidth) / 2;
        int top = (Window.getClientHeight() - offsetHeight) / 2;
        servletPopup.setPopupPosition(left, top);
      }
    });
    servletPopup.addCloseHandler(new CloseHandler<PopupPanel>() {

      @Override
      public void onClose(CloseEvent<PopupPanel> event) {
        basePanel.update();
      }

    });
  }
}