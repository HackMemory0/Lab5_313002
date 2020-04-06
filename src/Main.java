import java.io.IOException;
import java.io.InputStreamReader;

import exceptions.InvalidValueException;
import exceptions.NoCommandException;
import managers.CollectionManager;
import managers.CommandsManager;
import managers.ConsoleManager;


/**
 * Главный класс приложения
 */
public class Main {

    public static void main(String[] args) throws IOException {

        ConsoleManager consoleManager = new ConsoleManager(new InputStreamReader(System.in),false);
        consoleManager.writeln("CLI версия 0.1");
        consoleManager.writeln("Используйте help для получения справки");

        CollectionManager collectionManager = new CollectionManager(System.getenv("LAB5PATH"));

        while (true) {
            consoleManager.write("> ");
            if (consoleManager.hasNextLine()) {
                String cmd = consoleManager.read();

                try {
                    CommandsManager.getInstance().execute(cmd, consoleManager, collectionManager);
                }catch (NoCommandException ex) {
                    consoleManager.writeln("Такая команда не найдена :(\nВведите команду help, чтобы вывести спискок команд");
                }catch (NumberFormatException|ClassCastException ex){
                    consoleManager.writeln("Ошибка во время каста\n"+ex.getMessage());
                } catch (InvalidValueException ex){
                    consoleManager.writeln(ex.getMessage());
                }
            }
        }

    }

}
