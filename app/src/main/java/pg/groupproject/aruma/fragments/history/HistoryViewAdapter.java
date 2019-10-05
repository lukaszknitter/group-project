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

	private final List<HistoryViewModel> mValues;
	private final OnListFragmentInteractionListener mListener;

	public HistoryViewAdapter(List<HistoryViewModel> items, OnListFragmentInteractionListener listener) {
		mValues = items;
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_history_row, parent, false);
		return new ViewHolder(view);
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
		public final View mView;
		public final TextView title;
		public final TextView description;
		public final ImageButton edit;
		public final ImageButton delete;
		public HistoryViewModel mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			title = view.findViewById(R.id.history_row_title);
			description = view.findViewById(R.id.history_row_description);
			edit = view.findViewById(R.id.history_row_edit_button);
			delete = view.findViewById(R.id.history_row_delete_button);
		}
	}
}
