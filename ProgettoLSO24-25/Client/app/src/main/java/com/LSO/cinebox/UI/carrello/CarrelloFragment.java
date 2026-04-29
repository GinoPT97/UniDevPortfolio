package com.LSO.cinebox.UI.carrello;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.LSO.cinebox.Entity.Film;
import com.LSO.cinebox.Infrastructure.ServerConnect;
import com.LSO.cinebox.MainActivity;
import com.LSO.cinebox.Model.CartManager;
import com.LSO.cinebox.Model.FilmAdapter;
import com.LSO.cinebox.R;
import com.LSO.cinebox.UI.catalogo.CatalogoViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CarrelloFragment extends Fragment {

    private CarrelloViewModel mViewModel;
    private RecyclerView recyclerViewCarrello;
    private FilmAdapter filmAdapter;
    private TextView totalPriceTextView;
    private LinearLayout adminControls;
    private EditText maxRentalsInput;
    private Button setMaxRentalsButton;
    private TextView emptyCartTextView;

    public static CarrelloFragment newInstance() {
        return new CarrelloFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_carrello, container, false);

        recyclerViewCarrello = root.findViewById(R.id.recyclerViewCarrello);
        totalPriceTextView = root.findViewById(R.id.total_price_text_view);
        adminControls = root.findViewById(R.id.admin_controls);
        maxRentalsInput = root.findViewById(R.id.max_rentals_input);
        setMaxRentalsButton = root.findViewById(R.id.set_max_rentals_button);
        emptyCartTextView = root.findViewById(R.id.empty_cart_text_view); // aggiungi una TextView nel layout

        int userId = getActivity().getIntent().getIntExtra("userId", -1);

        if (userId == 0) {
            adminControls.setVisibility(View.VISIBLE);
            recyclerViewCarrello.setVisibility(View.GONE);
            totalPriceTextView.setVisibility(View.GONE);
            root.findViewById(R.id.buttonClearCart).setVisibility(View.GONE);
            root.findViewById(R.id.buttonCheckout).setVisibility(View.GONE);
            setupAdminControls();
            fetchMaxRentals();
        } else {
            setupRecyclerView();
            setupButtons(root);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int userId = getActivity().getIntent().getIntExtra("userId", -1);
        if (userId != 0) {
            observeViewModel();
        }
    }

    private void setupRecyclerView() {
        recyclerViewCarrello.setLayoutManager(new LinearLayoutManager(getContext()));
        filmAdapter = new FilmAdapter(
            new ArrayList<>(),
            CartManager.getInstance().getCart(),
            this::updateCarrello,
            true,
            (MainActivity) getActivity(),
            null
        );
        recyclerViewCarrello.setAdapter(filmAdapter);
    }

    private void setupButtons(View root) {
        Button buttonCheckout = root.findViewById(R.id.buttonCheckout);
        Button buttonClearCart = root.findViewById(R.id.buttonClearCart);

        buttonClearCart.setText("Svuota");

        buttonCheckout.setOnClickListener(v -> handleButtonClick(CartManager.getInstance().getCart().isEmpty(), "Il carrello è vuoto", "Checkout completato"));
        buttonClearCart.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            updateCarrello();
            Snackbar.make(requireView(), "Carrello svuotato", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setupAdminControls() {
        setMaxRentalsButton.setOnClickListener(v -> {
            String maxRentalsStr = maxRentalsInput.getText().toString();
            if (!maxRentalsStr.isEmpty()) {
                int maxRentals = Integer.parseInt(maxRentalsStr);
                ServerConnect serverConnect = new ServerConnect();
                serverConnect.openConnection(new ServerConnect.ConnectionCallback() {
                    @Override
                    public void onSuccess() {
                        serverConnect.sendMessage("SET_MAX_RENTALS " + maxRentals, new ServerConnect.MessageCallback() {
                            @Override
                            public void onSuccess(String response) {
                                getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Massimo noleggi impostato a " + maxRentals, Snackbar.LENGTH_SHORT).show());
                            }

                            @Override
                            public void onFailure(Exception e) {
                                getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Errore nell'impostazione del massimo noleggi: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Connessione al server fallita: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }

    private void observeViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(CarrelloViewModel.class);
        mViewModel.getCarrelloList().observe(getViewLifecycleOwner(), films -> {
            filmAdapter.setFilms(films);
            updateTotalPrice();
        });

        if (CartManager.getInstance().isEmpty()) {
            Snackbar.make(requireView(), "Il carrello è vuoto", Snackbar.LENGTH_SHORT).show();
        }

        if (getActivity().getIntent().getIntExtra("userId", -1) == 0) {
            adminControls.setVisibility(View.VISIBLE);
        }
    }

    private void handleButtonClick(boolean isEmpty, String emptyMessage, String successMessage) {
        if (isEmpty) {
            Snackbar.make(requireView(), emptyMessage, Snackbar.LENGTH_SHORT).show();
        } else {
            int userId = getActivity().getIntent().getIntExtra("userId", -1);
            ServerConnect serverConnect = new ServerConnect();
            serverConnect.openConnection(new ServerConnect.ConnectionCallback() {
                @Override
                public void onSuccess() {
                    checkAvailabilityAndCheckout(userId, serverConnect, successMessage);
                }

                @Override
                public void onFailure(Exception e) {
                    getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Connessione al server fallita: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
                }
            });
        }
    }

    private void checkAvailabilityAndCheckout(int userId, ServerConnect serverConnect, String successMessage) {
        ArrayList<Film> cart = new ArrayList<>(CartManager.getInstance().getCart());
        for (Film film : cart) {
            serverConnect.sendMessage("CHECK_AVAILABILITY " + film.getIdFilm(), new ServerConnect.MessageCallback() {
                @Override
                public void onSuccess(String response) {
                    if (response.equals("UNAVAILABLE")) {
                        getActivity().runOnUiThread(() -> {
                            Snackbar.make(requireView(), "Il film " + film.getTitolo() + " non è disponibile", Snackbar.LENGTH_SHORT).show();
                            CartManager.getInstance().removeFilm(film);
                            updateCarrello();
                        });
                    } else {
                        proceedWithCheckout(userId, serverConnect, successMessage);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Errore durante il controllo disponibilità: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
                }
            });
        }
    }

    private void proceedWithCheckout(int userId, ServerConnect serverConnect, String successMessage) {
        String cartData = createNoleggioData(userId);
        serverConnect.sendMessage("CHECKOUT " + cartData, new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                getActivity().runOnUiThread(() -> {
                    if (response.startsWith("SUCCESS")) {
                        Snackbar.make(requireView(), successMessage, Snackbar.LENGTH_SHORT).show();
                        CartManager.getInstance().clearCart();
                        updateCarrello();
                        ((MainActivity) getActivity()).fetchDataFromServer();
                        updateCatalogo();
                    } else if (response.contains("Il numero totale di noleggi supera il limite massimo consentito")) {
                        showLimitExceededDialog();
                    } else {
                        Snackbar.make(requireView(), "Errore durante il checkout", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Errore durante il checkout: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
            }
        });
    }

    private void showLimitExceededDialog() {
        new AlertDialog.Builder(getContext())
            .setTitle("Limite Noleggi Superato")
            .setMessage("Hai superato il limite massimo di noleggi consentiti. Per favore, restituisci alcuni film prima di procedere con nuovi noleggi.")
            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
            .show();
    }

    private void updateCatalogo() {
        CatalogoViewModel catalogoViewModel = new ViewModelProvider(requireActivity()).get(CatalogoViewModel.class);
        List<Film> cart = CartManager.getInstance().getCart();
        List<Film> catalogo = catalogoViewModel.getFilmList().getValue();

        if (catalogo != null) {
            for (Film cartFilm : cart) {
                for (Film catalogoFilm : catalogo) {
                    if (cartFilm.getIdFilm() == catalogoFilm.getIdFilm()) {
                        catalogoFilm.setNumeroCopieDisponibili(catalogoFilm.getNumeroCopieDisponibili() - 1);
                        catalogoFilm.setNumeroCopieInPrestito(catalogoFilm.getNumeroCopieInPrestito() + 1);
                        break;
                    }
                }
            }
            catalogoViewModel.updateFilmList(catalogo);
        }
    }

    private String createNoleggioData(int userId) {
        StringBuilder dataBuilder = new StringBuilder();
        for (Film film : CartManager.getInstance().getCart()) {
            dataBuilder.append(userId)
                       .append(",")
                       .append(film.getIdFilm())
                       .append(",")
                       .append(1)
                       .append(";");
        }
        return dataBuilder.toString();
    }

    private void updateCarrello() {
        mViewModel.updateCarrelloList();
        if (filmAdapter != null) {
            filmAdapter.setFilms(CartManager.getInstance().getCart());
        }
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = CartManager.getInstance().getTotalPrice();
        totalPriceTextView.setText("Totale: €" + String.format("%.2f", totalPrice));
    }

    private void fetchMaxRentals() {
        ServerConnect serverConnect = new ServerConnect();
        serverConnect.openConnection(new ServerConnect.ConnectionCallback() {
            @Override
            public void onSuccess() {
                serverConnect.sendMessage("GET_MAX_RENTALS", new ServerConnect.MessageCallback() {
                    @Override
                    public void onSuccess(String response) {
                        getActivity().runOnUiThread(() -> {
                            if (response != null && !response.isEmpty()) {
                                String maxRentals = response.replace("MAX_RENTALS ", "").trim();
                                maxRentalsInput.setHint(maxRentals);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Errore nel recupero del massimo noleggi: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(() -> Snackbar.make(requireView(), "Connessione al server fallita: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recyclerViewCarrello != null) {
            recyclerViewCarrello.setAdapter(null);
        }
    }
}