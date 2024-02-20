package Helper.PermissionHelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private Context context;
    private PermissionListener listener;

    public PermissionHelper(Context context, PermissionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                // Quyền đã được cấp
                listener.onPermissionGranted();
            }
        } else {
            // Đối với các phiên bản Android dưới M, quyền đã được cấp sẵn khi cài đặt ứng dụng
            listener.onPermissionGranted();
        }
    }
    public boolean hasStoragePermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp
                listener.onPermissionGranted();
            } else {
                // Quyền không được cấp
                listener.onPermissionDenied();
            }
        }
    }

    public interface PermissionListener {
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
