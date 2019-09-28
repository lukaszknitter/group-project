package pg.groupproject.aruma.feature.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.location.Location;
import pg.groupproject.aruma.feature.location.LocationService;
import pg.groupproject.aruma.feature.place.Place;
import pg.groupproject.aruma.feature.place.PlaceService;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;

public class DataService {

	private final static String CSV_SEPARATOR = ",";
	private final static String CSV_MIME_TYPE = "RFC7111";
	private LocationService locationService;
	private RouteService routeService;
	private PlaceService placeService;
	private Context context;

	public DataService(Context context) {
		locationService = new LocationService(context);
		routeService = new RouteService(context);
		placeService = new PlaceService(context);
		this.context = context;
	}

	public void importData(@NonNull final DocumentFile documentFile) {
		try {
			// Due to foreign keys, the following order must be followed.
			clearDatabase();
			importDataFromFile(documentFile, Filename.PLACE);
			importDataFromFile(documentFile, Filename.ROUTE);
			importDataFromFile(documentFile, Filename.LOCATION);
			Toast.makeText(context,
					context.getResources().getString(R.string.import_success_message),
					Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Log.e(DataService.class.toString(), "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e(DataService.class.toString(), "Can not read file: " + e.toString());
		}
	}

	public void exportData(DocumentFile documentFile) {
		try {
			exportDataToFile(documentFile, Filename.LOCATION);
			exportDataToFile(documentFile, Filename.PLACE);
			exportDataToFile(documentFile, Filename.ROUTE);
			Toast.makeText(context,
					context.getResources().getString(R.string.export_success_message),
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Log.e(DataService.class.toString(), "File write failed: " + e.toString());
		}
	}

	private void clearDatabase() {
		locationService.deleteAll();
		routeService.deleteAll();
		placeService.deleteAll();
	}

	private void exportDataToFile(DocumentFile documentFile, Filename filename) throws IOException {
		final DocumentFile file = createIfNotExist(documentFile, filename);
		final OutputStream outputStream = context.getContentResolver().openOutputStream(file.getUri());

		final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
		final BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

		switch (filename) {
			case LOCATION:
				final List<Location> locations = locationService.getAll();
				bufferedWriter.write(Location.csvHeader(CSV_SEPARATOR));
				bufferedWriter.newLine();

				for (Location location : locations) {
					bufferedWriter.write(location.toCSVRow(CSV_SEPARATOR));
					bufferedWriter.newLine();
				}
				break;
			case PLACE:
				final List<Place> places = placeService.getAll();
				bufferedWriter.write(Place.csvHeader(CSV_SEPARATOR));
				bufferedWriter.newLine();

				for (Place place : places) {
					bufferedWriter.write(place.toCSVRow(CSV_SEPARATOR));
					bufferedWriter.newLine();
				}
				break;
			case ROUTE:
				final List<Route> routes = routeService.getAll();
				bufferedWriter.write(Route.csvHeader(CSV_SEPARATOR));
				bufferedWriter.newLine();

				for (Route route : routes) {
					bufferedWriter.write(route.toCSVRow(CSV_SEPARATOR));
					bufferedWriter.newLine();
				}
				break;
		}

		bufferedWriter.close();
		outputStreamWriter.close();
		outputStream.close();

		Log.i(DataService.class.toString(), filename.getName() + " exported!");
	}

	private void importDataFromFile(DocumentFile documentFile, Filename filename) throws IOException {
		final DocumentFile fileIfExists = getFileIfExists(documentFile, filename);
		final InputStream inputStream = context.getContentResolver().openInputStream(fileIfExists.getUri());

		if (inputStream != null) {
			final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String receiveString;

			bufferedReader.readLine(); //header

			while ((receiveString = bufferedReader.readLine()) != null) {
				final String[] values = receiveString.split(CSV_SEPARATOR);

				switch (filename) {
					case LOCATION:
						final Location location = Location.fromCSVRow(values);
						locationService.insert(location, location.getRouteId());
						break;
					case PLACE:
						final Place place = Place.fromCSVRow(values);
						placeService.insert(place);
						break;
					case ROUTE:
						final Route route = Route.fromCSVRow(values);
						routeService.insert(route);
						break;
				}

			}
			inputStream.close();
			Log.i(DataService.class.toString(), filename.getName() + " imported!");
		}
	}

	private DocumentFile getFileIfExists(DocumentFile documentFile, Filename filename) throws FileNotFoundException {
		final String name = filename.getName();
		final DocumentFile file = documentFile.findFile(name);
		if (file != null && file.exists()) {
			return file;
		}
		throw new FileNotFoundException(documentFile.getUri() + name);
	}

	private DocumentFile createIfNotExist(DocumentFile documentFile, Filename filename) {
		final String name = filename.getName();
		final DocumentFile file = documentFile.findFile(name);
		if (file == null || !file.exists()) {
			return documentFile.createFile(CSV_MIME_TYPE, name);
		}
		return file;
	}

	@Getter
	@AllArgsConstructor
	private enum Filename {
		LOCATION("location.csv"),
		PLACE("place.csv"),
		ROUTE("route.csv");

		private final String name;
	}
}
