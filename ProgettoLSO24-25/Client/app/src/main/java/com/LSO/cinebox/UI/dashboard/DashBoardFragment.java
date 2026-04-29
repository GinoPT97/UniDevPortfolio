package com.LSO.cinebox.UI.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.LSO.cinebox.Infrastructure.ServerConnect;
import com.LSO.cinebox.MainActivity;
import com.LSO.cinebox.Model.SimpleStringAdapter;
import com.LSO.cinebox.R;

import java.util.ArrayList;
import java.util.List;

public class DashBoardFragment extends Fragment {

    private DashBoardViewModel mViewModel;
    private ProgressBar progressBar;

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dash_board, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(DashBoardViewModel.class);
        View view = getView();
        if (view != null) {
            progressBar = view.findViewById(R.id.progressBar);

            TextView dashboardTitle = view.findViewById(R.id.dashboardTitle);
            TextView dashboardWelcome = view.findViewById(R.id.dashboardWelcome);
            String username = requireActivity().getIntent().getStringExtra("username");
            if (dashboardWelcome != null && username != null) {
                dashboardWelcome.setText("Benvenuto, " + username + "!");
            }

            TextView last5Badge = view.findViewById(R.id.last5Badge);
            TextView top5Badge = view.findViewById(R.id.top5Badge);

            View cardLast5 = view.findViewById(R.id.cardLast5Rentals);
            View cardTop5 = view.findViewById(R.id.cardTop5RentedFilms);
            View cardActive = view.findViewById(R.id.cardActiveRentals);
            View cardAllOverview = view.findViewById(R.id.cardAllRentalsOverview);

            Intent intent = getActivity().getIntent();
            int userId = intent.getIntExtra("userId", -1);

            if (userId != 0) {
                cardLast5.setVisibility(View.VISIBLE);
                cardTop5.setVisibility(View.VISIBLE);
                cardActive.setVisibility(View.VISIBLE);
                cardAllOverview.setVisibility(View.GONE);

                RecyclerView recyclerLast5Rentals = view.findViewById(R.id.recyclerLast5Rentals);
                recyclerLast5Rentals.setLayoutManager(new LinearLayoutManager(getContext()));
                SimpleStringAdapter last5RentalsAdapter = new SimpleStringAdapter(new ArrayList<>(), null);
                recyclerLast5Rentals.setAdapter(last5RentalsAdapter);
                mViewModel.getLast5Rentals().observe(getViewLifecycleOwner(), rentals -> {
                    List<String> filteredRentals = new ArrayList<>(rentals);
                    filteredRentals.removeIf(String::isEmpty);
                    last5RentalsAdapter.updateData(filteredRentals);
                    if (last5Badge != null) last5Badge.setText(String.valueOf(filteredRentals.size()));
                    progressBar.setVisibility(View.GONE);
                });

                RecyclerView recyclerTop5RentedFilms = view.findViewById(R.id.recyclerTop5RentedFilms);
                recyclerTop5RentedFilms.setLayoutManager(new LinearLayoutManager(getContext()));
                SimpleStringAdapter top5RentedFilmsAdapter = new SimpleStringAdapter(new ArrayList<>(), null);
                recyclerTop5RentedFilms.setAdapter(top5RentedFilmsAdapter);
                mViewModel.getTop5RentedFilms().observe(getViewLifecycleOwner(), films -> {
                    List<String> filteredFilms = new ArrayList<>(films);
                    filteredFilms.removeIf(String::isEmpty);
                    top5RentedFilmsAdapter.updateData(filteredFilms);
                    if (top5Badge != null) top5Badge.setText(String.valueOf(filteredFilms.size()));
                    progressBar.setVisibility(View.GONE);
                });

                RecyclerView recyclerActiveRentals = view.findViewById(R.id.recyclerActiveRentals);
                recyclerActiveRentals.setLayoutManager(new LinearLayoutManager(getContext()));
                SimpleStringAdapter activeRentalsAdapter = new SimpleStringAdapter(new ArrayList<>(), null);
                recyclerActiveRentals.setAdapter(activeRentalsAdapter);
                mViewModel.getActiveRentals().observe(getViewLifecycleOwner(), rentals -> {
                    List<String> filteredRentals = new ArrayList<>(rentals);
                    filteredRentals.removeIf(String::isEmpty);
                    activeRentalsAdapter.updateData(filteredRentals);
                    progressBar.setVisibility(View.GONE);
                });
            } else {
                cardLast5.setVisibility(View.GONE);
                cardTop5.setVisibility(View.GONE);
                cardActive.setVisibility(View.GONE);
                cardAllOverview.setVisibility(View.VISIBLE);

                RecyclerView recyclerAllRentalsOverview = view.findViewById(R.id.recyclerAllRentalsOverview);
                recyclerAllRentalsOverview.setLayoutManager(new LinearLayoutManager(getContext()));
                SimpleStringAdapter allRentalsOverviewAdapter = new SimpleStringAdapter(new ArrayList<>(), this::showConfirmationDialog);
                recyclerAllRentalsOverview.setAdapter(allRentalsOverviewAdapter);
                mViewModel.getAllRentalsOverview().observe(getViewLifecycleOwner(), rentals -> {
                    List<String> filteredRentals = new ArrayList<>();
                    if (rentals != null) {
                        for (String r : rentals) {
                            if (r != null && !r.isEmpty()) filteredRentals.add(r);
                        }
                    }
                    allRentalsOverviewAdapter.updateData(filteredRentals);
                    progressBar.setVisibility(View.GONE);
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            progressBar.setVisibility(View.VISIBLE);
            activity.fetchDataFromServer();
        }
    }

    private void showConfirmationDialog(String rentalInfo) {
        new AlertDialog.Builder(getContext())
            .setTitle("Film restituito?")
            .setMessage("Vuoi impostare questo noleggio come restituito?")
            .setPositiveButton("Sì", (dialog, which) -> {
                String rentalId = extractRentalId(rentalInfo);
                ((MainActivity) getActivity()).getServerConnect().sendMessage("RETURN_RENTAL " + rentalId, new ServerConnect.MessageCallback() {
                    @Override
                    public void onSuccess(String response) {
                        if (isAdded()) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Noleggio aggiornato con successo", Toast.LENGTH_SHORT).show();
                                List<String> currentRentals = mViewModel.getAllRentalsOverview().getValue();
                                if (currentRentals != null) {
                                    List<String> updatedRentals = new ArrayList<>(currentRentals);
                                    updatedRentals.remove(rentalInfo);
                                    mViewModel.updateAllRentalsOverview(updatedRentals);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (isAdded()) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Errore nell'aggiornamento del noleggio", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            })
            .setNegativeButton("No", null)
            .show();
    }

    private String extractRentalId(String rentalInfo) {
        return rentalInfo.split(" - ")[0];
    }
}