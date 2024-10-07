package ggs.srr.oauth.client;

import java.util.ArrayList;
import java.util.List;


public class ClientManager {

    private List<Client> clientStorage = new ArrayList<>();

    public Client getClient(String name){
        for(Client client : clientStorage){
            if (client.getName().equals(name))
                return client;
        }
        return null;
    }

    public Client addClient(Client client){
        clientStorage.add(client);
        return client;
    }

    public List<Client> getClients(){
        return this.clientStorage;
    }
}
