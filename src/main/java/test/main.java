package test;

import app.Client;
import app.Server;
import test.inter.Person;

public class main {

    public static void main(String[] args)  {
        Server server=new Server();
        server.setProviderProperties("/service.properties");
        Client client=new Client();
       Person p=(Person)client.setReferenceProperties("/invoker.properties");
       System.out.println(p.cnt());
    }
}
