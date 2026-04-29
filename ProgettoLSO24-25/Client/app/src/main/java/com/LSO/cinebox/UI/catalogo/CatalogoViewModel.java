package com.LSO.cinebox.UI.catalogo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.LSO.cinebox.Entity.Film;

import java.util.List;

public class CatalogoViewModel extends ViewModel {
    private final MutableLiveData<List<Film>> filmList = new MutableLiveData<>();

    public LiveData<List<Film>> getFilmList() { return filmList; }

    public void updateFilmList(List<Film> newFilms) {
        List<Film> current = filmList.getValue();
        if (current == null || !current.equals(newFilms)) {
            filmList.setValue(newFilms);
        }
    }
}
