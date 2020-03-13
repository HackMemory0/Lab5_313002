package Commands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

/**
 * Абстрактный класс для команд
 */
public abstract class ACommand {
    int argCount = 0;
    String cmdName;
    String description;

    /**
     *
     * @param consoleManager управление консолью
     * @param collectionManager управление коллекцией
     * @param args аргументы, которые ввел пользователь в консоле
     */
    public abstract void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args);


    public String getCmdName() {
        return cmdName;
    }

    public String getDescription() {
        return description;
    }
}
