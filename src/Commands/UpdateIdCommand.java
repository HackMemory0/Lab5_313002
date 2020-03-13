package Commands;

import Collection.City;
import Exceptions.InvalidValueException;
import Managers.CollectionManager;
import Managers.ConsoleManager;

public class UpdateIdCommand extends ACommand {
    public UpdateIdCommand(){
        cmdName = "update";
        description = "обновляет значение элемента коллекции, id которого равен заданному";
        argCount = 1;
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        if (args.length < 1) {
            throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + argCount);
        }

        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return;
        }

        if(!collectionManager.checkIdExist(id))
            throw new InvalidValueException("Такого id не существует");

        City city = consoleManager.getCity();
        collectionManager.update(city, id);
        consoleManager.writeln("Элемент с id - " + id + " был изменен");
    }
}
