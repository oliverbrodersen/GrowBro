package com.example.growbro.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.User;
import com.example.growbro.R;
import com.example.growbro.ui.friends.rv.FriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements FriendAdapter.OnListItemClickListener{

    private FriendsViewModel mViewModel;
    private RecyclerView friendList;
    private FriendAdapter friendAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friends_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        friendList = root.findViewById(R.id.friendrv);
        friendList.hasFixedSize();
        friendList.setLayoutManager(new LinearLayoutManager(getContext()));

        friendAdapter = new FriendAdapter(this);
        friendList.setAdapter(friendAdapter);

        mViewModel.getFriendList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> databaseUsers) {
                friendAdapter.setItemList((ArrayList<User>) databaseUsers);
                friendList.setAdapter(friendAdapter);
            }
        });

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(getContext(), "Visiting " + mViewModel.getFriendList().getValue().get(clickedItemIndex).getUserName(), Toast.LENGTH_SHORT).show();
    }
}