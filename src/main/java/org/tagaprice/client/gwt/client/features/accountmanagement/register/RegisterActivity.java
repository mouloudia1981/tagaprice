package org.tagaprice.client.gwt.client.features.accountmanagement.register;

import org.tagaprice.client.gwt.client.ClientFactory;
import org.tagaprice.client.gwt.client.generics.events.InfoBoxEvent;
import org.tagaprice.client.gwt.client.generics.events.InfoBoxEvent.INFOTYPE;
import org.tagaprice.client.gwt.shared.logging.LoggerFactory;
import org.tagaprice.client.gwt.shared.logging.MyLogger;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RegisterActivity implements IRegisterView.Presenter, Activity {
	private static final MyLogger _logger = LoggerFactory.getLogger(RegisterActivity.class);

	private RegisterPlace _place;
	private ClientFactory _clientFactory;
	private IRegisterView _registerView;

	public RegisterActivity(RegisterPlace place, ClientFactory clientFactory) {
		RegisterActivity._logger.log("RegisterActivity created");

		_place = place;
		_clientFactory = clientFactory;
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		RegisterActivity._logger.log("activity startet");

		if (_registerView == null)
			_registerView = _clientFactory.getRegisterView();
		_registerView.setPresenter(this);
		panel.setWidget(_registerView);
	}

	@Override
	public void goTo(Place place) {
		this._clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void checkEmail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegisterButtonEvent() {
		RegisterActivity._logger.log("Register Button Pressed");


		RegisterActivity._logger.log("Email: "+_registerView.getEmail());
		RegisterActivity._logger.log("PW: "+_registerView.getPassword());
		RegisterActivity._logger.log("PW2: "+_registerView.getConfirmPassword());
		RegisterActivity._logger.log("challange: "+_registerView.getChallenge());
		RegisterActivity._logger.log("response: "+_registerView.getResponse());

		if(_registerView.getEmail().isEmpty())
			_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Email is empty", INFOTYPE.ERROR,0));

		if(_registerView.getPassword().isEmpty())
			_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Password is empty", INFOTYPE.ERROR,0));

		if(!_registerView.getPassword().equals(_registerView.getConfirmPassword()))
			_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Password and Confirm Password are not equal", INFOTYPE.ERROR,0));

		if(!_registerView.getEmail().isEmpty() && !_registerView.getPassword().isEmpty() && _registerView.getPassword().equals(_registerView.getConfirmPassword())){
			_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Register...", INFOTYPE.INFO));

			_clientFactory.getLoginService().isEmailAvailable(_registerView.getEmail(), new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					if(response==true){

						_clientFactory.getLoginService().registerUser(
								_registerView.getEmail(),
								_registerView.getPassword(),
								_registerView.getConfirmPassword(),
								_registerView.getChallenge(),
								_registerView.getResponse(),
								new AsyncCallback<Boolean>() {

									@Override
									public void onSuccess(Boolean response) {
										if(response==true){
											_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Juhu. You are registered!!!", INFOTYPE.SUCCESS));

										}else{
											_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Oooops but there is a problem with your registration ;-(", INFOTYPE.ERROR,0));

										}

									}

									@Override
									public void onFailure(Throwable e) {
										_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Exception: "+e, INFOTYPE.ERROR,0));

									}
								});
					}else
						_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Email not available ", INFOTYPE.ERROR,0));


				}

				@Override
				public void onFailure(Throwable e) {
					_clientFactory.getEventBus().fireEvent(new InfoBoxEvent("Exception: "+e, INFOTYPE.ERROR,0));

				}
			});
		}




	}
}