package com.LSO.cinebox;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.LSO.cinebox.Entity.Film;
import com.LSO.cinebox.Infrastructure.ServerConnect;
import com.LSO.cinebox.UI.catalogo.CatalogoFragment;
import com.LSO.cinebox.UI.catalogo.CatalogoViewModel;
import com.LSO.cinebox.UI.dashboard.DashBoardViewModel;
import com.LSO.cinebox.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static ServerConnect sharedServerConnect;
    
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ServerConnect serverConnect;
    private TextView textView;
    private CatalogoViewModel catalogoViewModel;
    private DashBoardViewModel mViewModel;
    private int userId;

    private boolean isFetchingData = false;
    private boolean isFetchingFilms = false;

    public static void setPersistentServerConnect(ServerConnect serverConnect) {
        sharedServerConnect = serverConnect;
    }

    public static ServerConnect getSharedServerConnect() {
        return sharedServerConnect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_catalogo, R.id.nav_carrello, R.id.nav_notification)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                logout();
                DrawerLayout drawer1 = binding.drawerLayout;
                drawer1.closeDrawer(GravityCompat.START);
                return true;
            }
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                drawer.closeDrawer(GravityCompat.START);
            }
            if (id == R.id.nav_catalogo && !isFetchingFilms) {
                fetchFilmsFromServer();
            } else if (id == R.id.nav_dashboard && !isFetchingData) {
                fetchDataFromServer();
            }
            return handled;
        });

        // Usa la connessione persistente condivisa se disponibile, altrimenti crea una nuova
        if (sharedServerConnect != null) {
            serverConnect = sharedServerConnect;
            System.out.println("Usando connessione persistente condivisa");
        } else {
            serverConnect = new ServerConnect();
            System.out.println("Creando nuova connessione ServerConnect");
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.nav_catalogo) {
                    Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                }
            });
        }

        textView = findViewById(R.id.textView);

        catalogoViewModel = new ViewModelProvider(this).get(CatalogoViewModel.class);
        catalogoViewModel.getFilmList().observe(this, films -> {
            if (textView != null) {
                StringBuilder filmListString = new StringBuilder();
                for (Film film : films) {
                    filmListString.append(film.getTitolo()).append("\n");
                }
                textView.setText(filmListString.toString());
            }
        });

        mViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        userId = intent.getIntExtra("userId", -1);

        Log.d("MainActivity", "Intent ricevuto: username=" + username + ", userId=" + userId);

        if (userId != -1) {
            TextView dashboardTitle = findViewById(R.id.dashboardTitle);
            if (dashboardTitle != null) {
                dashboardTitle.setText(getString(R.string.dashboard_title) + " " + username);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Errore nel recupero dei dati utente.", Toast.LENGTH_SHORT).show();
        }

        connectToServer();
    }

    public DashBoardViewModel getViewModel() {
        return mViewModel;
    }

    public int getUserId() {
        return userId;
    }

    private void connectToServer() {
        if (serverConnect.isPersistentMode() && serverConnect.isConnected()) {
            fetchDataFromServer();
            return;
        }
        if (serverConnect.isPersistentMode()) {
            // Se siamo in modalità persistente ma non connessi, non riavviare
            fetchDataFromServer();
            return;
        }
        new Thread(() -> {
            serverConnect.startPersistentConnection(new ServerConnect.ConnectionCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Connessione persistente stabilita", Toast.LENGTH_SHORT).show();
                        fetchDataFromServer();
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Connessione al server fallita: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }).start();
    }

    public void fetchDataFromServer() {
        if (!serverConnect.isConnected() && !serverConnect.isPersistentMode()) {
            connectToServer();
            return;
        }
        synchronized (this) {
            if (isFetchingData) return;
            isFetchingData = true;
        }
        new Thread(() -> {
            if (userId != 0) {
                serverConnect.fetchDataFromServerWithCommand("GET_LAST_5_RENTALS_BY_USER " + userId, new ServerConnect.DataCallback() {
                    @Override
                    public void onSuccess(String response) {
                        runOnUiThread(() -> {
                            List<String> rentals = Arrays.asList(response.replace("END\n", "").split("\n"));
                            mViewModel.updateLast5Rentals(rentals);
                    });
                    fetchTop5RentedFilms();
                }
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            });
            fetchActiveRentalsByUser();
        } else {
            serverConnect.fetchDataFromServerWithCommand("GET_ALL_RENTALS_OVERVIEW", new ServerConnect.DataCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        List<String> rentals = Arrays.asList(response.replace("END\n", "").split("\n"));
                        mViewModel.updateAllRentalsOverview(rentals);
                    });
                }
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            });
        }
        synchronized (this) { isFetchingData = false; }
        }).start();
    }

    private void fetchTop5RentedFilms() {
        serverConnect.fetchDataFromServerWithCommand("GET_TOP_5_RENTED_FILMS", new ServerConnect.DataCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    List<String> films = Arrays.asList(response.replace("END\n", "").split("\n"));
                    mViewModel.updateTop5RentedFilms(films);
                });
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void fetchActiveRentalsByUser() {
        serverConnect.fetchDataFromServerWithCommand("GET_ACTIVE_RENTALS_BY_USER " + userId, new ServerConnect.DataCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    List<String> rentals = Arrays.asList(response.replace("END\n", "").split("\n"));
                    mViewModel.updateActiveRentals(rentals);
                });
            }
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void fetchFilmsFromServer() {
        synchronized (this) {
            if (isFetchingFilms) return;
            isFetchingFilms = true;
        }
        serverConnect.sendMessage("GET_FILMS", new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    List<Film> films = Film.parseFilmsFromRawData(response);
                    catalogoViewModel.updateFilmList(films);
                    synchronized (MainActivity.this) { isFetchingFilms = false; }
                });
            }
            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Errore nel recupero dei film: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    synchronized (MainActivity.this) { isFetchingFilms = false; }
                });
            }
        });
    }

    public ServerConnect getServerConnect() {
        return serverConnect;
    }

    private void logout() {
        resetDashboardViewModel();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void resetDashboardViewModel() {
        mViewModel.updateLast5Rentals(new ArrayList<>());
        mViewModel.updateTop5RentedFilms(new ArrayList<>());
        mViewModel.updateAllRentalsOverview(new ArrayList<>());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int newUserId = intent.getIntExtra("userId", -1);
        if (newUserId != userId) {
            userId = newUserId;
            resetDashboardViewModel();
            fetchDataFromServer();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(() -> {
            if (serverConnect != null) {
                System.out.println("Chiusura connessione persistente...");
                serverConnect.stopPersistentConnection();
            }
        }).start();
        binding = null;
    }
}