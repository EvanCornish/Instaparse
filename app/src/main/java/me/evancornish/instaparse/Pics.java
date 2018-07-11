package me.evancornish.instaparse;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Pics extends Fragment {

    Button bNew;
    Button bOld;
    private PicMethodSelector selector;

    public Pics() {
        // Required empty public constructor
    }

    public static Pics newInstance() {
        Pics fragment = new Pics();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bNew = view.findViewById(R.id.bNew);
        bOld = view.findViewById(R.id.bOld);

        bNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector.picMethodSelected("new");
            }
        });
        bOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector.picMethodSelected("old");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PicMethodSelector) {
            selector = (PicMethodSelector) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement Pics.PicMethodSelector");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface PicMethodSelector {
        void picMethodSelected(String method);
    }
}
