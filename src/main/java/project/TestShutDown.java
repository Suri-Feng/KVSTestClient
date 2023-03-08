package project;

import project.ServerInfo.Server;
import project.Test.OutcomePair;
import project.Test.TestHandler;


import project.Client.UDPClient;

import java.io.IOException;


public class TestShutDown {

    public void ShutDown(String serverList) throws IOException, InterruptedException {
        TestHandler testHandler = new TestHandler(serverList, 1);
        UDPClient client = testHandler.clientList.getFirstClient();
        Server server = testHandler.serverList.getFirstServer();

        OutcomePair outcome0 = testHandler.isAlive(client, server);
        testHandler.printOutcome(outcome0); // Expect SUCCESS

        Thread.sleep(1000);

        OutcomePair outcome1 = testHandler.shutdown(client, server);
        testHandler.printOutcome(outcome1); // Expect TIMEOUT

        Thread.sleep(1000);

        OutcomePair outcome2 = testHandler.isAlive(client, server);
        testHandler.printOutcome(outcome2); // Expect TIMEOUT
    }
}
