package com.example.mobiledatabasing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayActivity extends AppCompatActivity {

    ListView lvUsers;

    DbHelper db;

    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> all_users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        db = new DbHelper(this);

        lvUsers = findViewById(R.id.lvUsers);

        fetch_users();

    }

    private void fetch_users() {
        all_users = db.getAllUsers();

        adapter = new ListViewAdapter(this, R.layout.adapter_users, all_users);
        lvUsers.setAdapter(adapter);
        registerForContextMenu(lvUsers);
    }

    private class ListViewAdapter extends ArrayAdapter {
        LayoutInflater inflater;
        TextView tvUsername, tvFullname;
        ImageView btnEdit, btnDelete;

        ArrayList<HashMap<String, String>> objects;

        public ListViewAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);

            inflater = LayoutInflater.from(context);

            this.objects = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.adapter_users, parent, false);

            tvUsername = convertView.findViewById(R.id.tvUsername);
            tvFullname = convertView.findViewById(R.id.tvFullname);
            btnEdit = convertView.findViewById(R.id.btnEdit);
            btnDelete = convertView.findViewById(R.id.btnDelete);

            tvUsername.setText(objects.get(position).get(db.TBL_USERS_USERNAME));
            tvFullname.setText(objects.get(position).get(db.TBL_USERS_FULLNAME));

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USERS_ID));db.deleteUser(userID);
                    Toast.makeText(DisplayActivity.this, "User Successfully Deleted", Toast.LENGTH_SHORT).show();
                    fetch_users();
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USERS_ID));

                    Intent intent = new Intent(getContext(), EditUserActivity.class);
                    intent.putExtra(db.TBL_USERS_ID,userID);

                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.btnLogout:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
