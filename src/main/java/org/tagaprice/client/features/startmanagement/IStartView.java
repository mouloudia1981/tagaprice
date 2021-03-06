package org.tagaprice.client.features.startmanagement;

import org.tagaprice.client.generics.events.InfoBoxShowEvent;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface IStartView extends IsWidget {

	/**
	 * Sets the displayed title
	 * 
	 * @param title
	 *            the displayed title
	 */
	public void setTitle(String title);

	/**
	 * Sets the {@link Presenter} which implements the {@link IProductView} to control this view. It is also necessary
	 * for the {@link IProductView} to communicate with the {@link Presenter}
	 * 
	 * @param presenter
	 *            Sets the {@link Presenter} which implements the {@link IProductView} to control this view.
	 */
	public void setPresenter(Presenter presenter);
	
	/**
	 * Returns the Invite key 
	 * @return invite key
	 */
	public String getInviteKey();
	

	public void setInviteKey(String inviteKey);

	public interface Presenter {
		/**
		 * Is used by the {@link org.tagaprice.client.mvp.AppActivityMapper} to display a new place in the
		 * browser window.
		 * 
		 * @param place
		 *            The {@link Place} which should be displayed next.
		 */
		public void goTo(Place place);
		
		/**
		 * invite buttons clicked
		 */
		public void onInvieteMe();
		
		/**
		 * register button clicked
		 */
		public void onRegisterWithKey();
	}

}
