package com.example.stock.ui.member;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.stock.AddMember;
import com.example.stock.DB.Member;
import com.example.stock.DB.Stock;
import com.example.stock.DB.StockDbHelper;
import com.example.stock.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MemberFragment extends Fragment {

    public ArrayList<Member> members = new ArrayList();

    private class MemberAdapter extends ArrayAdapter<Member> {
        private int resourceLayout;
        private Context mContext;

        public MemberAdapter(Context context, int resource, List<Member> items) {
            super(context, resource, items);
            this.resourceLayout = resource;
            this.mContext = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(mContext);
                v = vi.inflate(resourceLayout, null);
            }

            Member member = getItem(position);

            if (member != null) {
                TextView title = v.findViewById(R.id.member_name);

                if (title != null) {
                    title.setText(member.getFirstName() + " " + member.getLastName());
                }
                TextView phone = v.findViewById(R.id.member_phone);

                if(phone != null) {
                    phone.setText(member.getPhone());
                }


            }

            return v;
        }

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StockDbHelper helper = new StockDbHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        members = Member.find(db);

        View root = inflater.inflate(R.layout.fragment_member, container, false);
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("members " +  members);
        FloatingActionButton fab = view.findViewById(R.id.add);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddMember.class);
                        startActivity(intent);
                    }
                }
        );
        MemberFragment.MemberAdapter adapter = new MemberFragment.MemberAdapter(getContext(),
                R.layout.member_item, members);
        ListView membersList = view.findViewById(R.id.members);
        membersList.setAdapter(adapter);

        if(members.size() == 0) {
            TextView empty = view.findViewById(R.id.empty);
            empty.setText("Aucun membre trouvé, cliquez sur le bouton Ajouter pour créer votre premier!");
        }
    }
}