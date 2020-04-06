package commands;

import collection.City;
import managers.CollectionManager;
import managers.ConsoleManager;

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
