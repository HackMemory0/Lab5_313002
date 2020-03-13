package Commands;

import Collection.City;
import Managers.CollectionManager;
import Managers.ConsoleManager;

public class AddCommand extends ACommand {

    public AddCommand(){
        cmdName = "add";
        description = "добавляет новый элемент в коллекцию";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        City city = consoleManager.getCity();
        collectionManager.addElement(city);

        consoleManager.writeln("Добавлена новая запись.");
    }
}
