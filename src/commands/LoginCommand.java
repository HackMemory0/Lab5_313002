package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class LoginCommand extends AbstractCommand {

    public LoginCommand(){
        cmdName = "login";
        description = "вход пользователя в систему, для управления им его данными";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCredentials();
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);

        consoleManager.writeln(inputData.toString());

        inputData = null;
    }
}
