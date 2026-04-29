package com.LSO.cinebox.UI.carrello;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.LSO.cinebox.Entity.Film;
import com.LSO.cinebox.Model.CartManager;

import java.util.List;

public class CarrelloViewModel extends ViewModel {
    private final MutableLiveData<List<Film>> carrelloList = new MutableLiveData<>(CartManager.getInstance().getCart());

    public LiveData<List<Film>> getCarrelloList() { return carrelloList; }

    public void updateCarrelloList() {
        carrelloList.setValue(CartManager.getInstance().getCart());
    }
}