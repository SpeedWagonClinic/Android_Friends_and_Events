package com.example.finalproject;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of friends with selectable checkboxes in a RecyclerView.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private final List<Friend> friendList;
    private final List<Integer> selectedIds;

    /**
     * Constructs the FriendsAdapter.
     *
     * @param friendList A list of friends to display.
     * @param selectedIds A list of IDs that are initially selected.
     */
    public FriendsAdapter(List<Friend> friendList, List<Integer> selectedIds) {
        this.friendList = friendList;
        this.selectedIds = selectedIds == null ? new ArrayList<>() : selectedIds;
        Log.d("FriendsAdapter", "Initialized with " + friendList.size() + " friends and " + this.selectedIds.size() + " selected IDs.");
    }

    /**
     * Updates the list of friends and the selected IDs.
     *
     * @param newFriends A new list of friends.
     * @param newSelectedIds A new list of selected IDs.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateFriendsList(List<Friend> newFriends, List<Integer> newSelectedIds) {
        friendList.clear();
        friendList.addAll(newFriends);
        selectedIds.clear();
        selectedIds.addAll(newSelectedIds);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.textViewFriendName.setText(friend.getName());
        holder.textViewFriendPhone.setText(friend.getPhone());
        holder.textViewFriendGender.setText(friend.getGender());
        holder.checkBoxSelect.setChecked(selectedIds.contains(friend.getId()));

        // Prevent triggering the listener while binding
        holder.checkBoxSelect.setOnCheckedChangeListener(null);
        holder.checkBoxSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            friend.setSelected(isChecked);
            if (isChecked) {
                if (!selectedIds.contains(friend.getId())) {
                    selectedIds.add(friend.getId());
                }
            } else {
                selectedIds.remove(Integer.valueOf(friend.getId()));
            }
        });
    }

    /**
     * Retrieves the IDs of all selected friends.
     *
     * @return A list of IDs of selected friends.
     */
    public List<Integer> getSelectedFriendsIds() {
        List<Integer> selectedIds = new ArrayList<>();
        for (Friend friend : friendList) {
            if (friend.isSelected()) {
                selectedIds.add(friend.getId());
            }
        }
        return selectedIds;
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    /**
     * ViewHolder for friend items.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriendName;
        TextView textViewFriendPhone;
        TextView textViewFriendGender;
        CheckBox checkBoxSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewFriendName = itemView.findViewById(R.id.textViewFriendName);
            textViewFriendPhone = itemView.findViewById(R.id.textViewFriendPhone);
            textViewFriendGender = itemView.findViewById(R.id.textViewFriendGender);
            checkBoxSelect = itemView.findViewById(R.id.checkBoxSelect);
        }
    }
}
