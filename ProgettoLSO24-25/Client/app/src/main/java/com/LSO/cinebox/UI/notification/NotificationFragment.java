package com.LSO.cinebox.UI.notification;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.LSO.cinebox.Infrastructure.ServerConnect;
import com.LSO.cinebox.R;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private int userId;
    private EditText messageInput;
    private Spinner userSpinner;
    private Button sendButton;
    private ListView notificationList;
    private TextView noNotificationsTextView;
    private ArrayAdapter<String> notificationAdapter;
    private final ArrayList<String> notifications = new ArrayList<>();
    private final ArrayList<Integer> notificationIds = new ArrayList<>();
    private ServerConnect serverConnect;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        userId = getActivity().getIntent().getIntExtra("userId", -1);
        serverConnect = new ServerConnect();

        notificationList = view.findViewById(R.id.notification_list);
        noNotificationsTextView = view.findViewById(R.id.no_notifications_text);

        notificationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, notifications);
        notificationList.setAdapter(notificationAdapter);

        if (userId == 0) {
            // Layout admin
            messageInput = view.findViewById(R.id.message_input);
            userSpinner = view.findViewById(R.id.user_spinner);
            sendButton = view.findViewById(R.id.send_button);

            messageInput.setVisibility(View.VISIBLE);
            userSpinner.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);

            messageInput.setText("Superata la scadenza per la restituzione, consegnare il film");

            loadUsers();

            sendButton.setOnClickListener(v -> sendMessage());
        } else {
            // Layout utente
            loadNotifications();

            notificationList.setOnItemLongClickListener((adapterView, view1, position, id) -> {
                showDeleteDialog(position);
                return true;
            });
        }

        return view;
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        String selectedUser = userSpinner.getSelectedItem().toString();
        int targetUserId = Integer.parseInt(selectedUser.split(" - ")[0]);

        if (message.isEmpty()) {
            Toast.makeText(requireContext().getApplicationContext(), "Inserisci un messaggio", Toast.LENGTH_SHORT).show();
            return;
        }

        serverConnect.sendMessage("SEND_NOTIFICATION " + targetUserId + " " + message, new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext().getApplicationContext(), "Messaggio inviato", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext().getApplicationContext(), "Errore invio messaggio", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadUsers() {
        if (!serverConnect.isConnected()) {
            serverConnect.openConnection(new ServerConnect.ConnectionCallback() {
                @Override
                public void onSuccess() {
                    sendLoadUsersRequest();
                }

                @Override
                public void onFailure(Exception e) {
                    getActivity().runOnUiThread(() -> Toast.makeText(requireContext().getApplicationContext(), "Errore connessione al server", Toast.LENGTH_SHORT).show());
                }
            });
        } else {
            sendLoadUsersRequest();
        }
    }

    private void sendLoadUsersRequest() {
        serverConnect.sendMessage("LIST_USERS", new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                ArrayList<String> users = new ArrayList<>();
                String[] lines = response.split("\n");
                for (String line : lines) {
                    if (!line.startsWith("0 - ")) {
                        users.add(line);
                    }
                }
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<String> userAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, users);
                    userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    userSpinner.setAdapter(userAdapter);
                });
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext().getApplicationContext(), "Errore caricamento utenti", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadNotifications() {
        serverConnect.sendMessage("GET_NOTIFICATIONS " + userId, new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                final ArrayList<String> newNotifications = new ArrayList<>();
                final ArrayList<Integer> newNotificationIds = new ArrayList<>();

                String[] lines = response.split("\n");
                for (String line : lines) {
                    if (line.equals("END")) break;

                    Log.d("NotificationFragment", "Ricevuto: " + line);

                    String[] parts = line.split(" \\| ");
                    if (parts.length >= 3) {
                        int id = Integer.parseInt(parts[0].split(": ")[1]);
                        String message = parts[1];
                        String date = parts[2];

                        newNotifications.add(0, message + "\n" + date);
                        newNotificationIds.add(0, id);
                    }
                }

                getActivity().runOnUiThread(() -> {
                    notifications.clear();
                    notifications.addAll(newNotifications);
                    notificationIds.clear();
                    notificationIds.addAll(newNotificationIds);
                    notificationAdapter.notifyDataSetChanged();

                    if (notifications.isEmpty()) {
                        noNotificationsTextView.setVisibility(View.VISIBLE);
                    } else {
                        noNotificationsTextView.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("NotificationFragment", "Errore caricamento notifiche", e);
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext().getApplicationContext(), "Errore caricamento notifiche", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Elimina notifica")
                .setMessage("Vuoi eliminare questa notifica?")
                .setPositiveButton("Sì", (dialogInterface, i) -> deleteNotification(position))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteNotification(int position) {
        int notificationId = notificationIds.get(position);

        serverConnect.sendMessage("DELETE_NOTIFICATION " + notificationId, new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                getActivity().runOnUiThread(() -> {
                    notifications.remove(position);
                    notificationIds.remove(position);
                    notificationAdapter.notifyDataSetChanged();
                    Toast.makeText(requireContext().getApplicationContext(), "Notifica eliminata", Toast.LENGTH_SHORT).show();

                    if (notifications.isEmpty()) {
                        noNotificationsTextView.setVisibility(View.VISIBLE);
                    } else {
                        noNotificationsTextView.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext().getApplicationContext(), "Errore eliminazione notifica", Toast.LENGTH_SHORT).show());
            }
        });
    }
}