package commands;

import models.City;
import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;

public class UpdateIdCommand extends AbstractCommand {
    public UpdateIdCommand(){
        cmdName = "update";
        description = "обновляет значение элемента коллекции, id которого равен заданному";
        argCount = 1;
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCity();
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        if (args.length < 1) {
            throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + argCount);
        }

        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (Exception e) {
            throw new InvalidValueException(e.getMessage());
        }

        if(!collectionManager.checkIdExist(id))
            throw new InvalidValueException("Такого id не существует");

        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        collectionManager.update((City) inputData, id);
        consoleManager.writeln("Элемент с id - " + id + " был изменен");
    }
}
