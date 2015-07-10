package com.myapps.whodeletedme;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class UserListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;
    private User mUser;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static UserListFragment newInstance(String param1, int param2) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
        mUser = (User)ParseUser.getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        switch(mParam2) {
            case 0:
                mAdapter = new UsersRecyclerAdapter(mUser.getNewFriends());
                break;
            case 1:
                mAdapter = new UsersRecyclerAdapter(mUser.getCurrentFriends());
                break;
            case 2:
                mAdapter = new UsersRecyclerAdapter(mUser.getDeletedMeFriends());
                break;
            default:
                mAdapter = new UsersRecyclerAdapter(mUser.getDeletedByMeFriends());

        }
        mRecyclerView.setAdapter(mAdapter);//setListAdapter(new UsersAdapter(getActivity(),R.layout.list_users_item, mUsers));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        if(mParam1.equals("0")) {
//            Intent i = new Intent(getActivity(), ProfileActivity.class);
//            i.putExtra("user", mUsers.get(position).getUsername());
//            startActivity(i);
//            return;
//        }
//
//        if (null != mListener) {
//            // Notify the active callbacks interface (the activity, if the
//            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
//            openConversation(mUsers.get(position).getUsername());
//        }
//    }


    static class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.user_name) TextView name;
        @InjectView(R.id.profileImageView) ImageView profileImage;
        public ViewHolderClickListener mListener;

        public UsersViewHolder(View itemView, ViewHolderClickListener listener) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

                mListener.onItemClick(getAdapterPosition());
        }

        public interface ViewHolderClickListener {
            void onItemClick(int position);


        }
    }

    private class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersViewHolder> {
        Transformation transformation;
        private List<FBFriend> mUsers;
        public UsersRecyclerAdapter(List<FBFriend> users) {
            super();
            mUsers = users;
            if(mUsers == null)
                mUsers = new ArrayList<>();
            transformation = new CircleTransformation(0,
                    getResources().getColor(R.color.accent_color_500));
        }

        @Override
        public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_users_item, parent, false);
            UsersViewHolder holder = new UsersViewHolder(v, new UsersViewHolder.ViewHolderClickListener() {
                @Override
                public void onItemClick(int position) {
//                    if(mParam1.equals("0")) {
//                        Intent i = new Intent(getActivity(), ProfileActivity.class);
//                        i.putExtra("user", mUsers.get(position).getUsername());
//                        startActivity(i);
//                    } else
//                        openConversation(mUsers.get(position).getUsername());
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(UsersViewHolder holder, int position) {
            String name = "";
            try {
                name = mUsers.get(position).fetchIfNeeded().getString("name");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.name.setText(name);
            if(mUsers.get(position).getProfilePicture()!=null)
                Picasso.with(getActivity())
                        .load(mUsers.get(position).getProfilePicture())
                        .transform(transformation)
                        .into(holder.profileImage);
            else
                holder.profileImage.setImageDrawable(getResources().getDrawable(R.drawable.no_profile_image));
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);

    }


}
