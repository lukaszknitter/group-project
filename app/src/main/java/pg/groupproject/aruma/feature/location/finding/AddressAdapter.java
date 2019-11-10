package pg.groupproject.aruma.feature.location.finding;

import android.content.Context;
import android.location.Address;
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

class AddressAdapter extends ArrayAdapter<Address> {

    private static final int RESOURCE_ID = R.layout.listview_adress_element;
    public static final String NO_VALUE = "-";
    private List<Address> mOriginalValues;
    private List<Address> mObjects;
//    private Lock mLock;

    AddressAdapter(@NonNull Context context) {
        super(context, RESOURCE_ID);
    }

    AddressAdapter(@NonNull Context context, @NonNull List<Address> objects) {
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
        Address address = getItem(position);
        if (address == null) {
            return convertView;
        }

        TextView name = convertView.findViewById(R.id.listview_elem_name);
        name.setText(address.getFeatureName() == null ? NO_VALUE : address.getFeatureName());
        TextView country = convertView.findViewById(R.id.listview_elem_country);
        country.setText(address.getCountryName() == null ? NO_VALUE : address.getCountryName());
        TextView addressT = convertView.findViewById(R.id.listview_elem_address);
        addressT.setText(address.getAddressLine(0) == null ? NO_VALUE : address.getAddressLine(0));


        return convertView;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
//                synchronized (mLock) {
                mOriginalValues = new ArrayList<>(mObjects);
//                }
            }

            if (prefix == null || prefix.length() == 0) {
                final ArrayList<Address> list;
//                synchronized (mLock) {
                list = new ArrayList<>(mOriginalValues);
//                }
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                final ArrayList<Address> values;
//                synchronized (mLock) {
                values = new ArrayList<>(mOriginalValues);
//                }

                final int count = values.size();
                final ArrayList<Address> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final Address value = values.get(i);

                    // First match against the whole, non-splitted value
                    if (!Strings.isEmptyOrWhitespace(value.getFeatureName()) && value.getFeatureName().toLowerCase().startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = value.getFeatureName().split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<Address>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
