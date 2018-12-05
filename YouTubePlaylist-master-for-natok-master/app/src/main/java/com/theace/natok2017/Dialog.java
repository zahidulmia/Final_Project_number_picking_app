package com.theace.natok2017;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;



/**
 * Created by Tareq on 12/8/2016.
 */

public class Dialog extends DialogFragment {


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog, null);
        builder.setView(v);
       startActivity(new Intent(v.getContext(),playvideo.class));

        android.app.Dialog dialog=builder.create();
        return dialog;
    }

    }
