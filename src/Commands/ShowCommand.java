package Commands;

import Collection.City;
import Managers.CollectionManager;
import Managers.ConsoleManager;

public class ShowCommand extends ACommand {

    public ShowCommand(){
        cmdName = "show";
        description = "выводит все элементы коллекции";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        consoleManager.writeln("Количество элементов в коллекции: " + collectionManager.getCityCollection().size());
        StringBuilder sb = new StringBuilder();
        collectionManager.getCityCollection().forEach(s -> sb.append(s).append("\n"));

        consoleManager.writeln(sb.toString());
    }
}
