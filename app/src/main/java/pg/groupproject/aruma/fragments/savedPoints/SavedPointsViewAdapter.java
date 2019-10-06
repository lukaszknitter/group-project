package pg.groupproject.aruma.fragments.savedPoints;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Getter;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.fragments.common.OnListChangedCallbackInterface;
import pg.groupproject.aruma.fragments.savedPoints.SavedPointsContent.SavedPointViewModel;
import pg.groupproject.aruma.fragments.savedPoints.SavedPointsFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SavedPointViewModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SavedPointsViewAdapter extends RecyclerView.Adapter<SavedPointsViewAdapter.ViewHolder> {

	private final List<SavedPointViewModel> mValues;
	private final OnListFragmentInteractionListener mListener;
	private final OnListChangedCallbackInterface mCallbackInterface;

	public SavedPointsViewAdapter(List<SavedPointViewModel> items,
	                              OnListFragmentInteractionListener listener,
	                              OnListChangedCallbackInterface callbackInterface) {
		mValues = items;
		mListener = listener;
		mCallbackInterface = callbackInterface;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_savedpoints_row, parent, false);
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

	@Getter
	public class ViewHolder extends RecyclerView.ViewHolder {
		private final View mView;
		private final TextView title;
		private final TextView description;
		private final ImageButton edit;
		private final ImageButton delete;
		private SavedPointViewModel mItem;

		ViewHolder(View view, OnListChangedCallbackInterface callback) {
			super(view);
			mView = view;
			title = view.findViewById(R.id.saved_points_row_title);
			description = view.findViewById(R.id.saved_points_row_description);
			edit = view.findViewById(R.id.saved_points_row_edit_button);
			delete = view.findViewById(R.id.saved_points_row_delete_button);
			final ImageButton edit = view.findViewById(R.id.saved_points_row_edit_button);
			final ImageButton delete = view.findViewById(R.id.saved_points_row_delete_button);
			edit.setOnClickListener(v -> callback.onListElementEdit(mItem.getId()));
			delete.setOnClickListener(v -> callback.onListElementDelete(mItem.getId()));
		}

		@Override
		public String toString() {
			return super.toString() + " '" + description.getText() + "'";
		}
	}
}
