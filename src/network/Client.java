package network;

import commands.ACommand;
import commands.ExecuteScriptCommand;
import commands.ExitCommand;
import commands.SaveCommand;
import exceptions.InvalidValueException;
import exceptions.NoCommandException;
import lombok.extern.slf4j.Slf4j;
import managers.CommandsManager;
import managers.ConsoleManager;
import network.packets.*;
import utils.AppConstant;
import utils.Serializer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@Slf4j
public class Client {
    protected DatagramSocket socket;
    protected SocketAddress addressServer;
    private InetAddress IPAddress;
    private int PORT;
    private ByteBuffer inBuff = ByteBuffer.allocate(AppConstant.MESSAGE_BUFFER);
    private UserPacket userPacket;
    private ConsoleManager consoleManager;
    private boolean isConnected = false;
    private int tryConnect = 5;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        new Client(args).run();
    }

    public Client(String[] args) throws IOException {
        connect(args);
    }

    private void connect(String host, int port) throws IOException {
        //channel = DatagramChannel.open();
        //channel.configureBlocking(false);
        //channel.bind(null);
        socket = new DatagramSocket();
        socket.setSoTimeout(5000);
        addressServer = new InetSocketAddress(host, port);
        IPAddress = InetAddress.getByName( host );
        PORT = port;
    }

    private void connect(String[] args) throws IOException {
        if (args.length >= 2) {
            connect(args[0], Integer.parseInt(args[1]));
        } else if (args.length == 1) {
            String[] hostAndPort = args[0].split(":");
            if (hostAndPort.length != 2) {
                throw new InvalidValueException("");
            }
            connect(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
        } else {
            connect("localhost", AppConstant.DEFAULT_PORT);
        }
    }

    public void run() throws IOException, ClassNotFoundException, InterruptedException {
        consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(System.out), false);

        while (true) {
            consoleManager.write("Введите имя: ");
            if (consoleManager.hasNextLine()) {
                String str = consoleManager.read();
                if (!str.isEmpty()) {
                    this.userPacket = new UserPacket(str);
                    break;
                }
            }
        }

        while(!isConnected) {
            send(new LoginPacket(this.userPacket));
            consoleManager.writeln("Waiting to connect to the server...");
            objectHandler(recv());
        }



        tryConnect = 1;
        while (isConnected) {
            consoleManager.write("> ");
            if (consoleManager.hasNextLine()) {
                sendCommand(consoleManager.read(), consoleManager);
            }
        }
    }

    private void sendCommand(String sCmd, ConsoleManager cMgr) throws IOException, ClassNotFoundException {
        if(sCmd.isEmpty()) return;
        try {
            ACommand cmd = CommandsManager.getInstance().parseCommand(sCmd);
            if(cmd instanceof ExitCommand){ send(new LogoutPacket(this.userPacket)); socket.disconnect(); isConnected = false; }
            else if(cmd instanceof SaveCommand){ cMgr.writeln("Команда не работает в клиентской части."); }
            else if(cmd instanceof ExecuteScriptCommand){
                Path pathToScript = Paths.get(cmd.getArgs()[0]);
                int lineNum = 1;
                try {
                    ConsoleManager _consoleManager = new ConsoleManager(new FileReader(pathToScript.toFile()), new OutputStreamWriter(System.out), true);
                    for (lineNum=1; _consoleManager.hasNextLine(); lineNum++) {
                        String line = _consoleManager.read().trim();
                        if(!line.isEmpty()) { sendCommand(line, _consoleManager); }
                    }
                } catch (FileNotFoundException e) {
                    cMgr.writeln("Файла скрипта не найден.");
                }catch (Exception ex){
                    consoleManager.writeln("\n\t" + ex.getMessage() + "\n\tError on line " + lineNum);
                }catch (StackOverflowError ex){
                    consoleManager.writeln("Стек переполнен, выполнение прервано");
                }

            }
            else {
                if (cmd.getNeedInput()) cmd.setInputData(cmd.getInput(cMgr));
                send(cmd);
                objectHandler(recv());
                //cMgr.writeln(recv().toString());
            }
        }catch (NoCommandException ex) {
            cMgr.writeln("Такая команда не найдена :(\nВведите команду help, чтобы вывести спискок команд");
            log.error(ex.getMessage());
        }catch (NumberFormatException|ClassCastException ex){
            cMgr.writeln("Ошибка во время каста\n" + ex.getMessage());
            log.error(ex.getMessage());
        } catch (InvalidValueException ex){
            cMgr.writeln(ex.getMessage());
            log.error(ex.getMessage());
        }
    }


    private void objectHandler(Object obj){
        if(obj != null) {
            if (obj instanceof LoginSuccessPacket) {
                isConnected = true;
                consoleManager.writeln(((LoginSuccessPacket) obj).getMessage());
            } else if (obj instanceof LoginFailedPacket) {
                isConnected = false;
                consoleManager.writeln(((LoginFailedPacket) obj).getMessage());
            } else if (obj instanceof CommandExecutionPacket) {
                consoleManager.writeln(((CommandExecutionPacket) obj).getMessage());
            }
        }
    }

    private void send(Object obj) throws IOException {

        byte[] data = Serializer.Serialize(obj);
        final ByteBuffer buf = ByteBuffer.wrap(data);

        try {
            DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length, IPAddress, PORT);
            socket.send(packet);
        }catch (IOException ex){
            consoleManager.writeln(ex.getMessage());
        }

    }

    private Object recv() throws ClassNotFoundException, IOException {
        Object out = null;
        try {
            byte[] receiveData = new byte[AppConstant.MESSAGE_BUFFER];
            DatagramPacket received = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(received);
            if (received.getLength() != 0) {
            /*inBuff = ByteBuffer.wrap(received.getData());
            //inBuff.flip();


            int limits = inBuff.limit();
            byte bytes[] = new byte[limits];
            inBuff.get(bytes, 0, limits);

            out = Serializer.Deserialize(bytes);
            inBuff.clear();*/
                out = Serializer.Deserialize(received.getData());
            }

        }catch (SocketTimeoutException ex){
            if(tryConnect == 0) System.exit(1);

            tryConnect--;
            consoleManager.writeln("Failed to connect");
        }

        return out;
    }

}
