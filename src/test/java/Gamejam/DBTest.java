package Gamejam;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.AccountManager;

public class DBTest {
	AccountManager acctMgr = AccountManager.getInstance();
	@Test
	public void testDeleteAndCreateAccount() {
		// create accounts
		assertEquals(1, acctMgr.createAccount("linjieliu", "923"));
		assertEquals(2, acctMgr.createAccount("linjieliu", "456"));
		assertEquals(1, acctMgr.createAccount("abc", "123"));
		
		//delete accounts
		assertEquals(1, acctMgr.deleteAccount("abdq"));
		assertEquals(1, acctMgr.deleteAccount("abc"));
		assertEquals(1, acctMgr.deleteAccount("linjieliu"));
	}

	@Test
	public void testLogIn() {
		acctMgr.createAccount("linjieliu", "923");
		assertTrue(acctMgr.login("linjieliu", "923"));
		assertFalse(acctMgr.login("linjieliu", "123"));
		acctMgr.deleteAccount("linjieliu");
	}

}
