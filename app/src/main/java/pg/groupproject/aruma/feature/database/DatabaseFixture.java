package pg.groupproject.aruma.feature.database;

import android.content.Context;

import pg.groupproject.aruma.feature.location.Location;
import pg.groupproject.aruma.feature.location.LocationService;
import pg.groupproject.aruma.feature.place.Place;
import pg.groupproject.aruma.feature.place.PlaceService;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;

public class DatabaseFixture {
	private final LocationService locationService;
	private final PlaceService placeService;
	private final RouteService routeService;

	public DatabaseFixture(Context context) {
		locationService = new LocationService(context);
		placeService = new PlaceService(context);
		routeService = new RouteService(context);
	}

	public void insertFixture() {
		try {
			locationService.deleteAll();
			placeService.deleteAll();
			routeService.deleteAll();
			insertPlaces();
			insertFirstFinishedRoute();
			Thread.sleep(1000);
			insertSecondFinishedRoute();
			Thread.sleep(1000);
			insertUnfinishedFinishedRoute();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void insertPlaces() {
		final Place noweETI = Place.builder()
				.latitude(54.371695)
				.longitude(18.612301)
				.name("Nowe ETI")
				.build();

		final Place stareETI = Place.builder()
				.latitude(54.370785)
				.longitude(18.613101)
				.name("Stare ETI")
				.build();

		final Place mechaniczny = Place.builder()
				.latitude(54.371560)
				.longitude(18.614796)
				.name("Mechaniczny")
				.build();

		placeService.insert(noweETI);
		placeService.insert(stareETI);
		placeService.insert(mechaniczny);
	}

	private void insertFirstFinishedRoute() {
		final Route route = Route.builder()
				.distance(300)
				.finished(true)
				.name("Pierwsza zakończona trasa")
				.totalSeconds(300)
				.build();

		final long routeId = routeService.insert(route);

		final Location firstLocation = Location.builder()
				.altitude(100.1)
				.latitude(54.371931)
				.longitude(18.613200)
				.speed(12.1F)
				.build();

		final Location secondLocation = Location.builder()
				.altitude(110.1)
				.latitude(54.371284)
				.longitude(18.612851)
				.speed(12.1F)
				.build();

		final Location thirdLocation = Location.builder()
				.altitude(113.1)
				.latitude(54.371090)
				.longitude(18.613966)
				.speed(9.1F)
				.build();

		final Location fourthLocation = Location.builder()
				.altitude(107.1)
				.latitude(54.372199)
				.longitude(18.615483)
				.speed(10.1F)
				.build();

		final Location fifthLocation = Location.builder()
				.altitude(100.1)
				.latitude(54.371931)
				.longitude(18.613250)
				.speed(0.11F)
				.build();

		locationService.insert(firstLocation, routeId);
		locationService.insert(secondLocation, routeId);
		locationService.insert(thirdLocation, routeId);
		locationService.insert(fourthLocation, routeId);
		locationService.insert(fifthLocation, routeId);
	}

	private void insertSecondFinishedRoute() {
		final Route route = Route.builder()
				.distance(400)
				.finished(true)
				.name("Druga zakończona trasa")
				.totalSeconds(1000)
				.build();

		final long routeId = routeService.insert(route);

		final Location firstLocation = Location.builder()
				.altitude(200.1)
				.latitude(54.378319)
				.longitude(18.616066)
				.speed(10.1F)
				.build();

		final Location secondLocation = Location.builder()
				.altitude(150.1)
				.latitude(54.376616)
				.longitude(18.614944)
				.speed(17.1F)
				.build();

		final Location thirdLocation = Location.builder()
				.altitude(113.1)
				.latitude(54.375660)
				.longitude(18.614107)
				.speed(20.5F)
				.build();

		final Location fourthLocation = Location.builder()
				.altitude(113.1)
				.latitude(54.370345)
				.longitude(18.616016)
				.speed(25.5F)
				.build();

		final Location fifthLocation = Location.builder()
				.altitude(160.1)
				.latitude(54.370664)
				.longitude(18.617866)
				.speed(14.5F)
				.build();

		final Location sixthLocation = Location.builder()
				.altitude(190.1)
				.latitude(54.370419)
				.longitude(18.620112)
				.speed(9.5F)
				.build();

		final Location seventhLocation = Location.builder()
				.altitude(100.1)
				.latitude(54.369525)
				.longitude(18.624082)
				.speed(19.5F)
				.build();

		locationService.insert(firstLocation, routeId);
		locationService.insert(secondLocation, routeId);
		locationService.insert(thirdLocation, routeId);
		locationService.insert(fourthLocation, routeId);
		locationService.insert(fifthLocation, routeId);
		locationService.insert(sixthLocation, routeId);
		locationService.insert(seventhLocation, routeId);
	}

	private void insertUnfinishedFinishedRoute() {
		final Route route = Route.builder()
				.distance(200)
				.finished(false)
				.name("Trasa w trakcie")
				.totalSeconds(400)
				.build();

		final long routeId = routeService.insert(route);

		final Location firstLocation = Location.builder()
				.altitude(200.1)
				.latitude(54.370180)
				.longitude(18.610566)
				.speed(4.1F)
				.build();

		final Location secondLocation = Location.builder()
				.altitude(150.1)
				.latitude(54.369973)
				.longitude(18.612839)
				.speed(8.1F)
				.build();

		final Location thirdLocation = Location.builder()
				.altitude(113.1)
				.latitude(54.369929)
				.longitude(18.614135)
				.speed(10.5F)
				.build();

		locationService.insert(firstLocation, routeId);
		locationService.insert(secondLocation, routeId);
		locationService.insert(thirdLocation, routeId);
	}
}
