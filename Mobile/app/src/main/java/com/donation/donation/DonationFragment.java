package com.donation.donation;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donation.database.MyDBHandler;
import com.donation.donation.DonationUtils.AddressViewModel;
import com.donation.R;
import com.donation.database.TokenHandler;
import com.donation.donation.DonationUtils.GridViewAdapter;
import com.donation.model.AddressModel;
import com.donation.model.in.TagModelIn;
import com.donation.model.in.UserModelIn;
import com.donation.model.out.DonationModelOut;
import com.donation.model.out.FileModelOut;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonation;
import com.donation.retrofit.ApiServiceSession;
import com.donation.retrofit.ApiServiceTag;
import com.donation.retrofit.ApiServiceUser;
import com.donation.tag.SingerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;


public class DonationFragment extends Fragment {

    int PICK_IMAGE = 100;
    int TAKE_PHOTO = 200;
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_PICTURE = 102;
    int idUserSessionActive;
    Uri imageUri;
    GridView gvImagenes;
    ArrayList<TagModelIn> tags;
    ArrayList<String> tagsName;
    RecyclerView recyclerView;
    SingerAdapter mAdapter;
    List<Uri> listImages = new ArrayList<>();
    GridViewAdapter baseAdapter;
    AddressModel addressSelected;
    UserModelIn userIn;
    EditText txtName;
    EditText txtDescription;
    TextView txtSearchAddress;
    Retrofit retrofit;
    DonationModelOut modelOut;
    MyDBHandler dbHandler;
    TokenHandler tokenHandler;
    ApiServiceUser userService;
    ApiServiceSession serviceSession;
    ArrayList<FileModelOut> listFilesModelOut;
    String nameaux_ = "";
    String descriptionaux = "";
    View viewAux;
    UserModelIn userModelIn;
    private AddressViewModel viewModel;

    public DonationFragment() {
        retrofit = ApiClient.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                ArrayList<String> userTagsName = new ArrayList<String>();
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

        return inflater.inflate(R.layout.fragment_donation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.checkable_tags_list);
        final NavController navigation = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);
        userService = retrofit.create(ApiServiceUser.class);
        serviceSession = retrofit.create(ApiServiceSession.class);
        dbHandler = new MyDBHandler(this.getContext(), null, null, 1);
        tokenHandler = new TokenHandler(getContext(), null, null, 1);
        txtName = view.findViewById(R.id.txtDonationName);
        txtDescription = view.findViewById(R.id.txtDonationDescription);
        txtSearchAddress = view.findViewById(R.id.txt_searchAddress);
        if(viewModel.getAddressModel().getValue() == null) {
            txtSearchAddress.setVisibility(View.GONE);
        }else{
            txtSearchAddress.setVisibility(View.VISIBLE);
            txtSearchAddress.setText(viewModel.getAddressModel().getValue().getName());
        }
        Button confirmDonation = view.findViewById(R.id.ButtonConfirmDonation);
        Button btnLoadImage = view.findViewById(R.id.ButtonLoadImage);
        Button _btnLoadDirection = view.findViewById(R.id.btn_DirectionMap);
        LoadUserByLogged(tokenHandler.getUserToken());

        confirmDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CreateDonation(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ApiServiceDonation donationService = retrofit.create(ApiServiceDonation.class);
                if (modelOut != null) {
                    Call<ResponseBody> call = donationService.AddDonation(modelOut);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.message().equals("Bad Request")) {
                                Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), getResources().getString(R.string.donationSuccess), Toast.LENGTH_LONG).show();
                                navigation.popBackStack(R.id.nav_homeFragment, false);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.println(Log.ERROR, "Donation Failure", t.getMessage());
                            Toast.makeText(getContext(), "Error al ingresar la donacion", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                closeKeyboard();
            }
        });

        gvImagenes = view.findViewById(R.id.gvImagenes);
        recyclerView = view.findViewById(R.id.checkable_list);
        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImg();
            }
        });

        _btnLoadDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.nav_locationMapFragment);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            ClipData clipData = data.getClipData();
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                if (clipData == null) {
                    imageUri = data.getData();
                    listImages.add(imageUri);
                } else {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        listImages.add(clipData.getItemAt(i).getUri());
                    }
                }
            } else if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageUri = getImageUri(this.getContext(), imageBitmap);
                listImages.add(imageUri);
            }
            baseAdapter = new GridViewAdapter(this.getContext(), listImages);
            gvImagenes.setAdapter(baseAdapter);
        }
    }

    private void CreateDonation(View view) throws IOException {
        if (TextUtils.isEmpty(txtName.getText().toString())) {
            txtName.setError("Name should never be empty");
        } else {
            if (TextUtils.isEmpty(txtDescription.getText().toString())) {
                txtDescription.setError("Description should never be empty");
            } else {
                modelOut = new DonationModelOut();
                modelOut.setName(txtName.getText().toString());
                modelOut.setDescription(txtDescription.getText().toString());
                addressSelected = viewModel.getAddressModel().getValue();
                modelOut.setAddress(addressSelected);
                idUserSessionActive = userModelIn.getId();
                modelOut.setCreatorUserId(idUserSessionActive);
                modelOut.setFileList(CreateListFile());

                int count = recyclerView.getAdapter().getItemCount();
                ArrayList<String> tagsSelected = new ArrayList<String>();
                for (int i = 0; i < count; i++) {
                    CheckedTextView row = (CheckedTextView) recyclerView.getChildAt(i);
                    if (row.isChecked()) {
                        tagsSelected.add(row.getText().toString());
                    }
                }
                modelOut.setTags(tagsSelected);
            }
        }
    }


    private void LoadUserByLogged(String userToken) {
        ApiServiceSession serviceSession = retrofit.create(ApiServiceSession.class);
        Call<UserModelIn> call = serviceSession.GetUserByToken(userToken);
        call.enqueue(new Callback<UserModelIn>() {
            @Override
            public void onResponse(Call<UserModelIn> call, Response<UserModelIn> response) {
                if (response.isSuccessful()) {
                    userModelIn = response.body();
                } else {
                    Log.println(Log.INFO, "failure", response.message());
                    Toast.makeText(getContext(), "failure loading user logged", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModelIn> call, Throwable t) {
            }
        });
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private ArrayList<FileModelOut> CreateListFile() throws IOException {
        ArrayList<FileModelOut> files = new ArrayList<FileModelOut>();
        if (!listImages.isEmpty()) {
            for (int i = 0; i < listImages.size(); i++) {
                Uri _uriauxiliar = listImages.get(i);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), _uriauxiliar);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] _bytes = baos.toByteArray();
                String encImage = Base64.encodeToString(_bytes, Base64.DEFAULT);
                FileModelOut modelOut = new FileModelOut(getFileName(_uriauxiliar), encImage);
                files.add(modelOut);
            }
        }
        return files;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECCIONA LAS IMAGENES"), PICK_IMAGE);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, TAKE_PHOTO);
    }

    private void loadImg() {
        final CharSequence[] options = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOptions = new AlertDialog.Builder(this.getContext());
        alertOptions.setTitle("Seleccione una Opción");
        alertOptions.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("Tomar Foto")) {
                    takePhoto();
                } else {
                    openGallery();
                }
            }
        });
        alertOptions.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PICTURE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
        }
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}