/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPrice
 * Filename: TypeDraftServiceImpl.java
 * Date: 27.05.2010
*/
package org.tagaprice.server.rpc;

import java.util.ArrayList;

import org.tagaprice.shared.PropertyDefinition;
import org.tagaprice.shared.PropertyGroup;
import org.tagaprice.shared.Type;
import org.tagaprice.shared.Unit;
import org.tagaprice.shared.rpc.TypeHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TypeHandlerImpl extends RemoteServiceServlet implements TypeHandler {

	@Override
	public Type get(long id) throws IllegalArgumentException {
		Type type;
		
		if(id==9){			
			type = new Type("eisen", 9, 25, new Type("metall", 10, 15, new Type("werkzeug", 1, 5, null)));
			PropertyGroup pg =new PropertyGroup("NutritionFacts", PropertyGroup.GroupType.LIST);
			pg.addGroupElement(new PropertyDefinition(2L, 1, "energy", "Energy", 1, PropertyDefinition.Datatype.DOUBLE,0, 15, null,true)); 
			pg.addGroupElement(new PropertyDefinition(3L, 2, "protein", "Protein", 1, PropertyDefinition.Datatype.DOUBLE, -5, 20, null,true));
			pg.addGroupElement(new PropertyDefinition(4L, 3, "url", "URL", 1, PropertyDefinition.Datatype.STRING,-10, 25, null,false));
			type.addPropertyGroup(pg);
			PropertyGroup pg5 = new PropertyGroup("NUTRITIONFACTS", PropertyGroup.GroupType.LIST);
			pg5.addGroupElement(new PropertyDefinition(2L, 1, "ean", "BAR", 1, PropertyDefinition.Datatype.STRING, 1, 14, null,false)); 

			type.addPropertyGroup(pg5);
		}else if(id==10){
			type = new Type("metall", 10, 11, new Type("werkzeug", 5, 6, null));
			PropertyGroup pg =new PropertyGroup("speedeigenschaften", PropertyGroup.GroupType.LIST);
			pg.addGroupElement(new PropertyDefinition(2L, 1, "energy", "Energy", 1, PropertyDefinition.Datatype.DOUBLE,4, 7, null ,true)); 
			pg.addGroupElement(new PropertyDefinition(3L, 2, "kw", "KW", 1, PropertyDefinition.Datatype.DOUBLE,0, 1000, null,true));
			pg.addGroupElement(new PropertyDefinition(4L, 3, "url", "URL", 1, PropertyDefinition.Datatype.STRING,15, 2000, null,false));
			type.addPropertyGroup(pg);
		}else if(id==5){			
			type=new Type("werkzeug", 5, 6, null);
			PropertyGroup pg =new PropertyGroup("werkzeug", PropertyGroup.GroupType.LIST);
			pg.addGroupElement(new PropertyDefinition(2L, 1, "energy", "Energy", 1, PropertyDefinition.Datatype.DOUBLE,100, 200, null,true)); 
			type.addPropertyGroup(pg);
		}else if(id==0){			
			type=new Type("root", 0, 6, null);
		}else{
			type=new Type("auto", 6, 7, null);
			PropertyGroup pg =new PropertyGroup("auto", PropertyGroup.GroupType.LIST);
			pg.addGroupElement(new PropertyDefinition(2L, 1, "energy", "Energy", 1, PropertyDefinition.Datatype.DOUBLE,99, 101, null,true)); 
			type.addPropertyGroup(pg);
		}
		
		
		
		
		return type;
	}

	@Override
	public ArrayList<Type> getTypeList(Type type)
			throws IllegalArgumentException {
		
		ArrayList<Type> types = new ArrayList<Type>();
	
		if(type.getTitle().equals("root")){
			types.add(new Type("nahrung", 4, -4, null));
			types.add(new Type("werkzeug", 5, -5, null));
			types.add(new Type("auto", 6, -6, null));
		}else if(type.getTitle().equals("nahrung")){
			types.add(new Type("flussig", 7, -7, type));
			types.add(new Type("fest", 8, -8, type));				
		}else if(type.getTitle().equals("werkzeug")){
			types.add(new Type("holz", 9, -9, type));
			types.add(new Type("metall", 10, -10, type));
		}else if(type.getTitle().equals("auto")){
			types.add(new Type("lkw", 11, -11, type));
			types.add(new Type("pkw", 12, -12, type));
			
		}else if(type.getTitle().equals("flussig")){
			types.add(new Type("milch", 13, -13, type));
			types.add(new Type("tee", 14, -14, type));
		}else if(type.getTitle().equals("fest")){
			types.add(new Type("rind", 15, -15, type));
			types.add(new Type("pute", 16, -16, type));
		}else if(type.getTitle().equals("holz")){
			types.add(new Type("kirsche", 17, -17, type));
			types.add(new Type("birne", 18, -18, type));
		}else if(type.getTitle().equals("metall")){
			types.add(new Type("alu", 19, -19, type));
			types.add(new Type("eisen", 9, -20, type));
		}else if(type.getTitle().equals("Food")){
			types.add(new Type("food1", 21, -21, type));
			types.add(new Type("food2", 22, -22, type));
		}
			
		
		return types;
	}
	
	

}
