package Commands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class PrintFieldAscendingTimezoneCommand extends ACommand {

    public PrintFieldAscendingTimezoneCommand(){
        cmdName = "print_field_ascending_timezone";
        description = "выводит значения поля timezone всех элементов в порядке возрастания";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        collectionManager.sortByTimezone().forEach(x->consoleManager.writeln(x.toString()));
    }
}
