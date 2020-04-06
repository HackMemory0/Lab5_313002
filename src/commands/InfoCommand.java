package commands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class InfoCommand extends ACommand {

    public InfoCommand(){
        cmdName = "info";
        description = "выводит информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        consoleManager.writeln("Тип: " + collectionManager.getCityCollection().getClass().getName());
        consoleManager.writeln("Кол-во элементов: " + collectionManager.getCityCollection().size());
        consoleManager.writeln("Дата инициализации: " + collectionManager.getInitDate().toString());
    }
}
