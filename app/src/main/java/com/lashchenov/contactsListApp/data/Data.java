package com.lashchenov.contactsListApp.data;

import com.lashchenov.contactsListApp.pojo.User;

import java.util.List;

public interface Data {

    List<User> getUsers();

    void setUsers(List<User> userList);

    Boolean isEmpty();
}
