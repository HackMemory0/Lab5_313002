package Managers;

import Commands.*;
import Exceptions.NoCommandException;
import java.util.*;

/**
 * Менеджер команд
 */
public class CommandsManager {
    private static CommandsManager instance;

    public static CommandsManager getInstance() {
        if (instance == null) {
            instance = new CommandsManager();
        }
        return instance;
    }

    private Map<String, ACommand> commands = new HashMap<>();

    public CommandsManager(){
        addCommand(new AddCommand());
        addCommand(new HelpCommand());
        addCommand(new ExitCommand());
        addCommand(new ShowCommand());
        addCommand(new InfoCommand());
        addCommand(new UpdateIdCommand());
        addCommand(new RemoveIdCommand());
        addCommand(new ClearCommad());
        addCommand(new ShuffleCommand());
        addCommand(new AddIfMinCommand());
        addCommand(new CountLessThanGovernor());
        addCommand(new FilterContainsNameCommand());
        addCommand(new RemoveLowerCommand());
        addCommand(new PrintFieldAscendingTimezoneCommand());
        addCommand(new SaveCommand());
        addCommand(new ExecuteScriptCommand());
    }

    private void addCommand(ACommand cmd){
        commands.put(cmd.getCmdName(), cmd);
    }

    public ACommand getCommand(String s) throws NoCommandException {
        if (!commands.containsKey(s)) {
            throw new NoCommandException("Команда не найдена");
        }
        return commands.get(s);
    }

    /**
     * Выполняет команды введенные пользователем
     * @param str
     * @param consoleManager
     * @param collectionManager
     */
    public void execute(String str, ConsoleManager consoleManager, CollectionManager collectionManager){
        String[] parse = str.trim().split(" ");
        if(!parse[0].equals("")) {
            ACommand cmd = getCommand(parse[0].toLowerCase());
            //consoleManager.writeln("execution cmd: " + parse[0]);

            String[] args = Arrays.copyOfRange(parse, 1, parse.length);
            //consoleManager.writeln("args count: " + args.length);

            cmd.execute(consoleManager, collectionManager, args);
        }
    }


    public List<ACommand> getAllCommands() {
        return new ArrayList<>(commands.values());
    }
}
