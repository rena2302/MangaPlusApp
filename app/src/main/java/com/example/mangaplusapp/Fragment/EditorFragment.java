package com.example.mangaplusapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mangaplusapp.R;
import com.example.mangaplusapp.databinding.FragmentEditorCategoryBinding;
import com.example.mangaplusapp.databinding.FragmentEditorChapterBinding;
import com.example.mangaplusapp.databinding.FragmentEditorMangaBinding;
import com.example.mangaplusapp.object.Categories;
import com.example.mangaplusapp.object.Mangas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditorFragment extends Fragment {
    private View rootView;
    class MangaSession {
        private List<Categories> categoryList;
        private  Bundle extras;
        private String selectedCategoryId;
        FragmentEditorMangaBinding binding = FragmentEditorMangaBinding.inflate(getLayoutInflater());

        private MangaSession(Bundle extras){
            this.extras = extras;
            loadCategories();
            onClickEvent();
        }

        private void loadCategories() {
            //get categories from firebase
            categoryList = new ArrayList<>();
            //load fire db > categories --> categoryList
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    categoryList.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        //get Data
                        Categories category = ds.getValue(Categories.class);
                        //add to list
                        categoryList.add(category);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), R.string.loadingInterupted,Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void categoryPickDialog() {
            //get string Array from categoryList
            String[] categoriesString = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++){
                categoriesString[i] = categoryList.get(i).getNAME_CATEGORY();
            }

            //alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setTitle("Pick category")
                    .setItems(categoriesString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //handle item click
                            //get clicked item from list
                            String category = categoriesString[which];
                            //set category to textView
                            binding.editMangaCategory.setText(category);
                            selectedCategoryId = categoryList.get(which).getID_CATEGORY();

                        }
                    })
                    .show();
        }

        private void onClickEvent(){
            binding.editMangaSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateData();
                }
            });
            binding.editMangaCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryPickDialog();
                }
            });
            binding.editMangaPreCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    binding.editMangaPrice.setEnabled(isChecked);
                }
            });
        }

        private String manga = "";

        private void validateData() {
            /*before adding validate data*/
            //get Data
            if (binding.editMangaPreCheck.isChecked()) {
                if (TextUtils.isEmpty(binding.editMangaPrice.getText())) {
                    Toast.makeText(getContext(), R.string.fillAllField, Toast.LENGTH_LONG).show();
                }
            }
            manga = binding.editMangaName.getText().toString().trim();
            if(TextUtils.isEmpty(manga)){
                Toast.makeText(getContext(),R.string.fillAllField, Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(binding.editMangaCategory.getText())) {
                Toast.makeText(getContext(),R.string.fillAllField, Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(binding.editMangaDescription.getText())) {
                Toast.makeText(getContext(),R.string.fillAllField, Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(binding.editMangaPicture.getText())) {
                Toast.makeText(getContext(),R.string.fillAllField, Toast.LENGTH_LONG).show();
            } else {
                editMangaFireBase();
                binding.editMangaPicture.getText().clear();
                binding.editMangaCategory.setText("");
                binding.editMangaDescription.getText().clear();
                binding.editMangaName.getText().clear();
                binding.editMangaPreCheck.setChecked(false);
                binding.editMangaPrice.getText().clear();
                binding.editMangaPre.setEnabled(false);
            }
        }

        private void editMangaFireBase() {
            //setup info to add in firebase db
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("NAME_MANGA", ""+manga);
            hashMap.put("DESCRIPTION_MANGA", ""+binding.editMangaDescription.getText());
            hashMap.put("PICTURE_MANGA", ""+binding.editMangaPicture.getText());
            hashMap.put("CATEGORY_MANGA", ""+binding.editMangaCategory.getText());
            hashMap.put("ID_CATEGORY_MANGA",""+selectedCategoryId);
            if (binding.editMangaPreCheck.isChecked()){
                hashMap.put("PREMIUM_MANGA",true);
                hashMap.put("PRICE_MANGA", ""+binding.editMangaPrice.getText().toString());
            }
            else {
                hashMap.put("PREMIUM_MANGA",false);
                hashMap.put("PRICE_MANGA", "0");
            }

            //add to firebase db
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas").child(extras.getString("ID_MANGA"));
            reference.updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),R.string.manga_edit_successful, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),R.string.manga_edit_fail, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    class CategorySession{
        FragmentEditorCategoryBinding binding = FragmentEditorCategoryBinding.inflate(getLayoutInflater());
        private  Bundle extras;
        private CategorySession(Bundle extras){
            this.extras = extras;
            onClickEvent();
        }
        private void onClickEvent(){
            binding.editCategorySubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateData();
                }
            });
        }

        private String category = "";
        private void validateData() {
            /*before adding validate data*/

            //get Data
            category = binding.editCategoryName.getText().toString().trim();
            if(TextUtils.isEmpty(category)){
                Toast.makeText(getContext(),R.string.fillCategory, Toast.LENGTH_LONG).show();
            }
            else {
                editCategoryFireBase();
            }
        }

        private void editCategoryFireBase() {

            //setup info to add in firebase db
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("NAME_CATEGORY", ""+category);
            //hashMap.put("UID_CATEGORY", firebaseAuth.getUid());

            //add to firebase db
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories").child(extras.getString("ID_CATEGORY"));
            reference.updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //category add success
                            Toast.makeText(getContext(),R.string.category_edit_successful, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //category add fail
                            Toast.makeText(getContext(),R.string.category_edit_fail, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    class ChapterSession{
        FragmentEditorChapterBinding binding = FragmentEditorChapterBinding.inflate(getLayoutInflater());
        private List<Mangas> truyenTranhList;
        private  Bundle extras;
        private static final int PICK_PDF_FILE = 1;
        private Uri pdfUri;
        private String selectedMangaId;
        private ChapterSession(Bundle extras){
            this.extras = extras;
            loadMangas();
            onClickEvent();
        }

        private void loadMangas() {
            //get mangas from firebase
            truyenTranhList = new ArrayList<>();
            //load fire db > mangas --> mangaList
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mangas");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    truyenTranhList.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        //get Data
                        Mangas truyenTranh = ds.getValue(Mangas.class);
                        //add to list
                        truyenTranhList.add(truyenTranh);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), R.string.loadingInterupted,Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void onClickEvent() {
            binding.editChapterSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateData();
                }
            });
            binding.editChapterPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickPdfFile();
                }
            });
            binding.editChapterManga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mangaPickDialog();
                }
            });
        }

        private void mangaPickDialog() {
            //get string Array from categoryList
            String[] mangasString = new String[truyenTranhList.size()];
            for (int i = 0; i < truyenTranhList.size(); i++){
                mangasString[i] = truyenTranhList.get(i).getNAME_MANGA();
            }

            //alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setTitle("Pick manga")
                    .setItems(mangasString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //handle item click
                            //get clicked item from list
                            String manga = mangasString[which];
                            //set category to textView
                            binding.editChapterManga.setText(manga);
                            selectedMangaId = truyenTranhList.get(which).getID_MANGA();
                        }
                    })
                    .show();
        }

        private void pickPdfFile() {
            pickPdfFileLauncher.launch("application/pdf");
        }
        // ActivityResultLauncher for picking PDF file
        private final ActivityResultLauncher<String> pickPdfFileLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            pdfUri = result;
                            Toast.makeText(getActivity(), R.string.chapterSelected, Toast.LENGTH_SHORT).show();
                            // Now you can do whatever you want with the selected PDF URI
                        }
                    }
                }
        );

        private String chapter = "";
        private void validateData() {
            /*before adding validate data*/

            //get Data
            chapter = binding.editChapterName.getText().toString().trim();
            if(TextUtils.isEmpty(chapter)){
                Toast.makeText(getContext(),R.string.fillChapter, Toast.LENGTH_SHORT).show();
            } else if (binding.editChapterManga.getText() == null) {
                Toast.makeText(getContext(), R.string.fillAllField, Toast.LENGTH_SHORT).show();
            } else {
                editChapterFireBase();
            }
        }

        private void editChapterFireBase() {

            //setup info to edit in firebase db
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("NAME_CHAPTER", ""+chapter);
            hashMap.put("MANGA_CHAPTER", ""+binding.editChapterManga.getText());
            hashMap.put("ID_MANGA_CHAPTER", ""+selectedMangaId);
            //hashMap.put("UID_MANGA", firebaseAuth.getUid());
            // Upload PDF file to Firebase Storage
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(pdfUri);
                if(inputStream != null){
                    StorageReference reference = FirebaseStorage.getInstance().getReference("chapter_pdfs").child(extras.getString("ID_CHAPTER")+".pdf");
                    reference.putStream(inputStream)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    updateDataInFirebase(hashMap);
                                }
                            });
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.file_not_found, Toast.LENGTH_SHORT).show();
            }
        }

        private void updateDataInFirebase(HashMap<String, Object> hashMap){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chapters").child(extras.getString("ID_CHAPTER"));
            databaseReference.updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), R.string.chapter_uploaded_successfully, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), R.string.chapter_upload_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("session_edit", Context.MODE_PRIVATE);
        Bundle extras = getArguments();
        /*Manga Session*/
        if(sharedPreferences.getString("session","").equals("manga")){
            MangaSession mangaSession = new MangaSession(extras);
            rootView = mangaSession.binding.getRoot();
            //Set text for editText
            mangaSession.binding.editMangaName.setText(extras.getString("NAME_MANGA"));
            mangaSession.binding.editMangaDescription.setText(extras.getString("DESCRIPTION_MANGA"));
            mangaSession.binding.editMangaPicture.setText(extras.getString("PICTURE_MANGA"));
            if(!Boolean.parseBoolean(extras.getString("PREMIUM_MANGA"))) {
                mangaSession.binding.editMangaPreCheck.setChecked(false);
                mangaSession.binding.editMangaPrice.setEnabled(false);
            }else {
                mangaSession.binding.editMangaPreCheck.setChecked(true);
                mangaSession.binding.editMangaPrice.setText(extras.getString("PRICE_MANGA"));
            }
            mangaSession.binding.editMangaPreCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!isChecked){
                        mangaSession.binding.editMangaPrice.getText().clear();
                    }
                }
            });
        }
        /*Category Session*/
        else if (sharedPreferences.getString("session","").equals("category")) {
            CategorySession categorySession = new CategorySession(extras);
            rootView = categorySession.binding.getRoot();
            //Set text for editText
            categorySession.binding.editCategoryName.setText(extras.getString("NAME_CATEGORY"));
        }
        /*Chapter Session*/
        else if (sharedPreferences.getString("session","").equals("chapter")) {
            ChapterSession chapterSession = new ChapterSession(extras);
            rootView = chapterSession.binding.getRoot();
            //Set text for editText
            chapterSession.binding.editChapterName.setText(extras.getString("NAME_CHAPTER"));
            chapterSession.binding.editChapterManga.setText(extras.getString("MANGA_CHAPTER"));
        }
        // Inflate the layout for this fragment
        return rootView;
    }
}