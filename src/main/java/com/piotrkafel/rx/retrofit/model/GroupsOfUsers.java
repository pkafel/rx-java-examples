package com.piotrkafel.rx.retrofit.model;


import java.util.Set;

public class GroupsOfUsers {

    private final Set<Integer> groupIds;

    private final Pagination pagination;

    public GroupsOfUsers(Set<Integer> groupIds, Pagination pagination) {
        this.groupIds = groupIds;
        this.pagination = pagination;
    }

    public Set<Integer> getGroupIds() {
        return groupIds;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
