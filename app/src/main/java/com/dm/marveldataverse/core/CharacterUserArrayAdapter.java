package com.dm.marveldataverse.core;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.model.Character;

import java.util.ArrayList;

public class CharacterUserArrayAdapter extends ArrayAdapter {

    public CharacterUserArrayAdapter(Context context, ArrayList<Pair<Character, Long>> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final LayoutInflater INFLATER = LayoutInflater.from(this.getContext());
        final Pair<Character, Long> par = (Pair<Character, Long>) this.getItem(position);

        if (view == null) {
            view = INFLATER.inflate(R.layout.entry_character_user, null);
        }

        final ImageView IV_FAV = view.findViewById(R.id.ivFav);
        final TextView TV_NAME = view.findViewById(R.id.lblName);

        if (par.second != -1) {
            IV_FAV.setImageResource(R.drawable.ic_estrella_llena);
        } else {
            IV_FAV.setImageResource(R.drawable.ic_estrella_vacia);
        }

        TV_NAME.setText(par.first.getName());

        return view;
    }
}
