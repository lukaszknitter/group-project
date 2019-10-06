package pg.groupproject.aruma.fragments.history;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;
import pg.groupproject.aruma.fragments.common.EditRouteFragment;
import pg.groupproject.aruma.fragments.history.HistoryContent.HistoryViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HistoryFragment extends Fragment {

	private final RouteService routeService;
	// TODO: Customize parameters
	private int mColumnCount = 1;
	private OnListFragmentInteractionListener mListener;
	private ViewGroup container;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public HistoryFragment() {
		routeService = new RouteService(getContext());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		this.container = container;
		View view = inflater.inflate(R.layout.fragment_history_list, container, false);

		// Set the adapter
		if (view instanceof RecyclerView) {
			Context context = view.getContext();
			final List<Route> routes = getHistoryRoutes(context);
			RecyclerView recyclerView = (RecyclerView) view;
			if (mColumnCount <= 1) {
				recyclerView.setLayoutManager(new LinearLayoutManager(context));
			} else {
				recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
			}

			final OnListChangedCallbackInterface callbackInterface = new OnListChangedCallbackInterface() {
				@Override
				public void onListElementDelete(int listItemId) {
					handleOnListElementDelete(listItemId);
				}

				@Override
				public void onListElementEdit(int listItemId) {
					handleOnListElementEdit(listItemId);
				}
			};

			recyclerView.setAdapter(new HistoryViewAdapter(HistoryContent.createContent(routes, getResources()), mListener, callbackInterface));
		}
		return view;
	}

	private List<Route> getHistoryRoutes(Context context) {
		final RouteService routeService = new RouteService(context);
		return routeService.getAll();
	}

	private void handleOnListElementDelete(int listItemId) {
		DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					routeService.delete(listItemId);
					getActivity().getSupportFragmentManager()
							.beginTransaction()
							.detach(this)
							.attach(this)
							.commit();
					break;
				case DialogInterface.BUTTON_NEUTRAL:
				case DialogInterface.BUTTON_NEGATIVE:
					break;
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage("Czy na pewno chcesz usunąć tę trasę?")
				.setPositiveButton("Tak", dialogClickListener)
				.setNegativeButton("Nie", dialogClickListener)
				.show();
	}

	private void handleOnListElementEdit(int listItemId) {
		Bundle bundle = new Bundle();
		bundle.putInt("routeId", listItemId);
		EditRouteFragment editRouteFragment = new EditRouteFragment();
		editRouteFragment.setArguments(bundle);
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(container.getId(), editRouteFragment, "editRouteFragment")
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnListFragmentInteractionListener) {
			mListener = (OnListFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	interface OnListChangedCallbackInterface {
		void onListElementDelete(int listItemId);

		void onListElementEdit(int listItemId);
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnListFragmentInteractionListener {
		// TODO: Update argument type and name
		void onListFragmentInteraction(HistoryViewModel item);
	}
}
