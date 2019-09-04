package pg.groupproject.aruma.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.fragments.SavedPointsFragment.OnListFragmentInteractionListener;
import pg.groupproject.aruma.fragments.viewModels.SavedPointsContent.SavedPointViewModel;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SavedPointViewModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SavedPointsViewAdapter extends RecyclerView.Adapter<SavedPointsViewAdapter.ViewHolder> {

    private final List<SavedPointViewModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SavedPointsViewAdapter(List<SavedPointViewModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_savedpoints_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).title);
        holder.description.setText(mValues.get(position).description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
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
        public SavedPointViewModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.saved_points_row_title);
            description = (TextView) view.findViewById(R.id.saved_points_row_description);
            edit = (ImageButton) view.findViewById(R.id.saved_points_row_edit_button);
            delete = (ImageButton) view.findViewById(R.id.saved_points_row_delete_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + description.getText() + "'";
        }
    }
}
