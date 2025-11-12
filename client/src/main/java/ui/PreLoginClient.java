package ui;

import Server.ServerFacade;

public class PreLoginClient {
    private final ServerFacade server;

    public PreLoginClient(ServerFacade server) {
        this.server = server;
    }

public void run() {
    System.out.println(LOGO + " Welcome to the Chess");
    System.out.print(help());
}


}
