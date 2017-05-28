package com.teambazomi.mealplanner;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.teambazomi.mealplanner.Recipe.recid;

public class ShoppingList extends Fragment {

    private RecyclerView mRecyclerView;
    private RemovableItemListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    List<ShoppingListItem> items = ShoppingListItem.getAll();
    List<ShoppingListItem> itemsTwo;


    EditText name;
    EditText amount;
    Spinner type;
    Button addItemButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shopping_list, container, false);

        addItemButton = (Button) view.findViewById(R.id.shoplist_add_ingredient);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShoppingListItem();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        // Populate "measurementType" drop-down spinner list with measurement types
        Spinner measurement_dropdown = (Spinner) v.findViewById(R.id.shoplist_measurementType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.measurements_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurement_dropdown.setAdapter(adapter);

        // List shopping list items from database using RecyclerView
        Collections.sort(items, new Comparator<ShoppingListItem>() {
            @Override
            public int compare(final ShoppingListItem object1, final ShoppingListItem object2) {
                return object1.name.compareTo(object2.name);
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.shopping_list);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new RemovableItemListAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new RemovableItemTouchHelper(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void addShoppingListItem(){
        View v = getView();
        // Get values for ingredient name, amount and measurement type
        // addIngredientButton = (Button) v.findViewById(R.id.add_ingredient);
        name = (EditText) v.findViewById(R.id.shoplist_ingredient);
        amount = (EditText) v.findViewById(R.id.shoplist_amount);
        type = (Spinner) v.findViewById( R.id.shoplist_measurementType);
        String nameTemp = name.getText().toString();
        int amountTemp = Integer.parseInt(amount.getText().toString());
        String typeTemp = type.getSelectedItem().toString();

        // Create new ShoppingListItem and save to ShoppingListItems
        ShoppingListItem temp = new ShoppingListItem(ShoppingListItem.itemid, recid, nameTemp, amountTemp, typeTemp);
        temp.save();
        ShoppingListItem.itemid++;
        itemsTwo = items;
        itemsTwo.add(temp);
        Collections.sort(itemsTwo, new Comparator<ShoppingListItem>() {
            @Override
            public int compare(final ShoppingListItem object1, final ShoppingListItem object2) {
                return object1.name.compareTo(object2.name);
            }
        });
        mAdapter.notifyDataSetChanged();
    }


}
