package Commands;

import Collection.City;
import Managers.CollectionManager;
import Managers.ConsoleManager;

public class RemoveLowerCommand extends ACommand {
    public RemoveLowerCommand(){
        cmdName = "remove_lower";
        description = "удаляет из коллекции все элементы, меньшие, чем заданный";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {

        City city = consoleManager.getCity();

        int initSize = collectionManager.getCityCollection().size();
        collectionManager.removeLower(city);
        int afterSize = collectionManager.getCityCollection().size();

        consoleManager.writeln("Было удалено " + (initSize - afterSize) + " элементов");
    }
}
