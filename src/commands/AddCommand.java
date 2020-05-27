package commands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import models.City;
import managers.CollectionManager;
import managers.ConsoleManager;

import java.time.LocalDate;

public class AddCommand extends AbstractCommand {

    public AddCommand(){
        cmdName = "add";
        description = "добавляет новый элемент в коллекцию";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCity();
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(inputData == null)
            throw new InvalidValueException("Данные не найдены");

        String cityID = databaseController.addCity((City) inputData, credentials);
        if (isNumeric(cityID)) {
            ((City) inputData).setId(Long.valueOf(cityID));
            collectionManager.addElement((City) inputData);
            consoleManager.writeln("Добавлена новая запись.");
        }

        inputData = null;
    }
}
