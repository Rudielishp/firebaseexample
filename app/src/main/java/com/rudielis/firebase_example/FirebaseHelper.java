package com.rudielis.firebase_example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private static final String NODE = "cars";
    private static final String IMAGE_URL = "imagenes/";
    public static int counter = 0;

    private static Uri shareUri;
    private static Bitmap bitmap;

    public static void getFeeds(final CarListener listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(NODE);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Car> cars = new ArrayList<>();

                for (DataSnapshot nodeDS : dataSnapshot.getChildren()) {
                    Car car = nodeDS.getValue(Car.class);
                    cars.add(car);
                }

                listener.onRetrieveCars(cars);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    public static void saveFeed(Car car) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(NODE);

        String id = reference.push().getKey();
        if (id != null) {
            reference.child(id).child("nombre").setValue(car.nombre);
            reference.child(id).child("precio").setValue(car.precio);
            reference.child(id).child("url").setValue(car.url);
        }
        else Log.e("NULLEXCEPTION", "CANNOT CREATE A NEW ID BECAUSE IS NULL");
    }

    public static Bitmap downloadImage(String imageUrl) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageReference = storage.getReference().child(imageUrl);

        imageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        return bitmap;

    }

    public static String uploadImage(Bitmap bitmap) {
        String imageUrl = IMAGE_URL + "example_" + counter + ".png";
        counter++;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageReference = storage.getReference()
                .child(imageUrl);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask task = imageReference.putBytes(data);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("TAG", taskSnapshot.toString());
            }
        });

        return imageUrl;
    }

    public static void setImage(String imageUrl, final Context context, final ImageView imageView) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageReference = storage.getReference().child(imageUrl);

        imageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(imageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
