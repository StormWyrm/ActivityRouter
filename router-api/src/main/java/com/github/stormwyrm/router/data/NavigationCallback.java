package com.github.stormwyrm.router.data;

public interface NavigationCallback {
    void onFound(PostCard postCard);

    void onLost(PostCard postCard);

    void onArrival(PostCard postCard);
}
