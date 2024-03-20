package com.example.mangaplusapp.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangaplusapp.Activity.User.MangaDetailActivity;
import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.ItemFavoriteBinding;
import com.example.mangaplusapp.object.Mangas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{
    List<Mangas> mangasList;
    Context context;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    ItemFavoriteBinding binding;
    public FavoriteAdapter(Context context){
        this.context = context;
    }
    public void setData(List<Mangas> mangasList){
        this.mangasList = mangasList;
    }
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        binding = ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false);
        return new FavoriteViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Mangas mangas = mangasList.get(position);
        setFavorite(mangas.getID_MANGA(), holder);
        holder.favoName.setText(mangas.getNAME_MANGA());
        holder.favoPrice.setText("Price: " + mangas.getPRICE_MANGA() +"đ");
        Glide.with(context)
                .load(mangas.getPICTURE_MANGA())
                .into(holder.favoImg);
        holder.favoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite(mangas.getID_MANGA(), holder);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mangasList != null) return mangasList.size();
        return 0;
    }
    protected void removeFromFavorite(String mangaIdToRemove, FavoriteViewHolder holder){
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(context,"You're not logged in", Toast.LENGTH_SHORT).show();
            return;
        }else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Favorites").child(mangaIdToRemove)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            holder.itemView.setVisibility(View.GONE);
                            Toast.makeText(context, "Remove from favorite successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to remove from favorite", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
    private void toggleFavorite(String mangaId, FavoriteViewHolder holder) {
        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("Favorites");
        userFavoritesRef.child(mangaId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().getKey();
                        if (dataSnapshot.exists()) {
                            // Manga is already in favorites, remove it
                            removeFromFavorite(mangaId, holder);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    private void setFavorite(String mangaId, FavoriteViewHolder holder){
        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUser.getUid()).child("Favorites");
        userFavoritesRef.child(mangaId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            holder.favoButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C3662D")));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView favoImg;
        TextView favoName, favoPrice;
        ImageButton favoButton;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favoButton = (ImageButton) itemView.findViewById(R.id.itemFavoButton);
            favoImg = (ImageView) itemView.findViewById(R.id.itemFavoImg);
            favoName = (TextView) itemView.findViewById(R.id.itemFavoName);
            favoPrice = (TextView) itemView.findViewById(R.id.itemFavoPrice);
        }
    }
}
