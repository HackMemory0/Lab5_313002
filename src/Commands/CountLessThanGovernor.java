package Commands;

import Collection.Human;
import Managers.CollectionManager;
import Managers.ConsoleManager;

public class CountLessThanGovernor extends ACommand {
    public CountLessThanGovernor(){
        cmdName = "count_less_than_governor";
        description = "выводит количество элементов, значение поля governor которых меньше заданного";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        Human human = consoleManager.getHuman();
        long count = collectionManager.countLessThanGovernor(human);
        consoleManager.writeln("Кол-во элементов, которые меньше заданого governor: " + count);
    }
}
