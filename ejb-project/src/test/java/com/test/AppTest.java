//package com.test;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.wildfly.transaction.client.RemoteTransactionContext;
import org.wildfly.transaction.client.RemoteUserTransaction;
import com.test.ControllerRemote;
public class AppTest {
	public AppTest() {
	}
	public static void main(String[] args) throws Exception{
		AppTest tAPI = new AppTest();
		tAPI.testTransaction();
	}
	private void testTransaction() throws Exception
	{
    		RemoteUserTransaction tx = RemoteTransactionContext.getInstance().getUserTransaction();
		for(int i=1; i< 10000; i++)
		{
			System.out.println("Starting process " + i);
			try {
				tx.begin();
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
			}
			Context ctx = null;
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
			env.put(Context.PROVIDER_URL, "remote+http://8080"); 
			ctx = new InitialContext(env);
			ControllerRemote cr = (ControllerRemote) ctx.lookup("ejb-project-1.0-SNAPSHOT/Controller!com.test.ControllerRemote");
			cr.mainCall();
			try {
				tx.commit();;
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
				throw ex1;
			}
			if(ctx != null)
				ctx.close();
		}
	}
}
