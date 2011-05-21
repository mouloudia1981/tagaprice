package org.tagaprice.server.rpc;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.tagaprice.server.dao.IDaoFactory;
import org.tagaprice.server.dao.ISessionDao;
import org.tagaprice.server.dao.IUserDao;
import org.tagaprice.shared.entities.accountmanagement.Session;
import org.tagaprice.shared.entities.accountmanagement.User;
import org.tagaprice.shared.exceptions.UserAlreadyLoggedInException;
import org.tagaprice.shared.exceptions.UserNotLoggedInException;
import org.tagaprice.shared.exceptions.WrongEmailOrPasswordException;
import org.tagaprice.shared.exceptions.dao.DaoException;
import org.tagaprice.shared.logging.LoggerFactory;
import org.tagaprice.shared.logging.MyLogger;
import org.tagaprice.shared.rpc.accountmanagement.ILoginService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements ILoginService {

	private static final long serialVersionUID = 2766434026811432034L;


	HttpSession session;

	static MyLogger _logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	private ISessionDao _sessionDao;
	private IUserDao _userDao;

	private static Random _random = _createPRNG();

	public LoginServiceImpl() {
		IDaoFactory daoFactory = InitServlet.getDaoFactory();

		_sessionDao = daoFactory.getSessionDao();
		_userDao = daoFactory.getUserDao();
	}

	@Override
	public String setLogin(String email, String password) throws WrongEmailOrPasswordException,
	UserAlreadyLoggedInException, DaoException {
		User user = _userDao.getByMail(email);
		String rc = null;

		if (_checkPassword(user, password)) {
			// create Session (default expiration time: 1h)
			Date expirationDate = new Date(Calendar.getInstance().getTimeInMillis()+3600000);
			Session session = _sessionDao.create(new Session(user, expirationDate));
			rc = session.getId();
		}
		else throw new WrongEmailOrPasswordException("Please controll user and password");

		return rc;
	}

	@Override
	public void setLogout() throws UserNotLoggedInException {
		try {
			Session session = _sessionDao.get(getSid());
			_sessionDao.delete(session);

		} catch (DaoException e) {
			throw new UserNotLoggedInException("Logout error", e);
		}
	}

	@Override
	public String isLoggedIn() {
		String rc = null;

		try {
			rc = _sessionDao.get(getSid()).getId();
		}
		catch (DaoException e) {/* we'll simply return null */}

		return rc;
	}

	@Override
	public Boolean isEmailAvailable(String email) {
		User user = _userDao.getByMail(email);
		return user == null;
	}



	@Override
	public Boolean setNewPassword(String oldPassword, String newPassword, String newPassword2)
	throws UserNotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean registerUser(String email, String password, boolean agreeTerms) throws DaoException {
		// TODO do some error handling here
		if (!agreeTerms) return false;
		if (!isEmailAvailable(email)) return false;
		if (password.length() < 6) return false;

		LoginServiceImpl._logger.log("Try to register: email: " + email + ", password: " + password);


		String salt = generateSalt(24);
		String pwdHash;

		try {
			pwdHash = md5(password+salt);
		}
		catch (NoSuchAlgorithmException e) {
			throw new DaoException("Couldn't generate password hash: "+e.getMessage(), e);
		}

		User user = new User(email); // TODO we need an actual user name here
		user.setMail(email);
		user.setPasswordSalt(salt);
		user.setPasswordHash(pwdHash);
		_userDao.create(user);

		return true; // I don't want a session to be returned that soon. There should be a mail verification before
	}


	private String getSid(){
		if(session==null)session = getThreadLocalRequest().getSession();

		return session.getId();
	}

	private boolean _checkPassword(User user, String password) throws DaoException {
		String hash = user.getPasswordHash();
		String salt = user.getPasswordSalt();

		try {
			return hash.equals(md5(password+salt));
		}
		catch (NoSuchAlgorithmException e) {
			throw new DaoException("Couldn't generate password hash: "+e.getMessage(), e);
		}
	}

	public String md5(String in) throws NoSuchAlgorithmException {
		// calculate hash
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(in.getBytes());
		byte[] hash = md5.digest();
		StringBuffer rc = new StringBuffer();
		for (int i=0; i<hash.length; i++) {
			String hex = "0"+Integer.toHexString(0xFF & hash[i]);
			if (hex.length()>2) hex = hex.substring(hex.length()-2);
			rc.append(hex);
		}

		return rc.toString();
	}

	/**
	 * Use a Pseudo Random Number Generator to generate an arbitrary-length random String that
	 * can be used as password hash salt (or for anything else)
	 * 
	 * @param len Desired length of the returned String
	 * @return Random String with the given length
	 */
	public static String generateSalt(int len) {
		String rc = "";

		for (int i = 0; i < len; i++) {
			int n = LoginServiceImpl._random.nextInt(62);
			char c;
			if (n < 26) c = (char)(n+'a');
			else if (n < 52) c = (char)(n-26+'A');
			else c = (char) (n-52+'0');
			rc += c;
		}
		return rc;
	}


	/**
	 * Creates, initializes and returns a Pseudo Random Number Generator object
	 * 
	 * On UNIX-like systems this function initializes the PRNG using data read from
	 * /dev/urandom to make sure the seed is less predictable.
	 * @return PRNG object
	 */
	private static Random _createPRNG() {
		Random rc = null;

		try {
			FileInputStream in = new FileInputStream("/dev/urandom");
			int n;
			long seed = 0;

			// read 8 characters and put them in a long variable
			for (int i = 0; i < 8; i++) {
				n = in.read();
				if(n >= 0) {
					seed *= 256;
					seed += n;
				}
			}

			rc = new Random(seed);
		}
		catch (IOException e) { // /dev/urandom can't be read
			LoginServiceImpl._logger.log("Warning: using current time as random seed");
			rc = new Random();
		}

		return rc;
	}

}
