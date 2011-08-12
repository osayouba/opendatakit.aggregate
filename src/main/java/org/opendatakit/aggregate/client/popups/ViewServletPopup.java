/*
 * Copyright (C) 2011 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.aggregate.client.popups;

import org.opendatakit.aggregate.client.widgets.ClosePopupButton;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.PopupPanel;

public class ViewServletPopup extends PopupPanel {

  public ViewServletPopup(String buttonText, String url) {
    super(false);
    setTitle(buttonText);
    
    // so we can play with the dimensions...
    int innerWidth = Window.getClientWidth()*5 / 6;
    int innerHeight = Window.getClientHeight()*5 /6;
    
    Frame frame = new Frame(url);
    frame.setPixelSize(innerWidth,innerHeight); 

    DockLayoutPanel panel = new DockLayoutPanel(Unit.EM);
    panel.setPixelSize(innerWidth+6,innerHeight+30);
    panel.addNorth(new ClosePopupButton(this), 2);   
    panel.add(frame);      
    setWidget(panel);
  }
}
