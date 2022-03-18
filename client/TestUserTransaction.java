//package client;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.transaction.UserTransaction;

import org.wildfly.transaction.client.RemoteTransactionContext;
import org.wildfly.transaction.client.RemoteUserTransaction;

//import com.test.usertransaction.ControllerRemote;
import com.test.ControllerRemote;

public class TestUserTransaction {


	private String host = "localhost";
	private Integer port = 8080;
	private String username = "ejbuser";
	private String password = "redhat1!";

	public TestUserTransaction() {
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		TestUserTransaction tAPI = new TestUserTransaction();
		if(args.length <= 0 || Integer.parseInt(args[0]) == 1)
			tAPI.testTransaction();
		else if (Integer.parseInt(args[0]) == 2)
			tAPI.testTransaction2();
	}

	private void testTransaction() throws Exception {	
    	
		System.out.println("Running testTransaction() with UserTransaction");
	
		for(int i=1; i< 500000; i++) {
			System.out.println("Starting process " + i);

      // get the UserTransaction and EJB Proxy
			Context ctx = getInitialContext(host, port, username, password);
      UserTransaction tx = getUserTransaction(ctx);
			ControllerRemote cr = (ControllerRemote) ctx.lookup("Controller/Controller" + "!com.test.usertransaction.ControllerRemote");
		
			try {
				tx.begin();
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
			}
			//System.out.println("user transaction started");
				
			cr.mainCall();

			try {
				tx.commit();;
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
				throw ex1;
			}
			//System.out.println("commited user transaction");

			if(ctx != null)
				ctx.close();
		}
	}


	private void testTransaction2() throws Exception {	
		System.out.println("Running testTransaction2() with RemoteTransactionContext.getInstance().getUserTransaction()");

    		RemoteUserTransaction tx = RemoteTransactionContext.getInstance().getUserTransaction();
    	
		for(int i=1; i< 500000; i++) {
			System.out.println("Starting process " + i);

      // get the EJB Proxy
			Context ctx = getInitialContext(host, port, username, password);
		//	ControllerRemote cr = (ControllerRemote) ctx.lookup("Controller/Controller" + "!com.test.usertransaction.ControllerRemote");
		

			ControllerRemote cr = (ControllerRemote) ctx.lookup("ejb-project-1.0-SNAPSHOT/Controller!com.test.ControllerRemote");
			try {
				tx.begin();
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
			}
			//System.out.println("user transaction started");
				
			cr.mainCall();

			try {
				tx.commit();;
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
				throw ex1;
			}
			//System.out.println("commited user transaction");

			if(ctx != null)
				ctx.close();
		}
	}

  public UserTransaction getUserTransaction(Context ctx) throws NamingException {
    return (UserTransaction) ctx.lookup("txn:UserTransaction");
  }

  public static Context getInitialContext(String host, Integer port, String username, String password) throws NamingException {
     Properties props = new Properties();
     props.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
     props.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "remote+http", host, port));
     props.put(Context.SECURITY_PRINCIPAL, username);
     props.put(Context.SECURITY_CREDENTIALS, password);
     return new InitialContext(props);
  }


}
