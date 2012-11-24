/*
 * Copyright 2009 IT Mill Ltd.
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
package com.thingtrack.db4o.web;

import org.dellroad.stuff.vaadin.SpringContextApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import com.thingtrack.db4o.server.Db4oEntityManager;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MainApplication extends SpringContextApplication {
	@Autowired
	private Db4oEntityManager db4oEntityManager;
	
	private Window window;
	
	private VerticalLayout mainLayout; 	
	private UserComponent userComponent;
	
	@Override
	protected void initSpringApplication(ConfigurableWebApplicationContext context) {
	     window = new Window("db4o User Replication");
	     setMainWindow(window);
	     
	     // set full size and none margin for the default window layout
	     mainLayout = (VerticalLayout)window.getContent();
	     mainLayout.setSizeFull();
	     mainLayout.setMargin(false);
	        	     	     
	     // draw main user component
	     userComponent = new UserComponent(db4oEntityManager);
	     userComponent.setSizeFull();
	     mainLayout.addComponent(userComponent);
		
	}
    
}
