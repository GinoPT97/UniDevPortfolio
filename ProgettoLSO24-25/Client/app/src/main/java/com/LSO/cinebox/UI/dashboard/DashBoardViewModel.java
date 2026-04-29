package com.LSO.cinebox.UI.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DashBoardViewModel extends ViewModel {
    private final MutableLiveData<String> dashboardTitle;
    private final MutableLiveData<List<String>> last5Rentals;
    private final MutableLiveData<List<String>> top5RentedFilms;
    private final MutableLiveData<List<String>> allRentalsOverview;
    private final MutableLiveData<List<String>> activeRentals;

    public DashBoardViewModel() {
        dashboardTitle = new MutableLiveData<>();
        last5Rentals = new MutableLiveData<>();
        top5RentedFilms = new MutableLiveData<>();
        allRentalsOverview = new MutableLiveData<>();
        activeRentals = new MutableLiveData<>();
    }

    public LiveData<String> getDashboardTitle() {
        return dashboardTitle;
    }

    public LiveData<List<String>> getLast5Rentals() {
        return last5Rentals;
    }

    public LiveData<List<String>> getTop5RentedFilms() {
        return top5RentedFilms;
    }

    public LiveData<List<String>> getAllRentalsOverview() {
        return allRentalsOverview;
    }

    public LiveData<List<String>> getActiveRentals() {
        return activeRentals;
    }

    public void updateLast5Rentals(List<String> rentals) {
        last5Rentals.setValue(rentals);
    }

    public void updateTop5RentedFilms(List<String> films) {
        top5RentedFilms.setValue(films);
    }

    public void updateAllRentalsOverview(List<String> rentals) {
        allRentalsOverview.setValue(rentals);
    }

    public void updateActiveRentals(List<String> rentals) {
        activeRentals.setValue(rentals);
    }
}