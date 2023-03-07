package project;

import com.g3.CPEN431.A7.Model.Distribution.Node;
import com.g3.CPEN431.A7.Model.Distribution.NodesCircle;
import com.g3.CPEN431.A7.Model.KVServer;
import project.Client.UDPClient;
import project.ServerInfo.Server;
import project.Test.OutcomePair;
import project.Test.TestHandler;

import java.io.IOException;
import java.util.ArrayList;


public class TestStopAndResume {

    public static void main( String[] args ) throws IOException, InterruptedException {
        //"/Users/suri/Desktop/CPEN431/test/servers.txt"
        StopAndResumeWithGet(args[0]);
    }

//    public void StopAndResume() throws IOException, InterruptedException {
//        TestHandler testHandler = new TestHandler();
//        UDPClient client = testHandler.clientList.getFirstClient();
//        Server server = testHandler.serverList.getFirstServer();
//
//        // Stage 0
//        OutcomePair outcome0 = testHandler.isAlive(client, server);
//        testHandler.printOutcome(outcome0); // Expect SUCCESS
//        OutcomePair outcome1 = testHandler.getPid(client, server);
//        testHandler.printOutcome(outcome1); // Expect SUCCES
//
//        // Stage 1
//        // TODO: Will this send a shut down request?
//        OutcomePair outcome2 = testHandler.processControlShutDown(client, server);
//        testHandler.printOutcome(outcome2); // Expect SUCCESS
//        Thread.sleep(1000);
//        OutcomePair outcome3 = testHandler.isAlive(client, server);
//        testHandler.printOutcome(outcome3); // Expect TIMEOUT
//
//        // Stage 2
//        OutcomePair outcome4 = testHandler.processControlResume(client, server);
//        testHandler.printOutcome(outcome4); // Expect SUCCESS
//        Thread.sleep(1000);
//        OutcomePair outcome5 = testHandler.isAlive(client, server);
//        testHandler.printOutcome(outcome5); // Expect SUCCESS
//    }

    public static void StopAndResumeWithGet(String serverList) throws IOException, InterruptedException {
        TestHandler testHandler = new TestHandler(serverList, 1);
        UDPClient client = testHandler.clientList.getFirstClient();
        Server server = testHandler.serverList.getFirstServer();

        OutcomePair outcome1 = testHandler.getPid(client, server);
        testHandler.printOutcome(outcome1); // Expect SUCCESS

        OutcomePair outcome2 = testHandler.put(client, server, "K1", "1", 0);
        testHandler.printOutcome(outcome2); // EXPECT [K-N-F, NULL], [TIMEOUT, NULL], [SUCCESS, NULL]
        OutcomePair outcome3 = testHandler.get(client, server, "K1");
        testHandler.printOutcome(outcome3); // EXPECT GET(K1) -> [TIMEOUT, NULL], [K-N-F, NULL]

        OutcomePair outcome4 = testHandler.processControlShutDown(client, server);
        Thread.sleep(1000);
        testHandler.printOutcome(outcome4); // Expect TIMEOUT
        OutcomePair outcome5 = testHandler.get(client, server, "K1");
        testHandler.printOutcome(outcome5); // EXPECT TIMEOUT


        OutcomePair outcome6 = testHandler.processControlResume(client, server);
        Thread.sleep(1000);
        testHandler.printOutcome(outcome6); // Expect TIMEOUT
        OutcomePair outcome7 = testHandler.get(client, server, "K1");
        testHandler.printOutcome(outcome7); // EXPECT GET(K1) -> [TIMEOUT, NULL], [K-N-F, NULL]
    }
 }
