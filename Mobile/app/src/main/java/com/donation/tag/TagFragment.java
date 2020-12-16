package com.donation.tag;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donation.R;
import com.donation.database.TokenHandler;
import com.donation.model.in.TagModelIn;
import com.donation.model.in.UserModelIn;
import com.donation.model.in.UserTagModelIn;
import com.donation.model.out.UserTagModelOut;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceSession;
import com.donation.retrofit.ApiServiceTag;
import com.donation.retrofit.ApiServiceUser;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TagFragment extends Fragment {

    public TagFragment() {
        // Required empty public constructor
    }

    ArrayList<TagModelIn> tags;
    ArrayList<String> tagsName;
    ArrayList<Integer> userTags;
    ArrayList<String> userTagsName;
    RecyclerView recyclerView;
    TokenHandler tokenHandler;
    SingerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tokenHandler = new TokenHandler(this.getContext(), null, null, 1);
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceSession sessionService = retrofit.create(ApiServiceSession.class);
        Call<UserModelIn> callSession = sessionService.GetUserByToken(tokenHandler.getUserToken());
        callSession.enqueue(new Callback<UserModelIn>() {
            @Override
            public void onResponse(Call<UserModelIn> call, Response<UserModelIn> response) {
                UserModelIn user = response.body();
                userTags = new ArrayList<Integer>();
                ArrayList<UserTagModelIn> tagList = user.getTags();
                for (int i = 0; i < tagList.size(); i++) {
                    userTags.add(tagList.get(i).getTag());
                }
                cargarTags();
            }

            @Override
            public void onFailure(Call<UserModelIn> call, Throwable t) {
                Log.println(Log.ERROR, "login failure", t.getMessage());
            }
        });

        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.checkable_tags_list);
        Button btnAddTag = view.findViewById(R.id.ButtonAddTags);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTagsToUser();
            }
        });
    }

    private void cargarTags() {
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceTag tagService = retrofit.create(ApiServiceTag.class);
        Call<ArrayList<TagModelIn>> call = tagService.getAllTags();
        call.enqueue(new Callback<ArrayList<TagModelIn>>() {
            @Override
            public void onResponse(Call<ArrayList<TagModelIn>> call, Response<ArrayList<TagModelIn>> response) {
                tags = response.body();
                tagsName = new ArrayList<String>();
                for (int i = 0; i < tags.size(); i++) {
                    tagsName.add(tags.get(i).getName());
                }
                userTagsName = new ArrayList<String>();
                for (int i = 0; i < userTags.size(); i++) {
                    int index = obtenerIndexTag(userTags.get(i));
                    userTagsName.add(tags.get(index).getName());
                }

                mAdapter = new SingerAdapter(getContext(), tagsName, userTagsName);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

                recyclerView.setLayoutManager(mLayoutManager);

                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<TagModelIn>> call, Throwable t) {
                Log.println(Log.ERROR, "Back", "Not possible to close session");
            }
        });
    }

    private int obtenerIndexTag(int tagId) {
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getId() == tagId) {
                return i;
            }
        }
        return -1;
    }

    private void addTagsToUser() {
        int count = recyclerView.getAdapter().getItemCount();
        String token = tokenHandler.getUserToken();
        ArrayList<String> tagsSelected = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            CheckedTextView row = (CheckedTextView) recyclerView.getChildAt(i);
            if (row.isChecked()) {
                tagsSelected.add(row.getText().toString());
            }
        }
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceUser userService = retrofit.create(ApiServiceUser.class);
        UserTagModelOut userTagModelOut = new UserTagModelOut();
        userTagModelOut.setToken(token);
        userTagModelOut.setTags(tagsSelected);
        userTagModelOut.setUserTags(userTagsName);
        Call<ResponseBody> call = userService.addTags(userTagModelOut);
        call.enqueue(new Callback<ResponseBody>() {
            final Context context = getContext();
            final int duration = Toast.LENGTH_SHORT;

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                CharSequence text = "Tags agregada como favorita.";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CharSequence text = "Error al agregar las tags.";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}