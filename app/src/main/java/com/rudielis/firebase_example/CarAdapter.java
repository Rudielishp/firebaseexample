package com.rudielis.firebase_example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> cars;
    private Context context;

    public CarAdapter(Context context) {
        this.cars = null;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return (cars != null) ? cars.size() : 0;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_vehicle, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        holder.textViewPrecio.setText(car.precio.toString());
        holder.textViewNombre.setText(car.nombre);
        FirebaseHelper.setImage(car.url, context, holder.imageViewUrl);
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewPrecio;
        ImageView imageViewUrl;

        CarViewHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
            imageViewUrl = itemView.findViewById(R.id.imageViewUrl);
        }
    }
}
