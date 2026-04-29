package com.LSO.cinebox.UI.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<String> notificationText;
    private final MutableLiveData<String> notificationDate;

    public NotificationViewModel() {
        notificationText = new MutableLiveData<>();
        notificationText.setValue("Questa è una notifica");
        notificationDate = new MutableLiveData<>();
        notificationDate.setValue("01/01/2024");
    }

    public LiveData<String> getNotificationText() {
        return notificationText;
    }

    public LiveData<String> getNotificationDate() {
        return notificationDate;
    }
}