/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPriceUI
 * Filename: TypeWidget.java
 * Date: 02.07.2010
*/
package org.tagaprice.client;

import java.util.ArrayList;
import org.tagaprice.shared.Type;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Create a type selection widget optimized for finger clicking.  
 *
 */
public class TypeWidget extends Composite{

	TaPManager TaPMng = TaPManager.getInstance();
	Type type;
	HorizontalPanel hoPa1=new HorizontalPanel();
	PopupPanel typeItems = new PopupPanel(true);
	TypeWidgetHandler handler;
	
	/**
	 * 
	 * @param type If type is root, only one arrow and no text is displayed. 
	 * @param handler
	 */
	public TypeWidget(Type type, TypeWidgetHandler handler) {
		this.type=type;
		this.handler=handler;
				
		//hoPa1.setWidth("100%");
		initWidget(hoPa1);
		setStyleName("TypeWidget");
		
		createMenu();
	}
	
	
	private void createMenu(){
		//Type
		Type iterType = type;
		hoPa1.clear();
		
		if(!iterType.getTitle().equals("root")){		
			do{
				final Type innerType = iterType;
				Label typeLabel = new Label(iterType.getTitle());
				typeLabel.setStyleName("TypeWidget-Item");
				hoPa1.insert(typeLabel, 0);
				final Image arrow = new Image(MyResources.INSTANCE.typeSelectRight());
				//final Button arrow = new Button(">");
				hoPa1.insert(arrow,1);			
				final String sType=iterType.getTitle();	
				
				//Open Product by Type
				typeLabel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						System.out.println("Open Type: "+sType);
						
					}
				});
				
				arrow.addMouseOverHandler(new MouseOverHandler() {
					
					@Override
					public void onMouseOver(MouseOverEvent event) {
						typeItems.setWidget(new Label("Loading..."));					
						HandlerManager.getTypeHandler().getTypeList(innerType, new AsyncCallback<ArrayList<Type>>() {
							
							@Override
							public void onSuccess(ArrayList<Type> result) {
								
								VerticalPanel vePa1 = new VerticalPanel();
								//vePa1.add(new Button("---"));
								for(final Type ty:result){
									Label tsb=new Label(ty.getTitle());
									tsb.setStyleName("TypeWidget-Item");
									tsb.addClickHandler(new ClickHandler() {									
										@Override
										public void onClick(ClickEvent event) {
											handler.onChange(ty);
											typeItems.hide();
										}
									});
									
									vePa1.add(tsb);
								}
								typeItems.setWidget(vePa1);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								//TODO TaPMng.getInfoBox().showInfo("typeWidget Error", BoxType.WARNINGBOX);
							}
						});				
						
						typeItems.showRelativeTo(arrow);
						
					}
				});
				
				
			}while((iterType=iterType.getSuperType())!=null);
		}

		//Root elem
		final Image rootB = new Image(MyResources.INSTANCE.typeSelectRight());
		final int localeId = type.getLocaleId();
		
		rootB.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				typeItems.setWidget(new Label("Loading..."));					
				HandlerManager.getTypeHandler().getTypeList(new Type("root", localeId, 1, null), new AsyncCallback<ArrayList<Type>>() {
					
					@Override
					public void onSuccess(ArrayList<Type> result) {
						
						VerticalPanel vePa1 = new VerticalPanel();
						//vePa1.add(new Button("---"));
						for(final Type ty:result){
							Label tsb=new Label(ty.getTitle());
							tsb.setStyleName("TypeWidget-Item");
							tsb.addClickHandler(new ClickHandler() {									
								@Override
								public void onClick(ClickEvent event) {
									
									handler.onChange(ty);
									typeItems.hide();
								}
							});
							
							vePa1.add(tsb);
						}
						typeItems.setWidget(vePa1);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						//TODO TaPMng.getInfoBox().showInfo("typeWidget Error", BoxType.WARNINGBOX);
					}
				});				
				
				typeItems.showRelativeTo(rootB);
				
			}
		});
		
		hoPa1.insert(rootB,0);	
	}
}
