package Helper.LoadHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoadFragment {
    public void loadFragment(Fragment fragment, boolean isAppInitialized,int id) {

        FragmentTransaction fragmentTransaction = fragment.getParentFragmentManager().beginTransaction();
        if (isAppInitialized) {
            fragmentTransaction.add(id, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(id, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }
}
