package com.LSO.cinebox.Model;

import com.LSO.cinebox.Entity.Film;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Film> cart;

    private CartManager() { cart = new ArrayList<>(); }

    public static synchronized CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public synchronized void addFilm(Film film) {
        if (!cart.contains(film)) cart.add(film);
    }

    public synchronized void removeFilm(Film film) {
        cart.remove(film);
    }

    public synchronized List<Film> getCart() {
        return new ArrayList<>(cart);
    }

    public synchronized double getTotalPrice() {
        double total = 0;
        for (Film film : cart) total += film.getPrezzo();
        return total;
    }

    public synchronized boolean isEmpty() { return cart.isEmpty(); }

    public synchronized void clearCart() { cart.clear(); }
}
