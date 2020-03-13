package Commands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class ClearCommad extends ACommand {
    public ClearCommad(){
        cmdName = "clear";
        description = "очищает коллекцию";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        collectionManager.clear();
        consoleManager.writeln("Коллекция была очищена");
    }
}
