package Helper.LoadHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoadFragment {
    public void loadFragment(FragmentManager fragmentManager, Fragment fragment, boolean isAppInitialized, int containerId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            // Add the fragment
            fragmentTransaction.add(containerId, fragment, fragment.getClass().getSimpleName());
        } else {
            // Replace the fragment and add the transaction to the back stack
            fragmentTransaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        fragmentTransaction.commit();
    }
}
