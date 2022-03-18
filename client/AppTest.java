import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import com.test.ControllerRemote;
import javax.naming.NamingException;
//import org.wildfly.transaction.client.RemoteTransactionContext;
//import org.wildfly.transaction.client.RemoteUserTransaction;
import javax.transaction.UserTransaction;
public class AppTest {
	public AppTest() {
	}
	public static void main(String[] args) throws Exception{
		AppTest tAPI = new AppTest();
		tAPI.testTransaction();
	}
	private void testTransaction() throws Exception
	{
              		//RemoteUserTransaction tx =RemoteTransactionContext.getInstance().getUserTransaction();
			System.out.println("Running testTransaction() with UserTransaction");
    	for(int i=1; i< 50000; i++)
		{
                        
			System.out.println("Starting process " + i);
			
			Context ctx = null;
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
			env.put(Context.PROVIDER_URL,String.format("%s://%s:%d","remote+http","localhost",8080));
			ctx = new InitialContext(env);
			UserTransaction tx = getUserTransaction(ctx);
			ControllerRemote cr = (ControllerRemote) ctx.lookup("ejb-project-1.0-SNAPSHOT/Controller!com.test.ControllerRemote");
			try {
				tx.begin();
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
			}
			cr.mainCall();
			try {
				tx.commit();;
			}
			catch(Exception ex1) {
				ex1.printStackTrace();
				throw ex1;
			}
		}
}
public UserTransaction getUserTransaction(Context ctx) throws NamingException {
    return (UserTransaction) ctx.lookup("txn:UserTransaction");
  }

}
