package com.lashchenov.contactsListApp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lashchenov.contactsListApp.R;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private List<User> userList = new ArrayList<>();
    private OnUserClickListener onUserClickListener;
    private Context context;


    public UsersAdapter(OnUserClickListener onClickListener, Context context) {
        this.onUserClickListener = onClickListener;
        this.context = context;
    }


    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item_view, parent, false);
        return new UsersViewHolder(view);
    }


    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void setItems(Collection<User> users) {
        userList.addAll(users);
        notifyDataSetChanged();
    }


    public void clearItems() {
        userList.clear();
        notifyDataSetChanged();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameTextView) TextView name;
        @BindView(R.id.emailTextView) TextView email;
        @BindView(R.id.stateTextView) TextView state;

        public UsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = userList.get(getLayoutPosition());
                    onUserClickListener.onUserClick(user);
                }
            });
        }

        public void bind(User user) {
            name.setText(user.getName());
            email.setText(user.getEmail());
            if (user.getActive()) {
                state.setText("active");
                state.setTextColor(context.getResources().getColor(R.color.activeUser));
            }
            else {
                state.setText("inactive");
                state.setTextColor(context.getResources().getColor(R.color.inactiveUser));
            }
        }
    }


    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}
