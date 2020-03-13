package Commands;

import Collection.City;
import Exceptions.InvalidValueException;
import Managers.CollectionManager;
import Managers.ConsoleManager;

import java.util.ArrayList;

public class FilterContainsNameCommand extends ACommand {
    public FilterContainsNameCommand(){
        cmdName = "filter_contains_name";
        description = "выводит элементы, значение поля name которых содержит заданную подстроку";
        argCount = 1;
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        if (args.length < argCount) {
            throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + argCount);
        }

        ArrayList<City> out = collectionManager.findByName(args[0]);
        if(out.size() == 0) consoleManager.writeln("Элементы с таким именем не найден");
        out.forEach(x-> consoleManager.writeln(x.toString()));
    }
}
