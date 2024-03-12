package com.example.mangaplusapp.Activity.User;
        import android.app.Dialog;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.view.Gravity;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.widget.LinearLayout;
        import com.example.mangaplusapp.Activity.Base.BaseActivity;
        import com.example.mangaplusapp.Adapter.ChapterAdapter;
        import com.example.mangaplusapp.R;
        import com.example.mangaplusapp.databinding.ActivityMangaDetailBinding;
        import com.example.mangaplusapp.object.Chapters;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import java.util.ArrayList;
        import java.util.List;

public class MangaDetailActivity extends BaseActivity {
    Intent intent;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    ActivityMangaDetailBinding binding;
    private List<Chapters> chapterList = new ArrayList<>();
    // Khởi tạo adapter trước khi hiển thi
    private ChapterAdapter chapterAdapter ;
    String mangaId, nameManga, mangaPicture, mangaDescription;
    private Boolean mangaPremium;
    public interface OnPurchasedMangaIdsLoadedListener {
        void onPurchasedMangaIdsLoaded(Boolean premium);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        intent = getIntent();
        mangaPremium = Boolean.parseBoolean(intent.getStringExtra("PREMIUM_MANGA"));
        setContentView(binding.getRoot());
        mangaId = intent.getStringExtra("ID_MANGA");
        nameManga = intent.getStringExtra("NAME_MANGA");
        mangaPicture = intent.getStringExtra("PICTURE_MANGA");
        mangaDescription = intent.getStringExtra("DESCRIPTION_MANGA");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }
    private void showdialog()
    {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_payment);
        LinearLayout back=dialog.findViewById(R.id.BackToProduct);
        back.setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}