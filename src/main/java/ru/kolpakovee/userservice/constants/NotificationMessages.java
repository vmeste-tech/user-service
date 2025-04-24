package ru.kolpakovee.userservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationMessages {
    public final String CREATE_APARTMENT = "Квартира создана! Добро пожаловать домой 🏠";
    public final String ADD_TO_APARTMENT = "К вашей квартире присоединился новый участник.";
    public final String UPDATE_APARTMENT = "Информация о квартире обновлена. Актуальность — наше всё.";
    public final String DELETE_APARTMENT = "Квартира удалена. Надеемся, это было осознанное решение.";
    public final String REMOVE_FROM_APARTMENT = "Один из участников покинул квартиру.";
    public final String CREATE_INVITE = "Приглашение создано. Делитесь ссылкой с соседями!";
    public final String USE_INVITE = "Приглашение принято! Добро пожаловать в команду 🚪";
    public final String REGISTER_USER = "Регистрация прошла успешно. Рады видеть вас!";
    public final String PASSWORD_UPDATE = "Пароль обновлён. Безопасность — превыше всего 🔐";
    public static final String PROFILE_UPDATE = "Ваш профиль был обновлен";
}

