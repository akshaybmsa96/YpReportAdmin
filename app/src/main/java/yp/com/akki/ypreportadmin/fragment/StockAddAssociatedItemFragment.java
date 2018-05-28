package yp.com.akki.ypreportadmin.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import yp.com.akki.ypreportadmin.R;
import yp.com.akki.ypreportadmin.activity.StockEditActivity;
import yp.com.akki.ypreportadmin.adapter.CustomAdapterAssociatedItemAdd;
import yp.com.akki.ypreportadmin.lists.NonScrollListView;
import yp.com.akki.ypreportadmin.pojo.allItems.ItemsPojo;
import yp.com.akki.ypreportadmin.pojo.stock.StockAssociatedItemPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockAddAssociatedItemFragment extends DialogFragment implements SearchView.OnQueryTextListener{

    private NonScrollListView listView;
    private SearchView searchView;
    private Button buttonDone;
    private ItemsPojo itemsPojo;
    private String gData;
    private static ArrayList<StockAssociatedItemPojo> associatedItem;
    private CustomAdapterAssociatedItemAdd customAdapterAssociatedItemAdd;
    private OnCompleteListener mListener,mListener2;

    static public StockAddAssociatedItemFragment newInstance (String data, ArrayList<StockAssociatedItemPojo> items)
    {

        StockAddAssociatedItemFragment f = new StockAddAssociatedItemFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("data", data);
        f.setArguments(args);
        associatedItem = items;


        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick a style based on the num.
        setHasOptionsMenu(true);
        gData = getArguments().getString("data");
        initialize();


    }


    public StockAddAssociatedItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_add_associated_item, container, false);

        listView = (NonScrollListView) view.findViewById(R.id.listView);
        searchView = (SearchView)view.findViewById(R.id.searchView);
        buttonDone = (Button)view.findViewById(R.id.buttonDone);

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setupSearchView();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        customAdapterAssociatedItemAdd=new CustomAdapterAssociatedItemAdd(getContext(),itemsPojo.getItems(),getActivity(),associatedItem);
        listView.setAdapter(customAdapterAssociatedItemAdd);


        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener!=null)
                mListener.onComplete();
                if(mListener2!=null)
                mListener2.onComplete();
                dismiss();

            }
        });


        return view;
    }


    private void initialize() {

        JsonElement jsonElement=new JsonParser().parse(gData);
        GsonBuilder gsonBuilder=new GsonBuilder();
        Gson gson=gsonBuilder.create();
        itemsPojo=(gson.fromJson(jsonElement,ItemsPojo.class));

    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Item");
    }


    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        customAdapterAssociatedItemAdd.filter(newText);
        // Toast.makeText(this,newText,Toast.LENGTH_SHORT).show();
        return true;
    }



    public static interface OnCompleteListener
    {
        public abstract void onComplete();
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            if(context instanceof StockEditActivity) {
                   this.mListener2 = (OnCompleteListener)context;
            }

            else {
                this.mListener = (OnCompleteListener) getFragmentManager().findFragmentByTag("Stock Add");
            }
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement OnCompleteListener");
        }
    }

}
