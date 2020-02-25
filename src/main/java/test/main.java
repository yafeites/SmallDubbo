package test;

import app.Client;
import app.Server;
import framework.balance.strategy.Index;
import test.inter.Person;

import java.util.HashMap;
import java.util.Map;

public class main {

    public static void main(String[] args)  {

        Server server=new Server();
        server.setProviderProperties("/service.properties");
        Client client=new Client();
        Person p=(Person)client.setReferenceProperties("/invoker.properties");
       System.out.println(p.cnt());
    }
}
