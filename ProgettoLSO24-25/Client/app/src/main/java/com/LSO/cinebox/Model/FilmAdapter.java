package com.LSO.cinebox.Model;

import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.LSO.cinebox.Entity.Film;
import com.LSO.cinebox.MainActivity;
import com.LSO.cinebox.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private final List<Film> filmList;
    private final List<Film> cart;
    private final Runnable updateCarrello;
    private final boolean isGrid;
    private final MainActivity mainActivity;
    private final OnFilmClickListener onFilmClickListener;
    private String highlightQuery = "";

    public interface OnFilmClickListener { void onFilmClick(Film film); }

    public FilmAdapter(List<Film> filmList, List<Film> cart, Runnable updateCarrello, boolean isGrid, MainActivity mainActivity, OnFilmClickListener onFilmClickListener) {
        this.filmList = filmList;
        this.cart = cart;
        this.updateCarrello = updateCarrello;
        this.isGrid = isGrid;
        this.mainActivity = mainActivity;
        this.onFilmClickListener = onFilmClickListener;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(isGrid ? R.layout.item_cart_film : R.layout.item_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = filmList.get(position);

        holder.textViewTitolo.setText(film.getTitolo());
        holder.textViewPrezzo.setText("€" + String.format("%.2f", film.getPrezzo()));

        if (isGrid) {
            holder.textViewBadgeDisponibilita.setText(film.getNumeroCopieDisponibili() > 0 ? "Disponibile" : "Non disponibile");
            holder.textViewBadgeDisponibilita.setBackgroundResource(R.drawable.badge_bg);
            holder.textViewBadgeDisponibilita.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(
                holder.itemView.getContext(),
                film.getNumeroCopieDisponibili() > 0 ? R.color.primary : R.color.red
            )));
            holder.buttonRemoveFromCart.setVisibility(View.VISIBLE);
            holder.buttonRemoveFromCart.setOnClickListener(v -> {
                CartManager.getInstance().removeFilm(film);
                updateCarrello.run();
                Toast.makeText(holder.itemView.getContext().getApplicationContext(), "Film rimosso dal carrello", Toast.LENGTH_SHORT).show();
            });
            if (holder.buttonAddToCart != null) holder.buttonAddToCart.setVisibility(View.GONE);
            if (holder.textViewAnno != null) holder.textViewAnno.setVisibility(View.GONE);
            if (holder.textViewVotoMedio != null) holder.textViewVotoMedio.setVisibility(View.GONE);
            Picasso.get().load(film.getLocandinaUrl()).placeholder(R.drawable.generic_image).fit().centerCrop().into(holder.imageViewLocandina);
        } else {
            String anno = film.getDataRilascio() != null && film.getDataRilascio().length() >= 4 ? film.getDataRilascio().substring(0, 4) : "";
            holder.textViewAnno.setText(anno);
            holder.textViewBadgeDisponibilita.setText(film.getNumeroCopieDisponibili() > 0 ? "Disponibile" : "Non disponibile");
            holder.textViewBadgeDisponibilita.setBackgroundResource(R.drawable.badge_bg);
            holder.textViewBadgeDisponibilita.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(
                holder.itemView.getContext(),
                film.getNumeroCopieDisponibili() > 0 ? R.color.primary : R.color.red
            )));
            holder.textViewVotoMedio.setText("★ " + String.format("%.1f", film.getVotoMedio()) + "/10");
            holder.textViewVotoMedio.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
            holder.textViewVotoMedio.setBackgroundResource(R.drawable.badge_bg);
            holder.textViewVotoMedio.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange)));
            Picasso.get().load(film.getLocandinaUrl()).placeholder(R.drawable.generic_image).fit().centerCrop().into(holder.imageViewLocandina);

            int userId = mainActivity.getUserId();
            if (userId == 0) {
                holder.buttonAddToCart.setVisibility(View.GONE);
                holder.buttonRemoveFromCart.setVisibility(View.GONE);
            } else {
                if (cart.contains(film)) {
                    holder.buttonAddToCart.setVisibility(View.GONE);
                    holder.buttonRemoveFromCart.setVisibility(View.VISIBLE);
                    holder.buttonRemoveFromCart.setOnClickListener(v -> {
                        CartManager.getInstance().removeFilm(film);
                        updateCarrello.run();
                        Toast.makeText(holder.itemView.getContext().getApplicationContext(), "Film rimosso dal carrello", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    holder.buttonAddToCart.setVisibility(View.VISIBLE);
                    holder.buttonRemoveFromCart.setVisibility(View.GONE);
                    holder.buttonAddToCart.setOnClickListener(v -> {
                        CartManager.getInstance().addFilm(film);
                        updateCarrello.run();
                        Toast.makeText(holder.itemView.getContext().getApplicationContext(), "Film aggiunto al carrello", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            holder.itemView.setOnClickListener(v -> {
                if (onFilmClickListener != null) onFilmClickListener.onFilmClick(film);
            });

            if (highlightQuery != null && !highlightQuery.isEmpty()) {
                String titolo = film.getTitolo();
                int start = titolo.toLowerCase().indexOf(highlightQuery.toLowerCase());
                if (start >= 0) {
                    SpannableString spannable = new SpannableString(titolo);
                    spannable.setSpan(new BackgroundColorSpan(0xFFFFFF00), start, Math.min(start + highlightQuery.length(), titolo.length()), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.textViewTitolo.setText(spannable);
                } else {
                    holder.textViewTitolo.setText(titolo);
                }
            } else {
                holder.textViewTitolo.setText(film.getTitolo());
            }
        }
    }

    public void setHighlightQuery(String query) { this.highlightQuery = query; }

    public void setFilms(List<Film> films) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override public int getOldListSize() { return filmList.size(); }
            @Override public int getNewListSize() { return films.size(); }
            @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return filmList.get(oldItemPosition).getIdFilm() == films.get(newItemPosition).getIdFilm();
            }
            @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return filmList.get(oldItemPosition).equals(films.get(newItemPosition));
            }
        });
        filmList.clear();
        filmList.addAll(films);
        diffResult.dispatchUpdatesTo(this);
    }

    public Film getFilmAtPosition(int position) { return filmList.get(position); }
    @Override public int getItemCount() { return filmList.size(); }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewTitolo;
        final TextView textViewPrezzo;
        final TextView textViewBadgeDisponibilita;
        final TextView textViewAnno;
        final TextView textViewVotoMedio;
        final ImageView imageViewLocandina;
        final Button buttonAddToCart;
        final Button buttonRemoveFromCart;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitolo = itemView.findViewById(R.id.textViewTitolo);
            textViewPrezzo = itemView.findViewById(R.id.textViewPrezzo);
            imageViewLocandina = itemView.findViewById(R.id.imageViewLocandina);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
            buttonRemoveFromCart = itemView.findViewById(R.id.buttonRemoveFromCart);
            textViewBadgeDisponibilita = itemView.findViewById(R.id.textViewBadgeDisponibilita);
            textViewAnno = itemView.findViewById(R.id.textViewAnno);
            textViewVotoMedio = itemView.findViewById(R.id.textViewVotoMedio);
        }
    }
}
