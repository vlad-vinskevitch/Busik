package com.sharkit.busik.ui.Sender;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.MessagesAdapter;
import com.sharkit.busik.Entity.Message;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class SenderMessage extends Fragment {
    private ListView listMessage;
    private ImageView back;
    private ArrayList<Message> messages;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sender_massage, container, false);
        findView(root);
        getAllMessage();
        onClick();
        return root;
    }

    private void onClick() {
        back.setOnClickListener(v ->Navigation.findNavController(getActivity(), R.id.nav_host_sender).navigate(R.id.nav_sender_profile) );

    }

    private void getAllMessage() {
        messages = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users/" + StaticUser.getEmail() + "/Message")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                            messages.add(queryDocumentSnapshot.toObject(Message.class));
                        }
                        setAdapter();
                    }
                });
    }

    private void setAdapter() {
        MessagesAdapter messagesAdapter = new MessagesAdapter(messages, getContext());
        listMessage.setAdapter(messagesAdapter);
    }

    private void findView(View root) {
        back = root.findViewById(R.id.back_xml);
        listMessage = root.findViewById(R.id.list_message_xml);
    }
}
