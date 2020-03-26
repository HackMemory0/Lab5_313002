package Commands;

import Exceptions.InvalidValueException;
import Managers.CollectionManager;
import Managers.CommandsManager;
import Managers.ConsoleManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecuteScriptCommand extends ACommand {

    public ExecuteScriptCommand(){
        cmdName = "execute_script";
        description = "выполняет команды из скрипта";
        argCount = 1;
    }
//execute_script C:\Users\akper\Desktop\prog\Lab5_313002\script.txt
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        if (args.length < argCount) {
            throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + argCount);
        }

        Path pathToScript = Paths.get(args[0]);
        consoleManager.writeln("Идет выполнение скрипта: " + pathToScript.getFileName());
        int lineNum = 1;
        try {
            ConsoleManager _consoleManager = new ConsoleManager(new FileReader(pathToScript.toFile()), true);
            for (lineNum=1; _consoleManager.hasNextLine(); lineNum++) {
                String line = _consoleManager.read();
                CommandsManager.getInstance().execute(line, _consoleManager, collectionManager);
            }
            //consoleManager.writeln("Скрипт выполнен.");
        } catch (FileNotFoundException e) {
            consoleManager.writeln("Файла скрипта не найден.");
        }catch (Exception ex){
            consoleManager.writeln("\n\t" + ex.getMessage() + "\n\tError on line " + lineNum);
        }catch (StackOverflowError ex){
            consoleManager.writeln("Стек переполнен, выполнение прервано");
        }
    }
}
