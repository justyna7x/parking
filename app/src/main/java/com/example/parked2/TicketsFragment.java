package com.example.parked2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TicketsFragment extends Fragment {
    private View TicketsView;
    private RecyclerView myTicketsList;

    private DatabaseReference Ticketsref, Tickestsref2;

    private FirebaseAuth mAuth;
    private String uid;


    public TicketsFragment() {
        // Required empty public constructor
    }


    public static TicketsFragment newInstance(String param1, String param2) {
        TicketsFragment fragment = new TicketsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        TicketsView =  inflater.inflate(R.layout.fragment_tickets2, container, false);
        myTicketsList = (RecyclerView) TicketsView.findViewById(R.id.ticketsList);
        myTicketsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
       Ticketsref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Tickets");
       Tickestsref2 = FirebaseDatabase.getInstance().getReference().child("Users");

        return TicketsView;
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Ticket>()
                .setQuery(Ticketsref, Ticket.class)
                .build();

        FirebaseRecyclerAdapter<Ticket, TicketsViewHolder> adapter = new FirebaseRecyclerAdapter<Ticket, TicketsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TicketsViewHolder holder, int position, @NonNull Ticket ticket) {
                String ticketsID = getRef(position).getKey();
                Ticketsref.child(ticketsID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("regPlate")){
                            String regPlates = snapshot.child("regPlate").getValue().toString();
                            String startTimes = snapshot.child("startTime").getValue().toString();
                            String endTimes = snapshot.child("endTime").getValue().toString();
                            String durations = snapshot.child("duration").getValue().toString();
                            String zones = snapshot.child("zone").getValue().toString();

                            holder.zone.setText("Zone: "+zones);
                            holder.startTime.setText("Start Time: "+startTimes);
                            holder.endTime.setText("Expiration time: "+endTimes);
                            holder.regPlate.setText("Registration Plate Number: "+regPlates);
                            holder.duration.setText("Duration: "+durations+" hours");
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public TicketsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_ticket, viewGroup, false);
                TicketsViewHolder viewHolder = new TicketsViewHolder(view);
                return viewHolder;
            }
        };
        myTicketsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class TicketsViewHolder extends RecyclerView.ViewHolder{
        TextView zone, startTime, endTime, regPlate, duration;

        public TicketsViewHolder(@NonNull View itemView){
            super(itemView);

            zone = itemView.findViewById(R.id.zoneR);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            regPlate = itemView.findViewById(R.id.regPlateR);
            duration = itemView.findViewById(R.id.duration);
        }
    }
}
