package commands;

import managers.CollectionManager;
import managers.CommandsManager;
import managers.ConsoleManager;

import java.util.List;

public class HelpCommand extends ACommand {

    public HelpCommand(){
        cmdName = "help";
        description = "выводит список команд и их описание";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {

        List<ACommand> aCommands = CommandsManager.getInstance().getAllCommands();
        for (ACommand cmd: aCommands){
            consoleManager.writeln(cmd.getCmdName() + " - " + cmd.getDescription() );
        }
    }
}
