package ru.ifmo.lab.ui;

import ru.ifmo.lab.database.CurrentUser;

public interface NetworkListener {

    void onLogin(CurrentUser user);
}
