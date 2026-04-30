package com.LSO.cinebox.UI.catalogo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.LSO.cinebox.Entity.Film;
import com.LSO.cinebox.R;
import com.squareup.picasso.Picasso;

public class FilmDetailsDialogFragment extends DialogFragment {

    private static final String ARG_FILM = "film";

    public static FilmDetailsDialogFragment newInstance(Film film) {
        FilmDetailsDialogFragment fragment = new FilmDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILM, film);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        Film film = args != null ? (Film) args.getSerializable(ARG_FILM) : null;
        if (film == null) return new AlertDialog.Builder(requireContext()).setMessage("Errore: film non trovato").setPositiveButton("Chiudi", null).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_film_details, null);

        ImageView imageView = view.findViewById(R.id.dialog_imageViewLocandina);
        TextView titolo = view.findViewById(R.id.dialog_textViewTitolo);
        TextView descrizione = view.findViewById(R.id.dialog_textViewDescrizione);
        TextView genere = view.findViewById(R.id.dialog_textViewGenere);
        TextView lingua = view.findViewById(R.id.dialog_textViewLinguaOriginale);
        TextView data = view.findViewById(R.id.dialog_textViewDataRilascio);
        TextView stato = view.findViewById(R.id.dialog_textViewStato);
        TextView numeroCopieDisponibili = view.findViewById(R.id.dialog_textViewNumeroCopieDisponibili);
        TextView numeroCopieInPrestito = view.findViewById(R.id.dialog_textViewNumeroCopieInPrestito);
        TextView prezzo = view.findViewById(R.id.dialog_textViewPrezzo);

        TextView votoMedio = view.findViewById(R.id.dialog_textViewVotoMedio);
        TextView numeroVoti = view.findViewById(R.id.dialog_textViewNumeroVoti);
        TextView popolarita = view.findViewById(R.id.dialog_textViewPopularita);

        Picasso.get().load(film.getLocandinaUrl()).placeholder(R.drawable.generic_image).into(imageView);
        titolo.setText(film.getTitolo());
        descrizione.setText(film.getDescrizione());
        genere.setText("Genere: " + film.getGenere());
        lingua.setText("Lingua originale: " + film.getLinguaOriginale());
        data.setText("Data rilascio: " + film.getDataRilascio());
        stato.setText("Stato: " + film.getStato());
        numeroCopieDisponibili.setText("Copie disponibili: " + film.getNumeroCopieDisponibili());
        numeroCopieInPrestito.setText("Copie in prestito: " + film.getNumeroCopieInPrestito());
        prezzo.setText("Prezzo: €" + String.format("%.2f", film.getPrezzo()));

        votoMedio.setText("★ " + String.format("%.1f", film.getVotoMedio()) + "/10");
        votoMedio.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        votoMedio.setBackgroundResource(R.drawable.badge_bg);
        votoMedio.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.orange));
        votoMedio.setPadding(24, 8, 24, 8);

        numeroVoti.setText("Numero voti: " + film.getNumeroVoti());
        popolarita.setText("Popolarità: " + String.format("%.2f", film.getPopolarita()));

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setPositiveButton("Chiudi", null)
                .create();
    }
}
