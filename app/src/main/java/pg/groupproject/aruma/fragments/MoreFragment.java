package pg.groupproject.aruma.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.database.DataService;
import pg.groupproject.aruma.feature.database.DatabaseFixture;
import pg.groupproject.aruma.fragments.history.HistoryFragment;
import pg.groupproject.aruma.fragments.savedPoints.SavedPointsFragment;


public class MoreFragment extends Fragment {

	private final static int DIRECTORY_CHOOSER_IMPORT = 9000;
	private final static int DIRECTORY_CHOOSER_EXPORT = 9001;
	private DataService dataService;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_more, null);

		dataService = new DataService(getActivity().getApplicationContext());

		final RelativeLayout history = inflateView.findViewById(R.id.more_list_history);
		history.setOnClickListener(view -> loadFragment(new HistoryFragment()));

		final RelativeLayout savedPoints = inflateView.findViewById(R.id.more_list_saved_points);
		savedPoints.setOnClickListener(view -> loadFragment(new SavedPointsFragment()));

		final RelativeLayout importData = inflateView.findViewById(R.id.more_list_import);
		importData.setOnClickListener(view -> openDirectoryChooser(DIRECTORY_CHOOSER_IMPORT));

		final RelativeLayout exportData = inflateView.findViewById(R.id.more_list_export);
		exportData.setOnClickListener(view -> openDirectoryChooser(DIRECTORY_CHOOSER_EXPORT));

		//FIXME to remove after fixture no longer needed
		inflateView.findViewById(R.id.more_list_insert_fixture).setOnClickListener(view -> {
			final DatabaseFixture databaseFixture = new DatabaseFixture(getContext());
			databaseFixture.insertFixture();
			Toast.makeText(getContext(), "Fixture inserted", Toast.LENGTH_SHORT).show();
		});

		return inflateView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			final Uri uri = data.getData();
			final DocumentFile documentFile = DocumentFile.fromTreeUri(getActivity(), uri);
			switch (requestCode) {
				case DIRECTORY_CHOOSER_IMPORT:
					dataService.importData(documentFile);
					break;
				case DIRECTORY_CHOOSER_EXPORT:
					dataService.exportData(documentFile);
					break;
			}
		}
	}

	private void openDirectoryChooser(int requestCode) {
		final Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		startActivityForResult(Intent.createChooser(i, getResources().getString(R.string.select_directory)), requestCode);
	}

	private void loadFragment(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_container, fragment)
				.addToBackStack(null)
				.commit();
	}
}
