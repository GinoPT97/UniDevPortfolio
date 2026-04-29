package com.LSO.cinebox.UI.catalogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.LSO.cinebox.Entity.Film;
import com.LSO.cinebox.Infrastructure.ServerConnect;
import com.LSO.cinebox.MainActivity;
import com.LSO.cinebox.Model.FilmAdapter;
import com.LSO.cinebox.R;
import com.LSO.cinebox.UI.carrello.CarrelloViewModel;
import com.LSO.cinebox.databinding.FragmentCatalogoBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CatalogoFragment extends Fragment {

    private FragmentCatalogoBinding binding;
    private RecyclerView recyclerView;
    private FilmAdapter filmAdapter;
    private List<Film> filmList = new ArrayList<>();
    private List<Film> filteredList = new ArrayList<>();
    private ServerConnect serverConnect;
    private CatalogoViewModel catalogoViewModel;
    private TextView filmCountBadge;
    private View noResultsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            
            @SuppressWarnings("unchecked")
            List<Film> savedFilmList = (List<Film>) savedInstanceState.getSerializable("filmList");
            @SuppressWarnings("unchecked")
            List<Film> savedFilteredList = (List<Film>) savedInstanceState.getSerializable("filteredList");
            if (savedFilmList != null) filmList = savedFilmList;
            if (savedFilteredList != null) filteredList = savedFilteredList;
        }
    }

    public void setServerConnect(ServerConnect serverConnect) {
        this.serverConnect = serverConnect;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        
        catalogoViewModel = new ViewModelProvider(requireActivity()).get(CatalogoViewModel.class);

        binding = FragmentCatalogoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        filmCountBadge = root.findViewById(R.id.filmCountBadge);

        filmAdapter = new FilmAdapter(
            new ArrayList<>(),
            new ArrayList<>(),
            this::updateCarrello,
            false,
            (MainActivity) getActivity(),
            film -> showFilmDetailsDialog(film)
        );
        recyclerView.setAdapter(filmAdapter);

        setupSearchView();
        setupGenreSpinner();
        setupSortSpinner();
        binding.searchView.setQueryHint("Cerca film per titolo, genere...");

        return root;
    }

    private void setupGenreSpinner() {
        Set<String> genreSet = new HashSet<>();
        for (Film film : filmList) {
            String[] filmGenres = film.getGenere().split(",");
            for (String g : filmGenres) {
                genreSet.add(g.trim());
            }
        }
        List<String> genres = new ArrayList<>();
        genres.add("Tutti");
        genres.addAll(genreSet);
        List<String> genresToSort = genres.subList(1, genres.size());
        Collections.sort(genresToSort);
        ArrayAdapter<String> genreAdapter = (ArrayAdapter<String>) binding.spinnerGenre.getAdapter();
        if (genreAdapter == null || genreAdapter.getCount() != genres.size()) {
            genreAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genres);
            genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerGenre.setAdapter(genreAdapter);
        }
    }


    private void setupSortSpinner() {
        List<String> sortOptions = new ArrayList<>();
        sortOptions.add("Nessuno");
        sortOptions.add("Popolarità crescente");
        sortOptions.add("Popolarità decrescente");
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSort.setAdapter(sortAdapter);
    }

    private void setupSearchView() {
        SearchView searchView = binding.searchView;
        Spinner spinnerGenre = binding.spinnerGenre;
        Spinner spinnerSort = binding.spinnerSort;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters(query, spinnerGenre.getSelectedItem() != null ? spinnerGenre.getSelectedItem().toString() : "", spinnerSort.getSelectedItem() != null ? spinnerSort.getSelectedItem().toString() : "");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilters(newText, spinnerGenre.getSelectedItem() != null ? spinnerGenre.getSelectedItem().toString() : "", spinnerSort.getSelectedItem() != null ? spinnerSort.getSelectedItem().toString() : "");
                return false;
            }
        });

        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters(searchView.getQuery().toString(), parent.getItemAtPosition(position).toString(), spinnerSort.getSelectedItem() != null ? spinnerSort.getSelectedItem().toString() : "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters(searchView.getQuery().toString(), spinnerGenre.getSelectedItem() != null ? spinnerGenre.getSelectedItem().toString() : "", parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CatalogoViewModel catalogoViewModel = new ViewModelProvider(requireActivity()).get(CatalogoViewModel.class);
        catalogoViewModel.getFilmList().observe(getViewLifecycleOwner(), films -> {
            if (films == null) return;
            if (filmList.equals(films)) return;
            filmList = films;
            filteredList.clear();
            filteredList.addAll(films);
            if (filmAdapter != null) {
                filmAdapter.setFilms(filteredList);
            }
            setupGenreSpinner();
            setupSortSpinner();
            updateFilmCountBadge(films.size());
        });
    }

    private void updateCarrello() {
        CarrelloViewModel carrelloViewModel = new ViewModelProvider(requireActivity()).get(CarrelloViewModel.class);
        carrelloViewModel.updateCarrelloList();
    }

    private void applyFilters(String query, String genre, String sortOption) {
        new Thread(() -> {
            List<Film> result = new ArrayList<>();
            String queryLower = query.trim().toLowerCase();
            String genreLower = genre.trim().toLowerCase();
            if (queryLower.isEmpty() && (genreLower.equals("tutti") || genreLower.isEmpty())) {
                result.addAll(filmList);
            } else {
                for (Film film : filmList) {
                    String filmTitle = film.getTitolo().trim().toLowerCase();
                    String filmGenre = film.getGenere() != null ? film.getGenere().toLowerCase() : "";
                    boolean matchesQuery = filmTitle.contains(queryLower) || filmGenre.contains(queryLower);
                    boolean matchesGenre = true;
                    if (!genreLower.equals("tutti")) {
                        matchesGenre = false;
                        String[] filmGenres = film.getGenere().split(",");
                        for (String g : filmGenres) {
                            if (g.trim().toLowerCase().equals(genreLower)) {
                                matchesGenre = true;
                                break;
                            }
                        }
                    }
                    if (matchesQuery && matchesGenre) {
                        result.add(film);
                    }
                }
            }
            if (sortOption.equals("Popolarità crescente")) {
                result.sort((f1, f2) -> Integer.compare(f1.getNumeroVoti(), f2.getNumeroVoti()));
            } else if (sortOption.equals("Popolarità decrescente")) {
                result.sort((f1, f2) -> Integer.compare(f2.getNumeroVoti(), f1.getNumeroVoti()));
            }
            requireActivity().runOnUiThread(() -> {
                filteredList.clear();
                filteredList.addAll(result);
                filmAdapter.setHighlightQuery(query);
                filmAdapter.setFilms(filteredList);
                updateFilmCountBadge(filteredList.size());
                if (filteredList.isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    if (noResultsView == null) {
                        TextView noResults = new TextView(getContext());
                        noResults.setText("Nessun film trovato");
                        noResults.setTextSize(18);
                        noResults.setTextColor(0xFF888888);
                        noResults.setPadding(32, 100, 32, 32);
                        noResults.setGravity(android.view.Gravity.CENTER);
                        binding.getRoot().addView(noResults);
                        noResultsView = noResults;
                    }
                    noResultsView.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    if (noResultsView != null) {
                        noResultsView.setVisibility(View.GONE);
                    }
                }
            });
        }).start();
    }

    private void updateFilmCountBadge(int count) {
        if (filmCountBadge != null) {
            filmCountBadge.setText(String.valueOf(count));
        }
    }

    private Film getSelectedFilm() {
        int selectedPosition = recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return filmAdapter.getFilmAtPosition(selectedPosition);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("filmList", new ArrayList<>(filmList));
        outState.putSerializable("filteredList", new ArrayList<>(filteredList));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        recyclerView.setAdapter(null);
    }

    private void showFilmDetailsDialog(Film film) {
        FilmDetailsDialogFragment dialog = FilmDetailsDialogFragment.newInstance(film);
        dialog.show(getParentFragmentManager(), "FilmDetailsDialog");
    }
}
