package pg.groupproject.aruma.feature.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionChecker {

	static int MY_PERMISSIONS_REQUEST_ALL_REQUEST_CODE = 1;

	Activity activity;

	public void checkPermissions() {
		String[] requestedPermissions = getRequestedPermissions();
		if (ArrayUtils.isNotEmpty(requestedPermissions)) {
			ActivityCompat.requestPermissions(activity, requestedPermissions, MY_PERMISSIONS_REQUEST_ALL_REQUEST_CODE);
		}
	}

	public boolean permissionsGranted() {
		return permissionGranted(ACCESS_FINE_LOCATION) && permissionGranted(ACCESS_COARSE_LOCATION)
				&& permissionGranted(READ_EXTERNAL_STORAGE) && permissionGranted(WRITE_EXTERNAL_STORAGE);
	}

	private String[] getRequestedPermissions() {
		List<String> requestedPermissions = new ArrayList<>();
		if (!permissionGranted(ACCESS_FINE_LOCATION) || !permissionGranted(ACCESS_COARSE_LOCATION)) {
			requestedPermissions.add(ACCESS_FINE_LOCATION);
			requestedPermissions.add(ACCESS_COARSE_LOCATION);
		}
		if (!permissionGranted(READ_EXTERNAL_STORAGE) || !permissionGranted(WRITE_EXTERNAL_STORAGE)) {
			requestedPermissions.add(READ_EXTERNAL_STORAGE);
			requestedPermissions.add(WRITE_EXTERNAL_STORAGE);
		}
		return requestedPermissions.toArray(new String[0]);
	}

	private boolean permissionGranted(final String permission) {
		return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
	}
}
