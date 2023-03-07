package project.Test;

import ca.NetSysLab.ProtocolBuffers.KeyValueRequest;
import project.Client.MessageBuilder;
import project.Client.UDPClient;
import project.Client.UDPClientList;
import project.ServerInfo.Server;
import project.ServerInfo.ServerList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static project.Client.MessageBuilder.buildKVRequest;


public class TestHandler {

    public ServerList serverList;
    public UDPClientList clientList;


    public TestHandler (String serverList, int numClient) throws IOException, InterruptedException {
        this.serverList = new ServerList(serverList);
        this.clientList = new UDPClientList(numClient);
    }

    public void printOutcome(OutcomePair outcomePair) {
        System.out.println("[ Received response: ]");
        System.out.println("[ " + outcomePair.getStatus() + ", " + outcomePair.getValue() + "]");
    }
    public OutcomePair put(UDPClient client, Server server, String key, String val, int version) throws IOException, InterruptedException {
        KeyValueRequest.KVRequest request = MessageBuilder.buildKVRequest(MessageBuilder.Commands.PUT, key, val, version);
        return client.run(server, request);
    }

    public OutcomePair get(UDPClient client, Server server, String key) throws IOException, InterruptedException {
        KeyValueRequest.KVRequest request = MessageBuilder.buildKVRequest(MessageBuilder.Commands.GET, key);
        return client.run(server, request);
    }

    public OutcomePair remove(UDPClient client, Server server, String key) throws IOException, InterruptedException {
        KeyValueRequest.KVRequest request = MessageBuilder.buildKVRequest(MessageBuilder.Commands.REMOVE, key);
        return client.run(server, request);
    }

    public OutcomePair shutdown(UDPClient client, Server server) throws IOException, InterruptedException {
        KeyValueRequest.KVRequest request = MessageBuilder.buildKVRequest(MessageBuilder.Commands.SHUTDOWN);
        return client.run(server, request); // retry 3 times, should receive timeout
    }

    public OutcomePair isAlive(UDPClient client, Server server) throws IOException, InterruptedException {
        KeyValueRequest.KVRequest request = MessageBuilder.buildKVRequest(MessageBuilder.Commands.IS_ALIVE);
        return client.run(server, request); // should receive success
    }

    public OutcomePair wipeOut(UDPClient client, Server server) throws IOException, InterruptedException {
        KeyValueRequest.KVRequest request = MessageBuilder.buildKVRequest(MessageBuilder.Commands.WIPE_OUT);
        return client.run(server, request); // should receive success
    }

    public OutcomePair getPid(UDPClient client, Server server) throws IOException, InterruptedException {
        KeyValueRequest.KVRequest request = MessageBuilder.buildKVRequest(MessageBuilder.Commands.GET_PID);
        return client.run(server, request);
    }

    void runCommand(String cmd) throws IOException {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            System.out.println("err1");
            //Logger.getLogger(Documents.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.out.println("err2");
            // Logger.getLogger(Documents.class.getName()).log(Level.SEVERE, null, ex);
        }
        br.close();
        System.out.println("[ Run command: " + cmd + " ]");
    }

    public OutcomePair processControlShutDown(UDPClient client, Server server) throws IOException, InterruptedException {
        int pid = server.getPid();
        assert (pid != 0);
        String cmd = "kill -STOP " + pid;
        runCommand(cmd);
        //new ProcessBuilder().command(cmd).start();


        //Process proc = Runtime.getRuntime().exec(cmd);
//        BufferedReader reader =
//                new BufferedReader(new InputStreamReader(proc.getInputStream()));
//        while ((reader.readLine()) != null) {}
//        proc.waitFor();
        //TODO
        //KeyValueRequest.KVRequest request = buildKVRequest(Commands.SHUTDOWN);
        //return client.run(server, request);
        return new OutcomePair(OutcomePair.Status.PROCESSCONTROL, "Shut down");
    }

    public OutcomePair processControlResume(UDPClient client, Server server) throws IOException, InterruptedException {
        int pid = server.getPid();
        assert (pid != 0);
        String cmd = "kill -CONT " + pid;
        runCommand(cmd);
        //new ProcessBuilder().command(cmd).start();
        //Process proc = Runtime.getRuntime().exec(cmd);
//        BufferedReader reader =
//                new BufferedReader(new InputStreamReader(proc.getInputStream()));
//        while ((reader.readLine()) != null) {}
//        proc.waitFor();
//        KeyValueRequest.KVRequest request = buildKVRequest(Commands.IS_ALIVE); //TODO
//        return client.run(server, request);
        return new OutcomePair(OutcomePair.Status.PROCESSCONTROL, "Resume");
    }



}
