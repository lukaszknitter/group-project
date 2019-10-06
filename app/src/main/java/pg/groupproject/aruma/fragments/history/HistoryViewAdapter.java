package pg.groupproject.aruma.fragments.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.fragments.history.HistoryContent.HistoryViewModel;
import pg.groupproject.aruma.fragments.history.HistoryFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HistoryViewModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.ViewHolder> {

	private final HistoryFragment.OnListChangedCallbackInterface mCallbackInterface;
	private final OnListFragmentInteractionListener mListener;
	private List<HistoryViewModel> mValues;

	public HistoryViewAdapter(List<HistoryViewModel> items,
	                          OnListFragmentInteractionListener listener,
	                          HistoryFragment.OnListChangedCallbackInterface callbackInterface) {
		mValues = items;
		mListener = listener;
		mCallbackInterface = callbackInterface;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_history_row, parent, false);
		return new ViewHolder(view, mCallbackInterface);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.mItem = mValues.get(position);
		holder.title.setText(mValues.get(position).getTitle());
		holder.description.setText(mValues.get(position).getDescription());

		holder.mView.setOnClickListener(v -> {
			if (null != mListener) {
				// Notify the active callbacks interface (the activity, if the
				// fragment is attached to one) that an item has been selected.
				mListener.onListFragmentInteraction(holder.mItem);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mValues.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private final View mView;
		private final TextView title;
		private final TextView description;
		private HistoryViewModel mItem;

		public ViewHolder(View view, HistoryFragment.OnListChangedCallbackInterface callback) {
			super(view);
			mView = view;
			title = view.findViewById(R.id.history_row_title);
			description = view.findViewById(R.id.history_row_description);
			final ImageButton edit = view.findViewById(R.id.history_row_edit_button);
			final ImageButton delete = view.findViewById(R.id.history_row_delete_button);
			edit.setOnClickListener(v -> callback.onListElementEdit(mItem.getId()));
			delete.setOnClickListener(v -> callback.onListElementDelete(mItem.getId()));
		}
	}
}
