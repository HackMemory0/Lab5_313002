package Commands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class ExitCommand extends ACommand {

    public ExitCommand(){
        cmdName = "exit";
        description = "выход из программы без сохранения";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        if(consoleManager.getIsScript()) System.exit(1);

        String out = consoleManager.readWithMessage("Вы уверены? Данные не сохраняются (Y/N)", false);
        if(out.toLowerCase().equals("y")) System.exit(1);
    }
}
