package org.tagaprice.client;

import java.util.ArrayList;
import java.util.List;

import org.tagaprice.client.generics.events.InfoBoxShowEvent;
import org.tagaprice.client.generics.events.InfoBoxShowEvent.INFOTYPE;
import org.tagaprice.client.generics.events.LoginChangeEvent;
import org.tagaprice.shared.entities.Address;
import org.tagaprice.shared.entities.Address.LatLon;
import org.tagaprice.shared.entities.accountmanagement.User;
import org.tagaprice.shared.entities.receiptManagement.Receipt;
import org.tagaprice.shared.exceptions.UserNotConfirmedException;
import org.tagaprice.shared.exceptions.UserNotLoggedInException;
import org.tagaprice.shared.exceptions.WrongEmailOrPasswordException;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class holds all important information that we need to know at the start of tagaprice, or during surfing around.
 * 
 * 
 */
public class AccountPersistor implements IAccountPersistor {


	private Address I_ADDRESS;
	private ClientFactory _clientFactory;
	private Receipt _receipt=null;
	private User _user=null;
	
	public AccountPersistor() {
	}

	@Override
	public void setClientFactory(ClientFactory clientFactory) {
		_clientFactory=clientFactory;
	}


	
	public void addAddress(Address address){
		
		if(Cookies.getCookie("TAP_address_count")==null){
			Cookies.setCookie("TAP_address_count", "0");
		}
		
		
		int c = Integer.parseInt(Cookies.getCookie("TAP_address_count"));
		
		Cookies.setCookie("TAP_address_street_array_"+c,address.getStreet());
		Cookies.setCookie("TAP_address_lat_array_"+c,""+address.getPos().getLat());
		Cookies.setCookie("TAP_address_lon_array_"+c,""+address.getPos().getLon());
		c++;
		Cookies.setCookie("TAP_address_count", ""+c);
	}
	
	
	public List<Address> getAddressList(){
		ArrayList<Address> rc = new ArrayList<Address>();
		
		if(Cookies.getCookie("TAP_address_count")!=null){
			int c = Integer.parseInt(Cookies.getCookie("TAP_address_count"));
			
			for(int i=0;i<c;i++){
				
				Address a = new Address();
				
				a.setStreet(Cookies.getCookie("TAP_address_street_array_"+i));
				
				
				a.setPos(new LatLon(
						Double.parseDouble(Cookies.getCookie("TAP_address_lat_array_"+i)), 
						Double.parseDouble(Cookies.getCookie("TAP_address_lon_array_"+i))));
				
				
				rc.add(a);
			}
			
		}
		
		return rc;
	}
	
	/**
	 * Returns global Address
	 */
	
	public Address getCurAddress() {

		if(Cookies.getCookie("TAP_cur_address_street")!=null &&
				Cookies.getCookie("TAP_cur_address_lat")!=null &&
				Cookies.getCookie("TAP_cur_address_lon")!=null){
			
			Address a = new Address();
			a.setStreet(Cookies.getCookie("TAP_cur_address_street"));
			a.setPos(new LatLon(
					Double.parseDouble(Cookies.getCookie("TAP_cur_address_lat")), 
					Double.parseDouble(Cookies.getCookie("TAP_cur_address_lon"))));
		
			return a;
		}

		return null;
	}
	


	/**
	 * Set Global Address. Saves it also in the cookies.
	 * @param address setGlobalAddress
	 */
	public void setCurAddress(Address address) {
		Log.debug("setAddress: "+address);

		Cookies.setCookie("TAP_cur_address_street", address.getStreet());
		Cookies.setCookie("TAP_cur_address_lat", ""+address.getPos().getLat());
		Cookies.setCookie("TAP_cur_address_lon", ""+address.getPos().getLon());
	}


	/**
	 * Logout user and remove sid
	 */
	@Override
	public void logout() {
		_user=null;
		Log.debug("LogOut Button clicked");
		Cookies.removeCookie("TAP_SID");
		_clientFactory.getEventBus().fireEvent(new LoginChangeEvent(false));
		
		//Go To Login Place
		//goTo(new LoginPlace());



		_clientFactory.getLoginService().setLogout(new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void value) {
				Log.debug("Logout was ok: " + value);
				//Send User login event
				_user=null;
			}

			@Override
			public void onFailure(Throwable caught) {
				try {
					throw caught;
				} catch (UserNotLoggedInException e) {
					Log.warn("Login problem: " + e);
				} catch (Throwable e) {
					Log.error("Unexpected error: " + e);
				}
			}
		});
	}


	/**
	 * Login and set SessionId
	 * @param email email
	 * @param password Password
	 */
	@Override
	public void login(String email, String password){
		_clientFactory.getLoginService().setLogin(email, password, new AsyncCallback<User>() {

			@Override
			public void onSuccess(User user) {
				Log.debug("Login OK. SessionId: " + user.getMail());
				_user=user;
				//Send User login event
				_clientFactory.getEventBus().fireEvent(new LoginChangeEvent(true));

				//Go to user Area
				_clientFactory.getEventBus().fireEvent(new InfoBoxShowEvent(AccountPersistor.class, "You are logged in.", INFOTYPE.SUCCESS));

			}

			@Override
			public void onFailure(Throwable caught) {
				try {
					throw caught;
				} catch (WrongEmailOrPasswordException e) {
					Log.warn("Login problem: " + e);
					_clientFactory.getEventBus().fireEvent(new InfoBoxShowEvent(AccountPersistor.class, "Your email and password is incorrect. Register or try again. ", INFOTYPE.ERROR));
				} catch (UserNotConfirmedException e){
					_clientFactory.getEventBus().fireEvent(new InfoBoxShowEvent(AccountPersistor.class, "Please check your email and click the confirmation link.", INFOTYPE.ERROR));
				}catch (Throwable e) {
					Log.error("Unexpected error: " + e);
				} 

			}
		});
	}

	/**
	 * This method checks the login status and will fire all necessary events
	 */
	@Override
	public void checkLogin(){
		
		_clientFactory.getLoginService().isLoggedIn(new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User user) {
				if(user!=null){
					_user=user;
					//Send User login event
					_clientFactory.getEventBus().fireEvent(new LoginChangeEvent(true));
				}else{
					logout();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				try {
					throw caught;
				} catch (WrongEmailOrPasswordException e) {
					Log.warn("Login problem: " + e);
					_clientFactory.getEventBus().fireEvent(new InfoBoxShowEvent(AccountPersistor.class, "Something is wrong ", INFOTYPE.ERROR));
				} catch (Throwable e) {
					Log.error("Unexpected error: " + e);
				}				
			}
		});
		

	}



	@Override
	public void setReceiptDraft(Receipt draft) {
		Log.debug("set Receipt draft: ");
		_receipt=draft;
	}

	@Override
	public Receipt getReceiptDraft() {
		return _receipt;
	}

	@Override
	public boolean isLoggedIn() {
		if(_user!=null)
			return true;
		
		return false;
	}

	@Override
	public User getUser() {
		return _user;
	}
}
