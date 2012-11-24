package com.thingtrack.db4o.web;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.events.CommitEventArgs;
import com.db4o.events.Event4;
import com.db4o.events.EventListener4;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.foundation.Iterator4;
import com.db4o.internal.LazyObjectReference;

import com.thingtrack.db4o.domain.User;
import com.thingtrack.db4o.server.Db4oEntityManager;
import com.thingtrack.db4o.web.WindowDialog.DialogResult;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserComponent extends CustomComponent implements ServerInfo{

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Table tbUsers;
	@AutoGenerated
	private HorizontalLayout hlToolbar;
	@AutoGenerated
	private Button btnRemoveUser;
	@AutoGenerated
	private Button btnUpdateUser;
	@AutoGenerated
	private Button btnAddUser;
	@AutoGenerated
	private Button btnLoad;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	private Db4oEntityManager db4oEntityManager;
	
	private BeanItemContainer<User> beanItemUsers = new BeanItemContainer<User>(User.class);
	
	public UserComponent(final Db4oEntityManager db4oEntityManager) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		this.db4oEntityManager = db4oEntityManager;
		
		tbUsers.setSelectable(true);
		tbUsers.setMultiSelect(false);

		// TODO add user code here
		btnLoad.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				LoadUsers();
				
			}
		});
		
		btnAddUser.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				final User user = new User();
	    		
				try {
					@SuppressWarnings("unused")
					WindowDialog<User> windowDialog = new WindowDialog<User>(getWindow(), "New User", 
							"Save", DialogResult.SAVE, "Cancel", DialogResult.CANCEL, 
							new UserViewForm(), user, 
							new WindowDialog.CloseWindowDialogListener<User>() {
					    public void windowDialogClose(WindowDialog<User>.CloseWindowDialogEvent<User> event) {
					    	if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
					    		return ;
					    	
					    	try {					    							    		
					    		db4oEntityManager.getEntityManager().store(user);					    							    		
					    		db4oEntityManager.getEntityManager().commit();					    		
					    		
							} catch (Exception e) {
								e.printStackTrace();
								
							}
					    	finally {
					    		
					    	}
					    	
				    		LoadUsers();
					    }

					});
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		});
		
		btnUpdateUser.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				final User userSelected = (User)tbUsers.getValue();
				
				if (userSelected != null) {
					try {
						@SuppressWarnings("unused")
						WindowDialog<User> windowDialog = new WindowDialog<User>(getWindow(), "Update User", 
								"Save", DialogResult.SAVE, "Cancel", DialogResult.CANCEL, 
								new UserViewForm(), userSelected, 
								new WindowDialog.CloseWindowDialogListener<User>() {
						    public void windowDialogClose(WindowDialog<User>.CloseWindowDialogEvent<User> event) {
						    	if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
						    		return ;
						    							    	
						    	
						    	try {					    		
						    		db4oEntityManager.getEntityManager().store(userSelected);						    		
						    		db4oEntityManager.getEntityManager().commit();						    								    		
						    		
								} catch (Exception e) {
									e.printStackTrace();
									
								}
						    	finally {
						    		
						    	}
						    	
					    		LoadUsers();
						    }

						});
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				
			}
		});
		
		btnRemoveUser.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				final com.thingtrack.db4o.domain.User userSelected = (com.thingtrack.db4o.domain.User)tbUsers.getValue();
								
				if (userSelected != null) {				
			    	try {					    		
			    		db4oEntityManager.getEntityManager().delete(userSelected);			    					    		
			    		db4oEntityManager.getEntityManager().commit();
			    		//session.delete(userSelected);
		    			
					} catch (Exception e) {
						e.printStackTrace();
						
					}
			    	finally {

			    	}
			    	
		    		LoadUsers();
		
				}
			}
		});
		
		// refresh onload
		LoadUsers();
		
		registerEvent(db4oEntityManager.getEntityManager());
	}
	
	private void LoadUsers() {		
		try {													
   		 	List<com.thingtrack.db4o.domain.User> users = db4oEntityManager.getEntityManager().query(com.thingtrack.db4o.domain.User.class);
			   		 	   		 
   		 	beanItemUsers.removeAllItems();
	   		beanItemUsers.addAll(users);
	   		
	   		tbUsers.setContainerDataSource(beanItemUsers);
	   		 
	   		tbUsers.setVisibleColumns(new String[] {"username", "password"});
	   		tbUsers.setColumnHeaders(new String[] {"Username", "Password"});
   		 
   	 	}	     
    	catch (Exception e) {
			e.getMessage();
    	}
		finally {
			
		}
	}
	
    private void registerEvent(ObjectContainer container) {
        // on the updated-event we refresh the objects
        EventRegistry events = EventRegistryFactory.forObjectContainer(container);
        events.committed().addListener(new EventListener4<CommitEventArgs>() {
            public void onEvent(Event4<CommitEventArgs> commitEvent, CommitEventArgs commitEventArgs) {
                for(@SuppressWarnings("rawtypes")Iterator4 it = commitEventArgs.updated().iterator(); it.moveNext();){
                    LazyObjectReference reference = (LazyObjectReference) it.current();
                    Object obj = reference.getObject();
                    commitEventArgs.objectContainer().ext().refresh(obj,1);
                }
            }
        });

    }
    
	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// hlToolbar
		hlToolbar = buildHlToolbar();
		mainLayout.addComponent(hlToolbar);
		
		// tbUsers
		tbUsers = new Table();
		tbUsers.setImmediate(false);
		tbUsers.setWidth("100.0%");
		tbUsers.setHeight("100.0%");
		mainLayout.addComponent(tbUsers);
		mainLayout.setExpandRatio(tbUsers, 1.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHlToolbar() {
		// common part: create layout
		hlToolbar = new HorizontalLayout();
		hlToolbar.setImmediate(false);
		hlToolbar.setWidth("100.0%");
		hlToolbar.setHeight("-1px");
		hlToolbar.setMargin(false);
		
		// btnLoad
		btnLoad = new Button();
		btnLoad.setCaption("Refresh");
		btnLoad.setImmediate(true);
		btnLoad.setWidth("-1px");
		btnLoad.setHeight("-1px");
		hlToolbar.addComponent(btnLoad);
		
		// btnAddUser
		btnAddUser = new Button();
		btnAddUser.setCaption("Add User");
		btnAddUser.setImmediate(true);
		btnAddUser.setWidth("-1px");
		btnAddUser.setHeight("-1px");
		hlToolbar.addComponent(btnAddUser);
		
		// btnUpdate
		btnUpdateUser = new Button();
		btnUpdateUser.setCaption("Update User");
		btnUpdateUser.setImmediate(false);
		btnUpdateUser.setWidth("-1px");
		btnUpdateUser.setHeight("-1px");
		hlToolbar.addComponent(btnUpdateUser);
		
		// btnRemove
		btnRemoveUser = new Button();
		btnRemoveUser.setCaption("Remove User");
		btnRemoveUser.setImmediate(false);
		btnRemoveUser.setWidth("-1px");
		btnRemoveUser.setHeight("-1px");
		hlToolbar.addComponent(btnRemoveUser);
		hlToolbar.setExpandRatio(btnRemoveUser, 1.0f);
		
		return hlToolbar;
	}

}