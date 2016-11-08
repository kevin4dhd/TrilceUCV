package com.company.proyect.kevinpiazzoli.trilceucv.notifications;

import com.company.proyect.kevinpiazzoli.trilceucv.Interfaces.BasePresenter;
import com.company.proyect.kevinpiazzoli.trilceucv.Interfaces.BaseView;
import com.company.proyect.kevinpiazzoli.trilceucv.data.PushNotification;

import java.util.ArrayList;

/**
 * Interacción MVP en Notificaciones
 */
public interface PushNotificationContract {

    interface View extends BaseView<Presenter>{

        void showNotifications(ArrayList<PushNotification> notifications);

        void showEmptyState(boolean empty);

        void popPushNotification(PushNotification pushMessage);
    }

    interface Presenter extends BasePresenter{

        void registerAppClient();

        void loadNotifications();

        void savePushMessage(String title, String description, String expiryDate);
    }
}
