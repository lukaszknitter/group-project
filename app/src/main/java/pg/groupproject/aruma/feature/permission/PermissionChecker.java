package pg.groupproject.aruma.feature.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionChecker {

	static int MY_PERMISSIONS_REQUEST_ALL = 1;

	Activity activity;

	public void checkPermissions() {
		String[] requestedPermissions = getRequestedPermissions();
		if (ArrayUtils.isNotEmpty(requestedPermissions)) {
			ActivityCompat.requestPermissions(activity, requestedPermissions, MY_PERMISSIONS_REQUEST_ALL);
		}
	}

	private String[] getRequestedPermissions() {
		List<String> requestedPermissions = new ArrayList<>();
		if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestedPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
			requestedPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
		}
		if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			requestedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
		return requestedPermissions.toArray(new String[0]);
	}
}
