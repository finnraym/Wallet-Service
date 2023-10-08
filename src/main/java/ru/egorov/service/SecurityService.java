package ru.egorov.service;

import ru.egorov.model.Player;

public interface SecurityService {
    Player register(String login, String password);
    Player authorization(String login, String password);
}
