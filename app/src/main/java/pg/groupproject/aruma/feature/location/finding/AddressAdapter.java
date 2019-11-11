package pg.groupproject.aruma.feature.location.finding;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;
import java.util.List;

import pg.groupproject.aruma.R;

class AddressAdapter extends ArrayAdapter<NominatimLocation> {

    private static final int RESOURCE_ID = R.layout.listview_adress_element;
    public static final String NO_VALUE = "-";
    private List<NominatimLocation> mOriginalValues;
    private List<NominatimLocation> mObjects = new ArrayList<>();
    private final Object lock = new Object();

    AddressAdapter(@NonNull Context context) {
        super(context, RESOURCE_ID);
    }

    AddressAdapter(@NonNull Context context, @NonNull List<NominatimLocation> objects) {
        super(context, RESOURCE_ID, objects);
        this.mObjects = objects;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new ArrayFilter();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(RESOURCE_ID, parent, false);
        }
        NominatimLocation address = getItem(position);
        if (address == null) {
            return convertView;
        }

        TextView name = convertView.findViewById(R.id.listview_elem_name);
        name.setText(getNullSafeValue(NominatimLocationFormatting.formatFeatureName(address.getDisplayName())));
        TextView country = convertView.findViewById(R.id.listview_elem_address);
        country.setText(getNullSafeValue(NominatimLocationFormatting.formatAddress(address.getAddress())));

        return convertView;
    }

    private String getNullSafeValue(String value) {
        return Strings.isEmptyOrWhitespace(value) ? NO_VALUE : value;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            final ArrayList<NominatimLocation> list;
            synchronized (lock) {
                list = new ArrayList<>(mOriginalValues);
            }
            results.values = list;
            results.count = list.size();

            Log.e("filter-1", "found: " + mOriginalValues.size());
            Log.e("filter-2", "resul: " + results.count);

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<NominatimLocation>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
