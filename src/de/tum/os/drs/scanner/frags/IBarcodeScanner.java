package de.tum.os.drs.scanner.frags;


import android.support.v4.app.Fragment;

/**
 *Interface to be implemented by activity holding the {@link de.tum.os.drs.scanner.frags.RentFragment} and return Fragment.
 * Those fragments rely on this because they need to call the scanBarcode(...) method.
 */
public interface IBarcodeScanner {

    /**
     * Call to scan a barcode.
     * @param callingFrag - A ref to the fragment calling this method.
     */
    public void scanBarcode(Fragment callingFrag);
}
