
package de.tum.os.drs.scanner.frags;

import de.tum.os.drs.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;




public class RentFragment extends Fragment {

    IBarcodeScanner barcodeScanner;

    public RentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rent_fragment_layout, container, false);
        Button btnAddRentDevice = (Button) rootView.findViewById(R.id.btnRentFragAdd);
        btnAddRentDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barcodeScanner != null) {
                    barcodeScanner.scanBarcode((Fragment) RentFragment.this);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            barcodeScanner = (IBarcodeScanner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IBarcodeScanner");
        }
    }

    public void addBarcode(final String barcode) {
        LinearLayout lLayout = (LinearLayout) getView().findViewById(R.id.linLayoutRentFragment);
        if (lLayout != null){
            lLayout.addView(new TextView(getActivity()){
                {
                    setText(barcode);
                }
            });
        }

    }

}