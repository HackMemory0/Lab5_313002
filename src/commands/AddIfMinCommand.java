package commands;

import collection.City;
import managers.CollectionManager;
import managers.ConsoleManager;

public class AddIfMinCommand extends ACommand {

    public AddIfMinCommand(){
        cmdName = "add_if_min";
        description = "добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        City city = consoleManager.getCity();
        Boolean res = collectionManager.addIfMin(city);

        if(res)
            consoleManager.writeln("Элемент был добавлен");
        else
            consoleManager.writeln("Элемент не был добавлен. Элемент оказался больше минимального");
    }
}
